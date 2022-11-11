package com.ZenPack.Dto.FilterRequestDto;


import com.ZenPack.Dto.FilterNewDto.ColumnFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class FilterRequest {
    private Map<String, ColumnFilter> filterModel;
}
