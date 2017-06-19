package com.tanmay.excelerate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : tanmay
 * @created : 19-Jun-2017
 */
@Entity
@Table(name = "report_failure_archive")
public class ReportFailureArchive implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "report_id")
	private Long reportId;

	@Column(name = "failure_remarks")
	private String failureRemarks;

	@Column(name = "failure_dtm")
	private Date failureDtm;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFailureRemarks() {
		return failureRemarks;
	}

	public void setFailureRemarks(String failureRemarks) {
		this.failureRemarks = failureRemarks;
	}

	public Date getFailureDtm() {
		return failureDtm;
	}

	public void setFailureDtm(Date failureDtm) {
		this.failureDtm = failureDtm;
	}

}
