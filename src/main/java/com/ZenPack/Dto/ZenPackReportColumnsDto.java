package com.ZenPack.Dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ZenPackReportColumnsDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 8926000146702994647L;

	private Long zenPackReportId;
	
	private List<ReportColumnsDto> reportColumns;
}
