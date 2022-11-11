package com.ZenPack.Dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZenPackReportDto {

private long zenpackReportId;
	
	private boolean isAnalytics;
	
	private boolean isQuickAccess;
	
	private boolean isDashboard;
	
	private boolean addToFavorite;
	
	private String favoriteViewName;
	
	private List<Long> reportIds;
	
	private long zenPackId;
}
