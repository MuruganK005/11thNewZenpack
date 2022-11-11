package com.ZenPack.Dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFilterDto {
	private Integer startRow;
	private Integer endRow;
	private List<String> rowGroupCols;
	private boolean pivotMode;
	private List<String> pivotCols;
	private List<String> valueCols;
	private List<String> groupKeys;
	private Map<String, Map<String,String>> filterModel;
	private List<SortSpecificationDto> sortModel;
}
