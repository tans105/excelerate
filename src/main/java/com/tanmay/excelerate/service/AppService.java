package com.tanmay.excelerate.service;

import java.sql.SQLException;
import java.util.List;

import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 03-Jul-2017
 */
public interface AppService {
	List<ReportManager> fetchAllReport();
	void generate() throws SQLException;
}
