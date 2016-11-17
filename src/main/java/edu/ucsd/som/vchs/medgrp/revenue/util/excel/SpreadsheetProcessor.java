package edu.ucsd.som.vchs.medgrp.revenue.util.excel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

@ApplicationScoped
@Named
public class SpreadsheetProcessor {
	
	/**
	 * Make some nice generic formatting for a spreadsheet
	 * @param document spreadsheet that gets passed in from export button in Primefaces
	 * @throws ParseException 
	 */
	public void postProcessor(Object document) throws ParseException {

		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HashMap<Integer, Double> totals = new HashMap<Integer, Double>();

		HSSFCellStyle boldStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(Font.COLOR_NORMAL);
		font.setFontHeightInPoints((short) 10);
		boldStyle.setFont(font);
		boldStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		boldStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		boldStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		DataFormat format = wb.createDataFormat();
		HSSFCellStyle numberStyle = wb.createCellStyle();
		numberStyle.setDataFormat(format.getFormat("0.00##"));
		
		HSSFCellStyle currencyStyle = wb.createCellStyle();
		currencyStyle.setDataFormat((short)7);
		
		HSSFCellStyle totalStyle = wb.createCellStyle();
		totalStyle.setDataFormat((short)7);
		totalStyle.setFont(font);
		totalStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		

		Row row = sheet.getRow(0);
		int last = row.getLastCellNum();
		for (int j = 0; j < last; j++) {
			Cell cell = row.getCell(j);
			cell.setCellStyle(boldStyle);
			sheet.setColumnWidth(j, 5000);
		}

		
		Double previousValue = 0.0;
		for (int r = 1; r <= sheet.getLastRowNum(); r++) {
			row = sheet.getRow(r);
			for (int c = 0; c < last; c++) {
				Cell cell = row.getCell(c);
				String val = cell.getStringCellValue();
				if (isCurrency(val)) {
					Number number = NumberFormat.getCurrencyInstance(Locale.US).parse(val);
					cell.setCellValue(number.doubleValue());
					cell.setCellStyle(currencyStyle);
					// perform calcs to add up the value part of the hashmap
					previousValue = totals.get(c);
					if(previousValue == null) previousValue = 0.0;
					totals.put(c, number.doubleValue() + previousValue);
				} // done if currency
			} // done column
		} // done row
		
		
		/**
		 * Now iterate over the hashmap and put the appropriate values in the 
		 * correct lastrow + 1, columns
		 */		
		int startRow = sheet.getLastRowNum() + 1;
		row = sheet.createRow(startRow);
		Iterator<Entry<Integer, Double>> it = totals.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer, Double> column = it.next();
	        int c = (int) column.getKey();
	        Cell cell = row.createCell(c);
	        cell.setCellValue(column.getValue());
			cell.setCellStyle(totalStyle);
	        it.remove(); 
	    }

	}
	/**
	 * 
	 * @param str String value from a cell in a worksheet
	 * @return boolean to determine if it was a float with a decimal
	 */
	public static boolean isFloat(String str) {
		return str.matches("-?\\d+(\\.\\d+)"); // match a number with optional '-' and mandatory decimal.
	}
	
	public static boolean isCurrency(String str) {
		boolean status = false;
		Number number = null;
		try {
		    number = NumberFormat.getCurrencyInstance(Locale.US).parse(str);
		} catch(ParseException pe) {
		}

		if (number != null) {
		   status = true;
		}
		return status;
	}

}
