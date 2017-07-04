package com.tanmay.excelerate.dao;

import java.util.List;

import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 03-Jul-2017
 */
public interface AppDao {

	List<ReportManager> fetchAllReport();

	void logFailure(ReportManager report, String string);

	void saveOrUpdateEntity(ReportManager report);

}
