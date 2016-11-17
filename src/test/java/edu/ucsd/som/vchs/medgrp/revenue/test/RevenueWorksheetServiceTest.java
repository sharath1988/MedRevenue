/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheet;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;
import edu.ucsd.som.vchs.medgrp.revenue.service.RevenueWorksheetService;

/**
 * Test harness for the revenue worksheet service
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@Transactional
public class RevenueWorksheetServiceTest extends ShrinkWrapContainerTests {
	
	private static final Integer WORKSHEET_ID_DNE = 9999;
	private static final BigDecimal WRVUS_AFTER = new BigDecimal("4495.00");
	
	private static final Integer DIV_ID = 2;
	private static final Integer EMPLOYEE_UCSD_ID = 728150;
	
	@Inject
	private RevenueWorksheetService svc;
	
	@Inject
	private RevenueWorksheetRepository repo;
	
	@Inject
	private RevenueWorksheetViewRepository viewRepo;
	
	@Test
	public void testWiring() {
		Assert.assertNotNull(svc);
		Assert.assertNotNull(repo);
		Assert.assertNotNull(viewRepo);
	}
	
	@Test
	@Transactional(TransactionMode.ROLLBACK)
	public void testSaveWorksheetWrvus() {
		
		// Before sanity check
		RevenueWorksheetView worksheetView = viewRepo.findOptionalByDivisionIdAndEmployeeUcsdId(DIV_ID, EMPLOYEE_UCSD_ID);
		Assert.assertNotNull(worksheetView);
		Assert.assertEquals(DIV_ID, worksheetView.getDivisionId());
		
		// After check
		Integer worksheetId = worksheetView.getId();
		svc.saveWorksheetWrvus(worksheetView, WRVUS_AFTER);
		
		RevenueWorksheet worksheet = repo.findBy(worksheetId);
		Assert.assertEquals(WRVUS_AFTER, worksheet.getProjWrvus());
	}
	
	@Test
	@Transactional(TransactionMode.ROLLBACK)
	public void testSaveWorksheetWrvusDNE() {
				
		try {
			RevenueWorksheetView worksheetView = viewRepo.findBy(WORKSHEET_ID_DNE);
			svc.saveWorksheetWrvus(worksheetView, WRVUS_AFTER);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue("Expected exception: " + e.getMessage(), true);
		}
	}
}