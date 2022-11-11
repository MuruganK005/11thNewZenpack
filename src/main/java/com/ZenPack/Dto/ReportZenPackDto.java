package com.ZenPack.Dto;

import lombok.Data;

@Data
public class ReportZenPackDto {
	
	private long id;
	private String feature;
	private String category;
	private String osType;
	private String discoveryType;
	private String analyticsBy;
	private boolean isAnalytics;
	private boolean isQuickAccess;
	private boolean isDashboard;
	private boolean addToFavorite;
	private String favoriteViewName;
	private int chartCount =0;
	private int validationRuleCount=0;
	public ReportZenPackDto(long id, String feature, String category, String osType, String discoveryType,
			String analyticsBy, boolean isAnalytics, boolean isQuickAccess, boolean isDashboard, boolean addToFavorite,
			String favoriteViewName) {
		super();
		this.id = id;
		this.feature = feature;
		this.category = category;
		this.osType = osType;
		this.discoveryType = discoveryType;
		this.analyticsBy = analyticsBy;
		this.isAnalytics = isAnalytics;
		this.isQuickAccess = isQuickAccess;
		this.isDashboard = isDashboard;
		this.addToFavorite = addToFavorite;
		this.favoriteViewName = favoriteViewName;
	}
	public ReportZenPackDto() {
		
	}
	


}
