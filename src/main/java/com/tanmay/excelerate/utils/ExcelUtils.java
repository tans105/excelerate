package com.tanmay.excelerate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.tanmay.excelerate.dao.AppDao;
import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 19-Jun-2017
 */
public class ExcelUtils {

	public static HSSFCellStyle styleWorkbookCells(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.OLIVE_GREEN.index);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.RED.index);
		font.setBoldweight((short) 5);
		style.setFont(font);
		return style;
	}

	@SuppressWarnings("unused")
	public static void createWorkbook(ReportManager report, AppDao dao) {
		List<Map<String, Object>> list = dao.executeSQlQueryReturnAsListOfMaps(report.getReportId(), report.getQuery());
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		HSSFCellStyle style = styleWorkbookCells(workbook);
		Object[] columnHeaders = null;
		if (list.size() > 0) {
			columnHeaders = findAndFormatHeaders(list.get(0));
		}

		/*----------------------------Adding column Title-----------------------------------*/

		int row = 0;
		HSSFRow rowTitle = sheet.createRow(row);
		HSSFCell[] cellTitle = new HSSFCell[columnHeaders.length];

		for (int i = 0; i < columnHeaders.length; i++) {
			cellTitle[i] = rowTitle.createCell(i);
			cellTitle[i].setCellValue(columnHeaders[i].toString().replaceAll("_", " ").toUpperCase());
			cellTitle[i].setCellStyle(style);
		}
		/*---------------------------------------------------------------*/
		row++;
		HSSFRow emptyRow = sheet.createRow(row);
		row++;

		for (Map<String, Object> map : list) {
			HSSFRow rowValue = sheet.createRow(row);
			for (int i = 0; i < columnHeaders.length; i++) {
				HSSFCell[] cell = new HSSFCell[columnHeaders.length];
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
			fos = new FileOutputStream(new File("ExcelSheet.xls"));
			workbook.write(fos);
			System.out.println("<-------------------------------------------FILE CREATED----------------------------->");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object[] findAndFormatHeaders(Map<String, Object> map) {
		LinkedList<String> columnHeaderList = new LinkedList<String>();
		for (String key : map.keySet()) {
			columnHeaderList.add(key);
		}
		return columnHeaderList.toArray();
	}

}
