package com.tanmay.excelerate.service;

import java.io.File;
import java.util.Date;
import java.util.List;


import com.tanmay.excelerate.dao.AppDao;
import com.tanmay.excelerate.entity.ReportManager;
import com.tanmay.excelerate.utils.ExcelUtils;

/**
 * @author : tanmay
 * @created : 19-Jun-2017
 */
public class AppService {
	AppDao dao;
	private static final long D_HOURS = 24l;
	private static final long W_HOURS = 168l;
	private static final long M_HOURS = 744l;
	private static final String DAILY = "d";
	private static final String WEEKLY = "w";
	private static final String MONTHLY = "m";

	public AppService() {
		dao = new AppDao();
	}

	@SuppressWarnings("unchecked")
	public void generateReport() {
		List<ReportManager> allReports = dao.fetchAllReport();
		for (ReportManager report : allReports) {
			if (eligibleForGeneration(report)) {
				if (!checkDirectoryPresence(report))
					continue;
			} else {
				continue;
			}
			ExcelUtils.createWorkbook(report,dao);
		}
	}

	@SuppressWarnings("unused")
	private void printArray(String[] columnHeaders) {
		for (int i = 0; i < columnHeaders.length; i++) {
			System.out.println(columnHeaders[i]);
		}
	}

	private boolean checkDirectoryPresence(ReportManager report) {
		File f = new File(report.getDownloadLocation());
		if (!f.exists()) {
			if (f.mkdir())
				System.out.println("DIRECTORY CREATED");
			else {
				dao.logFailure(report.getReportId(), "Error creating Directory :" + report.getDownloadLocation());
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
