package com.ZenPack.Dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReportColumnsDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -402339180008616145L;
	
	private String reportDataId;
	private String deviceType;
	private String reportName;
	private String dataType;
	private String columnName;
	private String isSizeMatrics;
	private String seq;
	private String reportColumns;
	private String reportBy;
	private String dbFieldName;
	private boolean isPinned;
	private String tasklistSubCategory;
	private String aliasName;
	private String devices;
	private String tasklistCategory;
	private String categorySeq;
	private String subCategorySeq;
	private boolean hide;
}
