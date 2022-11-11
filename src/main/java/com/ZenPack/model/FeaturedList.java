package com.ZenPack.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Features")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeaturedList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "parent")
    private Integer parent;
    @Column(name = "resource_id")
    private String featureId;
    @Column(name = "features")
    private String text;
    @Column(name = "icon")
    private String icon;
    @Column(name = "is_setting_menu")
    private Boolean isSettingMenu;
    @Column(name = "feature_url")
    private String featureUrl;

}
