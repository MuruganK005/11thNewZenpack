package com.ZenPack.service.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ZenPack.Dto.ReportColumnsDto;
import com.ZenPack.Dto.ReportZenPackDto;
import com.ZenPack.Dto.SearchFilterDto;
import com.ZenPack.Dto.SortSpecificationDto;
import com.ZenPack.Dto.ZenPackDto;
import com.ZenPack.Dto.ZenPackFilterDTO;
import com.ZenPack.Dto.ZenPackReportColumnsDto;
import com.ZenPack.Dto.ZenPackReportDto;
import com.ZenPack.Dto.FilterNewDto.SpecificationResponse;
import com.ZenPack.Specification.SearchRequest;
import com.ZenPack.Specification.SearchSpecification;
//<<<<<<< HEAD
//import com.ZenPack.Dto.FilterDTO;
//import com.ZenPack.Dto.ReportZenPackDto;
//import com.ZenPack.Dto.ZenPackFilterDTO;
//import com.ZenPack.Dto.ZenPackReportDto;
//=======
//import com.ZenPack.Dto.*;
//import com.ZenPack.Dto.FilterNewDto.SpecificationResponse;
//>>>>>>> bf92054eea2008392dad0672eaefeb031d19dd7c
import com.ZenPack.Specification.ZenPackNewSpecification;
import com.ZenPack.exception.ZenPackException;
import com.ZenPack.model.Report;
import com.ZenPack.model.ReportColumns;
import com.ZenPack.model.ReportHeader;
import com.ZenPack.model.User;
import com.ZenPack.model.ZenPack;
import com.ZenPack.model.ZenPackReport;
import com.ZenPack.model.ZenPackReportColumns;
import com.ZenPack.repository.ReportColumnsRepository;
import com.ZenPack.repository.ReportHeaderRepository;
import com.ZenPack.repository.ReportRepository;
import com.ZenPack.repository.UserRepository;
import com.ZenPack.repository.ZenPackReportColumnsRepository;
import com.ZenPack.repository.ZenPackReportRepository;
import com.ZenPack.repository.ZenPackRepository;
import com.ZenPack.service.Services.ZenPackService;
import com.ZenPack.utils.CommonFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ZenPackServiceImpl implements ZenPackService {

	@Autowired
	private ZenPackRepository repository;

	@Autowired
	private ReportHeaderRepository reportHeaderRepo;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ZenPackRepository zenPackRepository;

	@Autowired
	private ReportColumnsRepository reportColumnsRepository;

	@Autowired
	private CommonFunctions commonFunctions;

	@Autowired
	private EntityManager manager;

	@Autowired
	private ZenPackNewSpecification specification;

	@Autowired
	private ZenPackReportRepository zenPackReportRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
    private ZenPackReportColumnsRepository zprRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(ZenPackServiceImpl.class);

	@Override
	public ResponseEntity<ZenPack> saveZenPack(ZenPack zenPack) {
		repository.save(zenPack);
		logger.info("Zen-Pack Saved Successfully");
		return new ResponseEntity<>(zenPack, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ZenPackDto> createZenPack(ZenPackDto zenPackDto) throws ZenPackException {
		Optional<ZenPack> zenPack1 = repository.findByName(zenPackDto.getName());
		if (zenPack1.isPresent() && zenPack1.get().getZenPackId() != zenPackDto.getZenPackId()) {
			throw new ZenPackException(HttpStatus.FOUND, "ZenPackName Already Exist");
		}
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);
		ZenPack zenPack = mapper.map(zenPackDto, ZenPack.class);
		zenPack.setCreatedDate(commonFunctions.getUtcDateTime());
		zenPack.setUpdatedTime(commonFunctions.getUtcDateTime());
		repository.save(zenPack);
		zenPackDto.setZenPackId(zenPack.getZenPackId());
		zenPackDto.setCreatedDate(zenPack.getCreatedDate());
		zenPackDto.setUpdatedTime(zenPack.getUpdatedTime());
		return new ResponseEntity<>(zenPackDto, HttpStatus.OK);
	}

	@Override
	public List<ZenPackDto> getAllZenPack() throws JsonProcessingException {
		List<ZenPack> zenPacks = repository.findAll();
		List<ZenPackDto> zenPackDtos = new ArrayList<>();
		for (ZenPack zenpack : zenPacks) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			ZenPackDto zenPackDto = mapper.map(zenpack, ZenPackDto.class);
			zenPackDto.setZenPackId(zenpack.getZenPackId());
			zenPackDto.setName(zenpack.getName());
			zenPackDto.setMenus(zenpack.getMenus());
			zenPackDto.setFeatures(zenpack.getFeatures());
			zenPackDtos.add(zenPackDto);
		}
		return zenPackDtos;
	}

	@Override
	public String deleteByzenPackId(Long zenPackId) {
		repository.deleteByZenPackId(zenPackId);
		return " Id " + zenPackId + " Deleted SuccessFully";
	}

	@Override
	public ZenPackDto getByZenPackId(Long zenPackId) {
		Optional<ZenPack> zenPack = repository.findByZenPackId(zenPackId);
		if (zenPack != null && zenPack.isPresent()) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			ZenPackDto zenPackDto = mapper.map(zenPack, ZenPackDto.class);
			zenPackDto.setZenPackId(zenPack.get().getZenPackId());
			zenPackDto.setCreatedBy(zenPack.get().getCreatedBy());
			zenPackDto.setUpdatedBy(zenPack.get().getUpdatedBy());
			zenPackDto.setCreatedDate(zenPack.get().getCreatedDate());
			zenPackDto.setUpdatedTime(zenPack.get().getUpdatedTime());
			zenPackDto.setName(zenPack.get().getName());
			zenPackDto.setMenus(zenPack.get().getMenus());
			zenPackDto.setFeatures(zenPack.get().getFeatures());
			return zenPackDto;
		} else {
			return null;
		}
	}

	@Override
	public boolean checkZenPackName(String name) {
		Optional<ZenPack> zenPack = repository.findByName(name);
		if (zenPack.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Page<ZenPack> searchZenPack(SearchRequest request) {
		SearchSpecification<ZenPack> specification = new SearchSpecification<>(request);
		Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
		return repository.findAll(specification, pageable);
	}

	@Override
	public String setActiveOrInActive(Long zenPackId) {
		Optional<ZenPack> entity = repository.findByZenPackId(zenPackId);
		if (entity.isPresent()) {
			entity.get().setInActive(true);
			repository.save(entity.get());
		}
		return "ZenPack " + zenPackId + " Set InActive Successful";
	}

	public ReportHeader createReportHeader(final ReportHeader reportHeader) {
		return this.reportHeaderRepo.save(reportHeader);
	}

	public List<ReportHeader> getAllReportHeader() {
		return this.reportHeaderRepo.findAll();
	}

	public String deleteReportHeaderById(Long reportId) {
		this.reportHeaderRepo.deleteById(reportId);
		return "Deleted Successfully";
	}

	public ReportHeader getReportHeaderById(Long reportId) {
		Optional<ReportHeader> reportHeader = this.reportHeaderRepo.findById(reportId);
		if (reportHeader != null && reportHeader.isPresent()) {
			return reportHeader.get();
		}
		return null;
	}

	public ReportHeader getReportHeaderByName(String reportName) {
		Optional<ReportHeader> reportHeader = this.reportHeaderRepo.findByReportName(reportName);
		if (reportHeader != null && reportHeader.isPresent()) {
			return reportHeader.get();
		}
		return null;
	}

	public String setActiveOrInActive(Boolean inActive, Long zenPackId) {
		Optional<ZenPack> optionalZenPack = repository.findByZenPackId(zenPackId);
		if (optionalZenPack.isPresent()) {
			optionalZenPack.get().setInActive(inActive);
			repository.save(optionalZenPack.get());
		}
		return "ZenPack id " + zenPackId + " has Successfully set to " + inActive;
	}

	public Page<Report> searchReport(SearchRequest request) {
		SearchSpecification<Report> specification = new SearchSpecification<>(request);
		Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
		return reportRepository.findAll(specification, pageable);
	}

	public Page<ReportColumns> searchReportColumns(SearchRequest searchRequest) {
		SearchSpecification<ReportColumns> specification = new SearchSpecification<>(searchRequest);
		Pageable pageable = SearchSpecification.getPageable(searchRequest.getPage(), searchRequest.getSize());
		return reportColumnsRepository.findAll(specification, pageable);
	}

	public SpecificationResponse getList(SearchFilterDto zenpackFilterDTO) {

		String[] columnName = zenpackFilterDTO.getFilterModel().entrySet().stream().findFirst().get().getKey()
				.split("~");
		Specification<ZenPack> specificationZenpacks = null;
		/*
		 * ArrayList<Specification<ZenPack>> zenpackSpecifications = new
		 * ArrayList<Specification<ZenPack>>();
		 */
		for (Map<String, String> zenpackDTO : zenpackFilterDTO.getFilterModel().values()) {
			specificationZenpacks = specification.getZenpacks(zenpackDTO.get("type"), zenpackDTO.get("filter"),
					columnName[1]);

		}
		SpecificationResponse response1 = new SpecificationResponse();
		if (zenpackFilterDTO.getStartRow() > repository.findAll(specificationZenpacks).size()) {
			/*
			 * zenpackFilterDTO.setStartRow(repository.findAll(specificationZenpacks).size()
			 * );
			 */
			return response1;
		}
		if (zenpackFilterDTO.getEndRow() > repository.findAll(specificationZenpacks).size()) {
			zenpackFilterDTO.setEndRow(repository.findAll(specificationZenpacks).size());
			/* return Collections.emptyList(); */
		}
		Sort sort = Sort.by("name").ascending();
		for (SortSpecificationDto sortModel : zenpackFilterDTO.getSortModel()) {
			if (Objects.equals(sortModel.getSort(), "asc")) {
				sort = Sort.by(sortModel.getColId()).ascending();
			} else {
				sort = Sort.by(sortModel.getColId()).descending();
			}
		}
		SpecificationResponse response = new SpecificationResponse();
		response.setData(Collections.singletonList(repository.findAll(specificationZenpacks, sort)
				.subList(zenpackFilterDTO.getStartRow(), zenpackFilterDTO.getEndRow())));
		response.setLastRow(repository.findAll(specificationZenpacks, sort).size());
		// Sort.by("name").ascending();
		return response;
	}

	public Page<ZenPack> getPagedPlays(Specification<ZenPack> spec, Integer page, Integer size,
			ZenPackFilterDTO zenpackFilterDTO) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, zenpackFilterDTO.getField());
		if (zenpackFilterDTO.getSortType().equals("desc")) {
			pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, zenpackFilterDTO.getField());
		}
		if (zenpackFilterDTO.getFilterDTOList().isEmpty()) {
			return repository.findAll(pageRequest);
		}
		return repository.findAll(spec, pageRequest);
	}

	public ResponseEntity<List<ReportZenPackDto>> getReportWithZenPackReprort(long reportId) {

		List<ReportZenPackDto> reportDto = zenPackReportRepository.findAll(reportId);

		return new ResponseEntity<>(reportDto, HttpStatus.OK);
	}

	public boolean createReportForZenPack(ZenPackReportDto report) {
		boolean isSuccess = false;
		
		try {
		
		List<ZenPackReport> zenPackReports = report.getReportIds().stream().map (reportId -> {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			ZenPackReport zenPack = mapper.map(report, ZenPackReport.class);
			zenPack.setReport(reportRepository.getReferenceById(reportId));
			zenPack.setZenPackId(zenPackRepository.getReferenceById(report.getZenPackId()));
			return zenPack;
			
		}).collect(Collectors.toList());
		
		if (!CollectionUtils.isEmpty(zenPackReports)) {
			List <ZenPackReport> results = zenPackReportRepository.saveAll(zenPackReports);
			if (!CollectionUtils.isEmpty(results)) {
				isSuccess = true;
			}
		}
		}catch (Exception e) {
			log.error("Exception occured in createReportForZenPack {}", e);
		}
		return isSuccess;
	}

	public List<ReportZenPackDto> getReportsForZenPack(long zenPackId) {

		List<ZenPackReport> zenPackReports = zenPackReportRepository
				.findByZenPackId(zenPackRepository.findById(zenPackId).get());
		List<ReportZenPackDto> reportZenPackDtos = null;
		if (!CollectionUtils.isEmpty(zenPackReports)) {
			reportZenPackDtos = zenPackReports.parallelStream().map(zenPackReport -> {
				ReportZenPackDto report = new ReportZenPackDto();
				report.setId(zenPackReport.getReport().getId());
				report.setAnalyticsBy(zenPackReport.getReport().getAnalyticsBy());
				report.setFeature(zenPackReport.getReport().getFeature());
				report.setCategory(zenPackReport.getReport().getCategory());
				report.setOsType(zenPackReport.getReport().getOsType());
				report.setDiscoveryType(zenPackReport.getReport().getDiscoveryType());
				report.setAnalytics(zenPackReport.isAnalytics());
				report.setQuickAccess(zenPackReport.isQuickAccess());
				report.setDashboard(zenPackReport.isDashboard());
				report.setAddToFavorite(zenPackReport.isAddToFavorite());
				report.setFavoriteViewName(zenPackReport.getFavoriteViewName());
				return report;
			}).collect(Collectors.toList());
		} else {
			log.warn("No report found for zenPackId {} ", zenPackId);
		}

		return reportZenPackDtos;
		// zenPackReportRepository.findByZenPackId(zenPackRepository.findById(zenPackId).get());
	}
	
	public User getUserByEmail (final String email){
		return userRepository.findByEmail(email);
	}

	@Override
	public List<ReportColumnsDto> getReportColumns(Long ZenPackReportId) {
		List<ZenPackReportColumns> columns = zprRepo.findAllByzenPackReportId(zenPackReportRepository.getReferenceById(ZenPackReportId));
		
		List<ReportColumnsDto> reportZenPackDtos = columns.parallelStream().map(reportColumn -> {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);
		ReportColumnsDto result= mapper.map(reportColumn, ReportColumnsDto.class);
		return result;
		}).collect(Collectors.toList());
		return reportZenPackDtos;
	}

	public boolean createReportColumns(ZenPackReportColumnsDto reportColumns) {
		List<ZenPackReportColumns> zenPackReports = reportColumns.getReportColumns().stream().map (reportColumn -> {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			ZenPackReportColumns zenPack = mapper.map(reportColumn, ZenPackReportColumns.class);
			zenPack.setZenPackReportId(zenPackReportRepository.getReferenceById(reportColumns.getZenPackReportId()));
			return zenPack;
			
		}).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(zenPackReports)) {
			List <ZenPackReportColumns> results = zprRepo.saveAll(zenPackReports);
			if (!CollectionUtils.isEmpty(results)) {
				return true;
			}
		}
		return false;
	}
	
	public Page<ZenPackReportColumnsDto> searchZenPackReportColumns(SearchRequest request) {
		SearchSpecification<ZenPackReportColumns> specification = new SearchSpecification<>(request);
		Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
		
		 Page<ZenPackReportColumns> zenPackReportColumns = zprRepo.findAll(specification, pageable);
		 
		 List<ZenPackReportColumnsDto> zenPackReportColumnsDtos = zenPackReportColumns.getContent().parallelStream().map(zenPackReportColumn -> {
			 
			 ModelMapper mapper = new ModelMapper();
				mapper.getConfiguration().setAmbiguityIgnored(true);
				ZenPackReportColumnsDto tmp = mapper.map(zenPackReportColumn, ZenPackReportColumnsDto.class);				
				return tmp;				
		 }).collect(Collectors.toList());
		 
		 Page<ZenPackReportColumnsDto> page = new PageImpl<ZenPackReportColumnsDto>(zenPackReportColumnsDtos);
		
		return page;
	}
}
