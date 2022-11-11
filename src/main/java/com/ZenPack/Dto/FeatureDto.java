package com.ZenPack.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureDto {

    private Integer id;
    private Integer parent;
    private String featureId;
    private String text;
    private String icon;
    private Boolean isSettingMenu;
    private String featureUrl;

}
