package com.tanmay.excelerate.dao;

import java.util.List;
import java.util.Map;

import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 03-Jul-2017
 */
public interface AppDao {

	List<ReportManager> fetchAllReport();

	void logFailure(ReportManager report, String string);

	void saveOrUpdateEntity(ReportManager report);
	
	Object[] getColumnNames(String query);
	
	List<Map<String,Object>> extractQueryResult(String query);

}
