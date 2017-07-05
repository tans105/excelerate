package com.tanmay.excelerate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import com.tanmay.excelerate.constants.Constants;
import com.tanmay.excelerate.dao.AppDao;
import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 19-Jun-2017
 */
@Component
public class ExcelUtils {

	@SuppressWarnings("deprecation")
	public static CellStyle styleWorkbookCells(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setColor(IndexedColors.RED.getIndex());
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		return style;
	}

	public void createWorkbook(ReportManager report, AppDao dao) {
		List<Map<String, Object>> list = dao.extractQueryResult(report.getQuery());
		if (list.size() > 0) {
			SXSSFWorkbook workbook = new SXSSFWorkbook();

			SXSSFSheet sheet = workbook.createSheet();

			CellStyle style = styleWorkbookCells(workbook);
			Object[] columnHeaders = null;
			columnHeaders = fetchHeaders(list.get(0));

			/*----------------------------Adding column Title-----------------------------------*/

			int row = 0;
			SXSSFRow rowTitle = sheet.createRow(row);
			SXSSFCell[] cellTitle = new SXSSFCell[columnHeaders.length];

			for (int i = 0; i < columnHeaders.length; i++) {
				cellTitle[i] = rowTitle.createCell(i);
				cellTitle[i].setCellValue(columnHeaders[i].toString().replaceAll("_", " ").toUpperCase());
				cellTitle[i].setCellStyle(style);
			}
			/*---------------------------------------------------------------*/
			row++;

			for (Map<String, Object> map : list) {
				SXSSFRow rowValue = sheet.createRow(row);
				for (int i = 0; i < columnHeaders.length; i++) {
					SXSSFCell[] cell = new SXSSFCell[columnHeaders.length];
					cell[i] = rowValue.createCell(i);
					if (null != map.get(columnHeaders[i])) {
						cell[i].setCellValue(map.get(columnHeaders[i]).toString());
					} else
						cell[i].setCellValue(" ");
				}
				row++;
			}

			//			/*---------------------Sizing the column width------------------------*/
			//			for (int i = 0; i < columnHeaders.length; i++) {
			//				sheet.autoSizeColumn(i);
			//			}
			//			/*---------------------------------------------------------------*/

			write(workbook, report, dao);

		} else {//list size check
			SXSSFWorkbook workbook = new SXSSFWorkbook();

			SXSSFSheet sheet = workbook.createSheet();

			CellStyle style = styleWorkbookCells(workbook);
			Object[] columnHeaders = null;
			columnHeaders = dao.getColumnNames(report.getQuery());

			/*----------------------------Adding column Title-----------------------------------*/

			int row = 0;
			SXSSFRow rowTitle = sheet.createRow(row);
			SXSSFCell[] cellTitle = new SXSSFCell[columnHeaders.length];

			for (int i = 0; i < columnHeaders.length; i++) {
				cellTitle[i] = rowTitle.createCell(i);
				cellTitle[i].setCellValue(columnHeaders[i].toString().replaceAll("_", " ").toUpperCase());
				cellTitle[i].setCellStyle(style);
			}
			write(workbook, report, dao);
			dao.logFailure(report, "Query returned 0 records");
		}
	}

	private void write(Workbook workbook, ReportManager report, AppDao dao) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(getReportDestination(report));
			workbook.write(fos);
			report.setLastGeneratedOn(new Date());
			report.setIsFailing(Boolean.FALSE);
			dao.saveOrUpdateEntity(report);
		} catch (Exception e) {
			e.printStackTrace();
			dao.logFailure(report, e.toString());
		}

	}

	private File getReportDestination(ReportManager report) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String downloadLocation = report.getDownloadLocation();
		File destination = null;
		if (downloadLocation.charAt(downloadLocation.length() - 1) == '/') {
			destination = new File(report.getDownloadLocation() + report.getFilename() + "_" + Constants.REPORT_TYPE_NAME_MAPPING.get(report.getType()) + "_" + sdf.format(timestamp) + ".xls");
		} else {
			destination = new File(report.getDownloadLocation() + "/" + report.getFilename() + "_" + Constants.REPORT_TYPE_NAME_MAPPING.get(report.getType()) + "_" + sdf.format(timestamp) + ".xls");
		}
		return destination;
	}

	private Object[] fetchHeaders(Map<String, Object> map) {
		LinkedList<String> columnHeaderList = new LinkedList<String>();
		for (String key : map.keySet()) {
			columnHeaderList.add(key);
		}
		return columnHeaderList.toArray();
	}

}
