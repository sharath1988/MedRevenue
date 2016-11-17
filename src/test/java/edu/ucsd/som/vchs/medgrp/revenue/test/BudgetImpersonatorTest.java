/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.sso.impersonate.AppCode;
import edu.ucsd.som.vchs.sso.impersonate.DeveloperImpersonator;
import edu.ucsd.som.vchs.sso.impersonate.DeveloperImpersonatorRepository;

/**
 * Test case for budget impersonators
 * 
 * @author somdev5
 *
 */
public class BudgetImpersonatorTest extends ShrinkWrapContainerTests {

	@Inject
	private DeveloperImpersonatorRepository impersonatorRepo;

	@Test
	public void testWiring() {
		Assert.assertNotNull(impersonatorRepo);
	}
	
	@Test
	public void testGetBudgetImpersonator() {
		DeveloperImpersonator impersonator = impersonatorRepo.findOptionalByDeveloperIdAndApplicationCode("affmj2", AppCode.BUDGET);
		Assert.assertNotNull(impersonator);
		System.out.println(impersonator.toString());
	}
}
