package com.tanmay.excelerate.service;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.tanmay.excelerate.dao.AppDao;
import com.tanmay.excelerate.entity.ReportManager;
import com.tanmay.excelerate.utils.ExcelUtils;

/**
 * @author : tanmay
 * @created : 03-Jul-2017
 */
@Service
@Configurable
public class AppServiceImpl implements AppService {
	private static Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);
	private static final long D_HOURS = 24l;
	private static final long W_HOURS = 168l;
	private static final long M_HOURS = 744l;
	private static final String DAILY = "d";
	private static final String WEEKLY = "w";
	private static final String MONTHLY = "m";
	
	@Autowired(required = true)
	private AppDao dao;
	
	@Autowired
	private ExcelUtils excel;

	@Override
	public List<ReportManager> fetchAllReport() {
		return dao.fetchAllReport();
	}

	@Override
	public void generate() throws SQLException {
		List<ReportManager> allReports=fetchAllReport();
		for (ReportManager report : allReports) {
			if (eligibleForGeneration(report)) {
				if (!checkDirectoryPresence(report))
					continue;
			} else {
				continue;
			}
			excel.createWorkbook(report,dao);
		}
	}
	
	private boolean checkDirectoryPresence(ReportManager report) {
		File f = new File(report.getDownloadLocation());
		if (!f.exists()) {
			if (f.mkdir())
				logger.debug("DIRECTORY CREATED");
			else {
				logger.debug("Error creating Directory");
				dao.logFailure(report, "Error creating Directory :" + report.getDownloadLocation());
				return false;
			}
		}
		return true;
	}
	
	private boolean eligibleForGeneration(ReportManager report) {
		Boolean isEligible = Boolean.FALSE;
		if (null == report.getLastGeneratedOn())
			return true;
		long diffHours = (new Date().getTime() - report.getLastGeneratedOn().getTime()) / (60 * 60 * 1000);
		if (report.getType().equals(DAILY) && diffHours >= D_HOURS) {
			isEligible = true;
		}
		if (report.getType().equals(WEEKLY) && diffHours >= W_HOURS) {
			isEligible = true;
		}
		if (report.getType().equals(MONTHLY) && diffHours >= M_HOURS) {
			isEligible = true;
		}
		return isEligible;
	}
}
