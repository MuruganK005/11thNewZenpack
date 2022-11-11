package com.ZenPack.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ZenPack.Dto.ReportColumnsDto;
import com.ZenPack.Dto.ReportZenPackDto;
import com.ZenPack.Dto.SearchFilterDto;
import com.ZenPack.Dto.SortSpecificationDto;
import com.ZenPack.Dto.SpecificationDto;
import com.ZenPack.Dto.ZenPackDto;
import com.ZenPack.Dto.ZenPackReportColumnsDto;
import com.ZenPack.Dto.ZenPackReportDto;
import com.ZenPack.Dto.FilterNewDto.SpecificationResponse;
import com.ZenPack.Specification.FieldType;
import com.ZenPack.Specification.FilterRequest;
import com.ZenPack.Specification.SearchRequest;
import com.ZenPack.Specification.SortDirection;
import com.ZenPack.Specification.SortRequest;
import com.ZenPack.Specification.ZenpackOperator;
import com.ZenPack.excel.ZenPackExcelExporter;
import com.ZenPack.exception.ZenPackException;
import com.ZenPack.interceptor.JwtTokenUtil;
import com.ZenPack.model.Report;
import com.ZenPack.model.ReportColumns;
import com.ZenPack.model.User;
import com.ZenPack.model.ZenPack;
import com.ZenPack.model.ZenPackReportColumns;
import com.ZenPack.repository.ExcelRepository;
import com.ZenPack.repository.ZenPackReportColumnsRepository;
import com.ZenPack.repository.ZenPackRepository;
import com.ZenPack.service.Impl.ZenPackServiceImpl;
import com.ZenPack.service.Services.ReportService;
import com.ZenPack.service.Services.SpecificationService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ZenPackController {

    @Autowired
    private ZenPackServiceImpl service;

    @Autowired
    private ZenPackRepository zenPackRepository;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private ExcelRepository excelRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;
    
    @Autowired
    private ReportService reportService;

    @PostMapping("/save")
    public ResponseEntity<ZenPack> saveZenPack(@RequestBody ZenPack zenPack,@RequestHeader(value="Authorization") String authorizationHeader) {
    	String email = tokenUtil.getUsernameFromToken(authorizationHeader);
    	User user = service.getUserByEmail(email);
    	
    	zenPack.setUpdatedBy(tokenUtil.getUsernameFromToken(authorizationHeader));
    	zenPack.setUpdatedBy(user.getFirstName());
    	
        return service.saveZenPack(zenPack);
    }

//    @PostMapping("/create")
//    public ResponseEntity<ZenPackDto> createZenPack(@RequestBody ZenPackDto zenPackDto){
//        if(zenPackDto == null || service.checkZenPackName(zenPackDto.getName())){
//    		return new ResponseEntity<>(null,HttpStatus.EXPECTATION_FAILED);
//    	}
//        return service.createZenPack(zenPackDto);
//    }

    @PostMapping("/create")
    public ResponseEntity<ZenPackDto> createZenPack(@RequestBody ZenPackDto zenPackDto, @RequestHeader(value="Authorization") String authorizationHeader) throws ZenPackException, ZenPackException {
    	String email = tokenUtil.getUsernameFromToken(authorizationHeader);
    	User user = service.getUserByEmail(email);
    	
        zenPackDto.setCreatedBy(user.getFirstName());
    	return service.createZenPack(zenPackDto);
    }


    @GetMapping(value = "get_all",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ZenPackDto> getAllZenPack() throws JsonProcessingException {
        return service.getAllZenPack();
    }
    @DeleteMapping("/delete/{zenPackId}")
    public String deleteByZenPackId(@PathVariable Long zenPackId){
        return service.deleteByzenPackId(zenPackId);
    }
    @GetMapping(value = "/getByZenPackId/{zenPackId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ZenPackDto> getByZenPackId(@PathVariable Long zenPackId){
        com.ZenPack.Dto.ZenPackDto result = service.getByZenPackId(zenPackId);
        if(result == null) {
        	return ResponseEntity.notFound().eTag(zenPackId + " not found").build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ZenPack>> getBySpecification(@RequestBody SpecificationDto specificationDto){
        ResponseEntity<Page<ZenPack>> response = specificationService.getBySpecification(specificationDto);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }
    
    @PostMapping("/searchZenPack")
    public Page<ZenPack> searchZenPack(@RequestBody SearchFilterDto request) {
    	
        return service.searchZenPack(getSearchRequest(request));
    }
    
    @GetMapping(value = "checkZenPackName",produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean checkZenPackNameExists(@RequestParam String name) throws JsonProcessingException {
        return service.checkZenPackName(name);
    }

    private SearchRequest getSearchRequest(SearchFilterDto searchFilter) {
    	SearchRequest request = new SearchRequest();
    	Map<String, Map<String,String>> filter = searchFilter.getFilterModel();
    	List<SortSpecificationDto> sortModel = searchFilter.getSortModel();
    	List<FilterRequest> filterRequest = new ArrayList<>();
    	List<SortRequest> sortRequest = new ArrayList<>();
    	Iterator<String> keyItr = filter.keySet().iterator();
    	while(keyItr.hasNext()) {
    		String filterName = keyItr.next();
    		Map<String,String> filterValues = filter.get(filterName);
    		FilterRequest filterReq = new FilterRequest();
    		filterReq.setFieldType(FieldType.valueOf(filterValues.get("filterType").toUpperCase()));
        	filterReq.setOperator(ZenpackOperator.valueOf(filterValues.get("type").toUpperCase()));
        	filterReq.setKey(filterName);
        	filterReq.setValue(filterValues.get("filter"));
        	filterRequest.add(filterReq);
    	}
    	for(SortSpecificationDto sort: sortModel) {
    		SortRequest req = new SortRequest();
    		req.setDirection(SortDirection.valueOf(sort.getSort().toUpperCase()));
    		req.setKey(sort.getColId());
    		sortRequest.add(req);
    	}
    	request.setFilters(filterRequest);
    	request.setSorts(sortRequest);
    	return request;
    }
    
    @PostMapping("/export/excel")//new one
    public void exportToExcel(@RequestBody SearchFilterDto searchFilterDto, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headervalue = "attachment; filename=ZenPack_info"+currentDateTime+".xlsx";
        response.setContentType(searchFilterDto.toString());
        response.setHeader(headerKey, headervalue);
        PageRequest pageRequest = PageRequest.of(searchFilterDto.getStartRow(),searchFilterDto.getEndRow());
        Page<ZenPack> listStudent = excelRepository.findAll(pageRequest);
        ZenPackExcelExporter exp = new ZenPackExcelExporter(listStudent.getContent());
        exp.export(searchFilterDto,response);
    }

    @DeleteMapping("/set_in_active/{zenPackId}")
    public String setZenPackActiveOrInActive(@PathVariable Long zenPackId){
        return  service.setActiveOrInActive(zenPackId);
    }

    @PutMapping("/setActiveOrInActive")
    public String setActiveOrInActive(@RequestParam Boolean inActive,@RequestParam Long zenPackId){
        return service.setActiveOrInActive(inActive,zenPackId);
    }
    

    
    @PostMapping("/searchReport")
    public ResponseEntity<Page<Report>> getReportBySpecification(@RequestBody SpecificationDto specificationDto){
        ResponseEntity<Page<Report>> response = specificationService.getReportBySpecification(specificationDto);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }
    
    @PostMapping("/searchReportByFilter")
    public Page<Report> searchReport(@RequestBody SearchFilterDto request) {
    	
        return service.searchReport(getSearchRequest(request));
    }
    
    @PostMapping("/searchReportColumns")
    public ResponseEntity<Page<ReportColumns>> getReportColumnsBySpecification(@RequestBody SpecificationDto specificationDto){
        ResponseEntity<Page<ReportColumns>> response = null;
//        		specificationService.getReportColumnsBySpecification(specificationDto);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }
    
    @PostMapping("/searchReportColumnsByFilter")
    public Page<ReportColumns> searchReportColumns(@RequestBody SearchFilterDto request) {
    	
        return service.searchReportColumns(getSearchRequest(request));
    }

    @PostMapping("/search_zen_pack_list")
    public SpecificationResponse getList(@RequestBody SearchFilterDto zenpackFilterDTO) {
        return service.getList(zenpackFilterDTO);
    }
    
    @PostMapping("/createReportForZenPack")
    ResponseEntity<?> createZenPackReport(@RequestBody ZenPackReportDto report){
    	ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	
    	if (service.createReportForZenPack(report)) {
    		responseEntity = new ResponseEntity<>(HttpStatus.OK);
    	}else {
    		log.error("Create Zenpack report failed");
    	}
    	
    	return responseEntity;
    }
    
    @GetMapping({"/getReportWithZenPack/{zenPackId}","/getReportWithZenPack"})
    public ResponseEntity<List<ReportZenPackDto>> getReportByZenPackId(@PathVariable("zenPackId") java.util.Optional<Long> zenPackId ){
    	
    	ResponseEntity<List<ReportZenPackDto>> responseEntity = new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
       List<Report> reports = new ArrayList<>();
        if(zenPackId.isPresent() ) {
        	 List<ReportZenPackDto> response = service.getReportsForZenPack(zenPackId.get());
        	 
        	 if (!CollectionUtils.isEmpty(response)) {
        		 responseEntity = new ResponseEntity<>(response,HttpStatus.OK);
        	 }
        	 
				/*
				 * for(ZenPackReport zpr: response) { report.add(zpr.getReport()); }
				 */
        } else {
        	reports = reportService.getAllReports();
        	
        	List <ReportZenPackDto> reportZenPackDtos = null;        	
        	if (!CollectionUtils.isEmpty(reports)) {
    			reportZenPackDtos = reports.parallelStream().map(report -> {
    				 
    				ModelMapper mapper = new ModelMapper();
    				mapper.getConfiguration().setAmbiguityIgnored(true);
    				ReportZenPackDto zenPackReportDto = mapper.map(report, ReportZenPackDto.class);   				
    				
    				
    				return zenPackReportDto;
    			}).collect(Collectors.toList());
    			
    			if (!CollectionUtils.isEmpty(reportZenPackDtos)) {
           		 responseEntity = new ResponseEntity<>(reportZenPackDtos,HttpStatus.OK);
           	 }
    			
    		}else {
    			log.warn("No report found for zenPackId {} ", zenPackId);
    		}
        }
        return responseEntity;
    }
    
    @GetMapping("/getReportColumns/{zenPackReportId}")
    public ResponseEntity<List<ReportColumnsDto>> getReportByZenPackId(@PathVariable("zenPackReportId") Long zenPackReportId){
		
    	return new ResponseEntity<List<ReportColumnsDto>>(service.getReportColumns(zenPackReportId), HttpStatus.OK);
    	
    }
    
    @PostMapping("/createReportColumns")
    public ResponseEntity<Boolean> createReportColumns(@RequestBody ZenPackReportColumnsDto reportColumns)
    {
    	return new ResponseEntity<Boolean>(service.createReportColumns(reportColumns), HttpStatus.OK);
    }
    
    @PostMapping("/searchZenPackReportColumns")
    public Page<ZenPackReportColumnsDto> searchZenPackReportColumns(@RequestBody SearchFilterDto request) {
    	
        return service.searchZenPackReportColumns(getSearchRequest(request));
    }
    
}
