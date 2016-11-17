package edu.ucsd.som.vchs.medgrp.revenue.util.excel;

import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelFormattingContext {
	CellStyle headerStyle, footerStyle;

	/**
	 * @return the headerStyle
	 */
	public CellStyle getHeaderStyle() {
		return headerStyle;
	}

	/**
	 * @param headerStyle the headerStyle to set
	 */
	public void setHeaderStyle(CellStyle headerStyle) {
		this.headerStyle = headerStyle;
	}

	/**
	 * @return the footerStyle
	 */
	public CellStyle getFooterStyle() {
		return footerStyle;
	}

	/**
	 * @param footerStyle the footerStyle to set
	 */
	public void setFooterStyle(CellStyle footerStyle) {
		this.footerStyle = footerStyle;
	}
}
