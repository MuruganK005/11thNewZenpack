package com.ZenPack.controller;



import com.ZenPack.Dto.ZenPackFilterDTO;
import com.ZenPack.model.Report;
import com.ZenPack.service.Impl.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ReportController {
    @Autowired
    private ReportServiceImpl service;

    @PostMapping("/report_list")
    public List<Report> getAllReportsWithFilters(@RequestBody ZenPackFilterDTO zenpackFilterDTO){
        return service.getReportList(zenpackFilterDTO);
    }

    @PostMapping("/createReport")
    public ResponseEntity<Report> createReport(@RequestBody Report report){
        return new ResponseEntity<>(service.createReport(report),HttpStatus.OK);
    }
}

