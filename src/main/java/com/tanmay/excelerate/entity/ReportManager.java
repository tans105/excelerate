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
@Table(name = "report_manager")
public class ReportManager implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long reportId;

	@Column(name = "query")
	private String query;

	@Column(name = "filename")
	private String filename;

	@Column(name = "type")
	private String type;

	@Column(name = "download_location")
	private String downloadLocation;

	@Column(name = "last_generated_on")
	private Date lastGeneratedOn;

	@Column(name = "is_failing")
	private Boolean isFailing;

	@Column(name = "last_failure_dtm")
	private Date lastFailureDtm;

	@Column(name = "active")
	private Boolean active;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDownloadLocation() {
		return downloadLocation;
	}

	public void setDownloadLocation(String downloadLocation) {
		this.downloadLocation = downloadLocation;
	}

	public Date getLastGeneratedOn() {
		return lastGeneratedOn;
	}

	public void setLastGeneratedOn(Date lastGeneratedOn) {
		this.lastGeneratedOn = lastGeneratedOn;
	}

	public Boolean getIsFailing() {
		return isFailing;
	}

	public void setIsFailing(Boolean isFailing) {
		this.isFailing = isFailing;
	}

	public Date getLastFailureDtm() {
		return lastFailureDtm;
	}

	public void setLastFailureDtm(Date lastFailureDtm) {
		this.lastFailureDtm = lastFailureDtm;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
