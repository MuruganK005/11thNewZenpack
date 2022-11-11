package com.ZenPack.Dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZenPackDto {
    private Long zenPackId;
    private String name;
    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedTime;
    private List<MenuDto> menus;
    private List<FeatureDto> features;
    private Boolean inActive=false;
    private String token;
}