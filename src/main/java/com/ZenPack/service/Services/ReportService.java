package com.ZenPack.service.Services;


import com.ZenPack.Dto.ZenPackFilterDTO;
import com.ZenPack.model.Report;

import java.util.List;

public interface ReportService {
    List<Report> getReportList(ZenPackFilterDTO zenpackFilterDTO);
    Report createReport(Report report);
    List<Report> getAllReports();
}


