package com.tanmay.excelerate.service;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
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
	private static final String DAILY = "d";
	private static final String WEEKLY = "w";
	private static final String MONTHLY = "m";
	private static final String MULTI_WEEKLY = "mw";

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
		List<ReportManager> allReports = fetchAllReport();
		for (ReportManager report : allReports) {
			if (isReportEligible(report)) {
				logger.debug("Elgibile :" + report.getFilename());
				if (!checkDirectoryPresence(report))
					continue;
			} else {
				logger.debug("Not Eligible :" + report.getFilename());
				continue;
			}
			excel.createWorkbook(report, dao);
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

	private boolean isReportEligible(ReportManager report) {
		//Daily Reports
		if (report.getType().equals(DAILY)) {
			if (null == report.getLastGeneratedOn()) {
				return true;
			} else {
				Calendar c = Calendar.getInstance();
				c.setTime(report.getLastGeneratedOn());
				int lastReportGeneratedDay = c.get(Calendar.DAY_OF_WEEK);
				c.setTime(new Date());
				int presentDay = c.get(Calendar.DAY_OF_WEEK);
				return lastReportGeneratedDay != presentDay ? true : false;
			}
		}

		//Custom Daily Reports
		if (report.getType().equals(MULTI_WEEKLY)) {
			List<String> values = Arrays.asList(report.getValue().split("[\\s,]+"));
			for (String strValue : values) {
				Integer value = Integer.parseInt(strValue);
				if (value < 1 && value > 7) {
					dao.logFailure(report, "One of the value is invalid for multi weekly report");
					return false;
				}
			}
			for (String strValue : values) {
				Integer value = Integer.parseInt(strValue);
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				if (dayOfWeek == value)
					return true;
			}
		}

		//Weekly Reports
		if (report.getType().equals(WEEKLY)) {
			Integer value = Integer.parseInt(report.getValue());
			if (value < 1 && value > 7) {
				dao.logFailure(report, "Invalid Value for weekly report");
				return false;
			}
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			return dayOfWeek == value ? true : false;
		}

		//Monthly Reports
		if (report.getType().equals(MONTHLY)) {
			Integer value = Integer.parseInt(report.getValue());
			if (value < 1 && value > 31) {
				dao.logFailure(report, "Invalid Value for monthly report");
				return false;
			}
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
			if (c.getActualMaximum(Calendar.DAY_OF_MONTH) < value && dayOfMonth == c.getActualMaximum(Calendar.DAY_OF_MONTH)) {
				return true;
			}
			return dayOfMonth == value ? true : false;
		}
		return false;
	}
}
