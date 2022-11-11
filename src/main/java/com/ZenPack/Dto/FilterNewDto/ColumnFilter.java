package com.ZenPack.Dto.FilterNewDto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "filterType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SetColumnFilter.class, name = "set") })
public class ColumnFilter {
    String filterType;
}

