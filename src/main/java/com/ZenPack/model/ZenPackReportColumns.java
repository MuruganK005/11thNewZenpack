package com.ZenPack.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "zen_pack_report_column")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class ZenPackReportColumns implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 359873533032660286L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zen_pack_report__column_id")
	private long zenpackReportColumnId;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "zen_pack_report_id", referencedColumnName = "zen_pack_report_id")
    private ZenPackReport zenPackReportId;
	
	@Column(name = "data_type")
	private String dataType;
	
	@Column(name = "is_size_metrics")
	private Boolean isSizeMetrics;
	
	@Column(name = "seq")
	private Integer sequence;
	
	@Column(name = "column_name")
	private String columnName;
	
	@Column(name = "report_by")
	private String reportBy;
	
	@Column(name = "db_field_name")
	private String dbFieldName;
	
	@Column(name = "is_pinned")
	private Boolean isPinned;
	
	@Column(name = "tasklist_sub_category")
	private String tasklistSubCategory;
	
	@Column(name = "alias_name")
	private String aliasName;
	
	@Column(name = "devices")
	private String devices;
	
	@Column(name = "tasklist_category")
	private String taskListCategory;
	
	@Column(name = "category_seq")
	private String categorySequence;
	
	@Column(name = "hide")
	private Boolean isHide;
	
	@Column(name = "takslist_sub_category")
	private String taskListSubCategory;
	
}
