package com.ZenPack.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.ZenPack.Dto.HeaderInfoDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REPORT_HEADER", uniqueConstraints = {
		@UniqueConstraint(name = "UniqueReportName", columnNames = { "REPORT_NAME" }) })
@JsonInclude(JsonInclude.Include.NON_NULL)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
public class ReportHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REPORT_ID")
	private Long reportId;

	@Column(name = "REPORT_NAME", unique = true)
	private String reportName;

	@Type(type = "jsonb") // See (2)
	@Column(name = "HEADERINFO", columnDefinition = "jsonb")
	private List<HeaderInfoDto> headerInfo;

	@Type(type = "jsonb") // See (2)
	@Column(name = "COLUMNORDER", columnDefinition = "jsonb")
	private List<String> columnOrder;

}
