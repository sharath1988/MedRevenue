/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.jsoup.Jsoup;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.BudgetMetadataQualifer;
import edu.ucsd.som.vchs.medgrp.revenue.model.BudgetMetadata;
import edu.ucsd.som.vchs.medgrp.revenue.util.excel.ExcelFormattingContext;

/**
 * Contains pre and post processor utility methods for data table data exporting
 * 
 * @author somdev5
 *
 */	
@SuppressWarnings("cdi-ambiguous-dependency")
@Named("customDataExporter")
public class CustomDataExporter implements Serializable {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -6576255910648166519L;
	
	private static final DecimalFormat decimalFormat = new DecimalFormat("###,###");
	
	private static final String DECIMAL_WITH_COMMAS_REGEX = "^\\-?(?:\\d{1,3}\\,)*\\d{1,3}(?:\\.\\d+)?$";
	
	@Inject
	private Log log;
	
	@Inject @BudgetMetadataQualifer
	private BudgetMetadata budgetMetadata;
		
	/**
	 * Main post processor method, verifies what object type is being 
	 * formatted and then formats it if it matches.
	 * 
	 * @param xlsWorksheet
	 */
	public void revenueWorksheetPostProcess(Object data) {
		log.info("post processor invoked");
		if (data instanceof HSSFWorkbook) {
			HSSFWorkbook hssfWorkbook = (HSSFWorkbook) data;
			format(hssfWorkbook);
			log.info("post processing data exporter - HSSFWorkbook");
		}
	}
	
	/**
	 * Formats the entire workbook.
	 * 
	 * @param workbook	
	 */
	private void format(HSSFWorkbook workbook) {
		ExcelFormattingContext context = new ExcelFormattingContext();
		CellStyle boldStyle = getBoldCellStyle(workbook);
		context.setHeaderStyle(boldStyle);
		context.setFooterStyle(boldStyle);
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			formatSheet(workbook.getSheetAt(i),context);
		}
	}
	
	/**
	 * Formats a sheet, iterating by row and then auto-sizes the column widths.
	 * 
	 * @param sheet
	 * @param context
	 */
	private void formatSheet(HSSFSheet sheet, ExcelFormattingContext context) {
		int firstRow = sheet.getFirstRowNum();
		int lastRow = sheet.getLastRowNum();
		int lastColumnNumber = -1;
		for (int i = firstRow; i <= lastRow; i++) {
			Row row = sheet.getRow(i);
			if (i == firstRow) {
				lastColumnNumber = row.getLastCellNum();
				processRow(row,context.getHeaderStyle());
			} else if (i == lastRow) {
				processRow(row,context.getFooterStyle());
			} else {
				processRow(row);
			}
		}
		if (lastColumnNumber != -1) {
			for (int j = 0; j <= lastColumnNumber; j++) {
				sheet.autoSizeColumn(j);
			}
		}
	}
	
	/**
	 * Iterators through a row, stripping cells of HTML, replacing any parameterized values, and parsing them for numeric values.
	 * 
	 * @param row
	 */
	private void processRow(Row row) {
		processRow(row,null);
	}

	/**
	 * Iterates through a row, stripping cells of HTML, replacing any parameterized values, parsing them for numeric values, and styling them according to a passed CellStyle.
	 * 
	 * @param row
	 * @param style
	 */
	private void processRow(Row row, CellStyle style) {
		for (Cell cell: row) {
			String value = cell.getStringCellValue();
			value = replaceUiParams(stripHtml(value));
			Double doubleValue = toDouble(value);
			if (doubleValue != null) {
				cell.setCellValue(doubleValue);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			} else {
				cell.setCellValue(value);
			}
			if (style != null) {
				cell.setCellStyle(style);
			}
		}
	}

	/**
	 * Attempts to convert a string cell value to a double, checking 
	 * against a regex first to reduce the amount of excepts thrown.
	 * 
	 * @param s
	 * @return Double value of string, or null if parsing failed.
	 */
	private Double toDouble(String s) {
		if (s.startsWith("(") && s.endsWith(")")) {
			s = s.replace("(", "-");
			s = s.replace(")","");
		}
		if (!s.matches(DECIMAL_WITH_COMMAS_REGEX)) {
			return null;
		}
		try {
			return decimalFormat.parse(s).doubleValue();
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Strips a string of any HTML and returns only the text.
	 * 
	 * @param s
	 * @return A string without markup.
	 */
	private String stripHtml(String s) {
		return Jsoup.parse(s).text();
	}

	/**
	 * Creates a bold cell style.
	 * 
	 * @param workbook
	 * @return New bold cell style for workbook.
	 */
	private CellStyle getBoldCellStyle(HSSFWorkbook workbook) {
		CellStyle boldStyle = workbook.createCellStyle();
		Font boldFont = workbook.createFont();
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		boldStyle.setFont(boldFont);
		return boldStyle;
	}

	/**
	 * Replaces #{currentBudgetYearLabel} & #{priorBudgetYearLabel} text from using ui-includes
	 * to be the actual values.
	 * 
	 * @param cellValue
	 * @return String with replaced parameters.
	 */
	private String replaceUiParams(String cellValue) {
		cellValue = StringUtils.replace(cellValue, "#{budgetMetadata.currentBudgetYearLabel}", budgetMetadata.getCurrentBudgetYearLabel());
		cellValue = StringUtils.replace(cellValue, "#{budgetMetadata.priorBudgetYearLabel}", budgetMetadata.getPriorBudgetYearLabel());
		return cellValue;
	}
}
