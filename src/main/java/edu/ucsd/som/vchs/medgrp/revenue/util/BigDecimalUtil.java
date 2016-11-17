/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.inject.Named;

/**
 * Utility class to format BigDecimal values
 * 
 * @author somdev5
 *
 */
@Named("bigDecimalUtil")
public class BigDecimalUtil {
	
	private static final String CURRENCY_FORMAT = "$###,###.##";
	
	private static final String RVU_FORMAT = "###,###";
	
	private static final String PERCENT_FORMAT = "###.##";
	
	public static final BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);

	/**
	 * Converts BigDecimal to Currency String
	 * 
	 * @param value
	 * @return
	 */
	public static final String toCurrencyString(BigDecimal value) {
		return new DecimalFormat(CURRENCY_FORMAT).format(value.doubleValue());
	}
	
	/**
	 * Converts a BigDecimal to RVU format
	 * 
	 * @param value
	 * @return
	 */
	public static final String toRVUString(BigDecimal value) {
		return new DecimalFormat(RVU_FORMAT).format(value.doubleValue());
	}
	
	/**
	 * Converts a BigDecimal to a Percent
	 * 
	 * @param value
	 * @return
	 */
	public static final String toPercentString(BigDecimal value) {
		return new DecimalFormat(PERCENT_FORMAT).format(value.doubleValue()) + "%";
	}
	
	/**
	 * Divides by 100 and adds one for easy percentage multiplication
	 * 
	 * @param percentage
	 * @return
	 */
	public static final BigDecimal toPercentProduct(BigDecimal percentage) {
						
		return BigDecimal.ONE.add(percentage);
	}
	
	/**
	 * isEqual
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static final boolean isEqual(BigDecimal d1, BigDecimal d2) {
		return (d1.setScale(4).compareTo(d2.setScale(4)) == 0);
	}
	
	/**
	 * isLessThan
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static final boolean isLessThan(BigDecimal d1, BigDecimal d2) {
		return (d1.setScale(4).compareTo(d2.setScale(4)) == -1);
	}
	
	/**
	 * isLessThanOrEqual
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static final boolean isLessThanOrEqual(BigDecimal d1, BigDecimal d2) {
		return (isLessThan(d1, d2) || isEqual(d1, d2));
	}
	
	/**
	 * isGreaterThan
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static final boolean isGreaterThan(BigDecimal d1, BigDecimal d2) {
		return (d1.setScale(4).compareTo(d2.setScale(4)) == 1);
	}	
	
	/**
	 * isGreaterThanOrEqual
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static final boolean isGreaterThanOrEqual(BigDecimal d1, BigDecimal d2) {
		return (isGreaterThan(d1, d2) || isEqual(d1, d2));
	}
}
