package com.tanmay.excelerate.service;

import java.util.List;

import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 03-Jul-2017
 */
public interface IBootAppService {
	List<ReportManager> fetchAllReport();
	void generate();
}
