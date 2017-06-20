package com.tanmay.excelerate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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

	public static void createWorkbook(ReportManager report, String[] columnHeaders, AppDao dao) {
		List<Object[]> list = dao.executeQueryReturnAsListOfObject(report.getReportId(), report.getQuery());
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		HSSFCellStyle style = styleWorkbookCells(workbook);

		/*----------------------------Adding column Title-----------------------------------*/

		int row = 0;
		HSSFRow rowTitle = sheet.createRow(row);
		HSSFCell[] cellTitle = new HSSFCell[columnHeaders.length];

		for (int i = 0; i < columnHeaders.length; i++) {
			cellTitle[i] = rowTitle.createCell(i);
			cellTitle[i].setCellValue(columnHeaders[i]);
			cellTitle[i].setCellStyle(style);
		}
		/*---------------------------------------------------------------*/
		row++;
		HSSFRow emptyRow = sheet.createRow(row);
		row++;

		for (Object[] obj : list) {
			HSSFRow rowValue = sheet.createRow(row);
			for (int i = 0; i < obj.length; i++) {
				HSSFCell[] cell = new HSSFCell[obj.length];

				cell[i] = rowValue.createCell(i);
				if (null != obj[i]) {
					cell[i].setCellValue(obj[i].toString());
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
			HSSFCellStyle hsfstyle = workbook.createCellStyle();
			hsfstyle.setBorderBottom((short) 1);
			hsfstyle.setFillBackgroundColor((short) 245);
			workbook.write(fos);
			System.out.println("<-------------------------------------------FILE CREATED----------------------------->");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
