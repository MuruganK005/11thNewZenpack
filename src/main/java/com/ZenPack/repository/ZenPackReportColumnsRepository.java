package com.ZenPack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ZenPack.model.ZenPackReport;
import com.ZenPack.model.ZenPackReportColumns;

public interface ZenPackReportColumnsRepository
		extends JpaRepository<ZenPackReportColumns, Long>, JpaSpecificationExecutor<ZenPackReportColumns> {

	List<ZenPackReportColumns> findAllByzenPackReportId(ZenPackReport zenPackReportId);
}
