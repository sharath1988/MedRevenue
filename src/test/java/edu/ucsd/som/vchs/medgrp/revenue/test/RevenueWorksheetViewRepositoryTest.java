/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;
import edu.ucsd.som.vchs.medgrp.revenue.test.util.TestResources;

/**
 * Test harness for RevenueWorksheetViewRepository
 * 
 * @author somdev5
 * @see TestResources
 */
@SuppressWarnings("cdi-ambiguous-dependency")
public class RevenueWorksheetViewRepositoryTest extends ShrinkWrapContainerTests {
	
	private static final Integer DIVISION_ID = 5;
	
	private static final Integer DIVISION_ID_DNE = 9999;

	@Inject
	private RevenueWorksheetViewRepository repo;
	
	@Test
	public void testWiring() {
		Assert.assertNotNull(repo);
	}
	
	@Test
	public void testFindOptionalByDivisionId() {
		List<RevenueWorksheetView> worksheets = repo.findOptionalByDivisionId(DIVISION_ID);
		Assert.assertNotNull(worksheets);
		Assert.assertTrue(worksheets.size() > 10);
		
		for (RevenueWorksheetView ws : worksheets) {
			Assert.assertEquals(DIVISION_ID, ws.getDivisionId());
			Assert.assertNotNull(ws.getProjWrvus());
		}
	}
	
	@Test
	public void testFindOptionalByDivisionIdDNE() {
		List<RevenueWorksheetView> worksheets = repo.findOptionalByDivisionId(DIVISION_ID_DNE);
		Assert.assertNotNull(worksheets);
		Assert.assertEquals(0, worksheets.size());
	}	
}
