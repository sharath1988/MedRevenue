/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheet;
import edu.ucsd.som.vchs.medgrp.revenue.util.BigDecimalUtil;

/**
 * Test case testing retrieval of Revenue worksheet provider
 * 
 * @author somdev5
 *
 */
public class RevenueWorksheetTest extends ShrinkWrapContainerTests {
	
	private static final Integer DIVISION_ID = 2;
	private static final Integer WORKSHEET_ID_NO_PY = 4097;
	private static final Integer WORKSHEET_ID_PY = 23;
	
	@Inject
	private RevenueWorksheetRepository worksheetRepo;
	
	@Test
	@Ignore
	public void testWiring() {
		Assert.assertNotNull(worksheetRepo);
	}

	@Test
	@Ignore
	public void testGetWorksheetWithoutPyById() {
		RevenueWorksheet worksheet = worksheetRepo.findBy(WORKSHEET_ID_NO_PY);
		Assert.assertNotNull(worksheet);
		Assert.assertNull(worksheet.getPriorYearActuals());
	}
	
	@Test
	@Ignore
	public void testGetWorksheetWithPyById() {
		RevenueWorksheet worksheet = worksheetRepo.findBy(WORKSHEET_ID_PY);
		Assert.assertNotNull(worksheet);
		Assert.assertNotNull(worksheet.getPriorYearActuals());
		System.out.println(worksheet.getPriorYearActuals());
	}
	
	@Test
	public void testApplyPercentChange() {

		try {
			BigDecimal percentChangeProduct = BigDecimalUtil.toPercentProduct(new BigDecimal(".33"));
			worksheetRepo.applyPercentChange(DIVISION_ID, percentChangeProduct);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testResetPercentChange() {

		try {
			worksheetRepo.resetPercentChange(DIVISION_ID);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
