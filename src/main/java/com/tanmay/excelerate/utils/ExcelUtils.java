package com.tanmay.excelerate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tanmay.excelerate.dao.AppDao;
import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 19-Jun-2017
 */
public class ExcelUtils {

	public static CellStyle styleWorkbookCells(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setColor(IndexedColors.RED.getIndex());
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		return style;
	}

	@SuppressWarnings("unused")
	public static void createWorkbook(ReportManager report, AppDao dao) {
		List<Map<String, Object>> list = dao.executeSQlQueryReturnAsListOfMaps(report, report.getQuery());
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet();

		CellStyle style = styleWorkbookCells(workbook);
		Object[] columnHeaders = null;
		if (list.size() > 0) {
			columnHeaders = findAndFormatHeaders(list.get(0));
		}

		/*----------------------------Adding column Title-----------------------------------*/

		int row = 0;
		Row rowTitle = sheet.createRow(row);
		Cell[] cellTitle = new Cell[columnHeaders.length];

		for (int i = 0; i < columnHeaders.length; i++) {
			cellTitle[i] = rowTitle.createCell(i);
			cellTitle[i].setCellValue(columnHeaders[i].toString().replaceAll("_", " ").toUpperCase());
			cellTitle[i].setCellStyle(style);
		}
		/*---------------------------------------------------------------*/
		row++;
		Row emptyRow = sheet.createRow(row);
		row++;

		for (Map<String, Object> map : list) {
			Row rowValue = sheet.createRow(row);
			for (int i = 0; i < columnHeaders.length; i++) {
				Cell[] cell = new Cell[columnHeaders.length];
				cell[i] = rowValue.createCell(i);
				if (null != map.get(columnHeaders[i])) {
					cell[i].setCellValue(map.get(columnHeaders[i]).toString());
				} else
					cell[i].setCellValue(" ");
			}
			row++;
		}

		/*---------------------Sizing the column width------------------------*/
		for (int i = 0; i < columnHeaders.length; i++) {
			sheet.autoSizeColumn(i);
		}
		/*---------------------------------------------------------------*/ 

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

	private static File getReportDestination(ReportManager report) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		File destination = new File(report.getDownloadLocation() + "/" + report.getFilename() + "_" + sdf.format(timestamp) + ".xls");
		return destination;
	}

	private static Object[] findAndFormatHeaders(Map<String, Object> map) {
		LinkedList<String> columnHeaderList = new LinkedList<String>();
		for (String key : map.keySet()) {
			columnHeaderList.add(key);
		}
		return columnHeaderList.toArray();
	}

}
