package com.ZenPack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ZenPack.model.ReportHeader;

public interface ReportHeaderRepository extends JpaRepository<ReportHeader, Long>{

	Optional<ReportHeader> findByReportName(String name);
}
