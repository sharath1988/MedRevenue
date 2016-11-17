/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.data.DivisionRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.Division;

/**
 * Test harness for DivisionRepositoryTest
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
public class DivisionRepositoryTest extends ShrinkWrapContainerTests {
	
	private static final Integer DIV_ID = 5;
	
	private static final Integer DEPT_ID = 303;
	
	private static final String DIV_NAME = "Cardiology";
	
	private static final String DEPT_NAME = "MEDICINE";
	
	@Inject
	private DivisionRepository repo;

	public void testWiring() {
		Assert.assertNotNull(repo);
	}
	
	@Test
	public void testFindById() {
		Division division = repo.findBy(DIV_ID);
		Assert.assertNotNull(division);
		Assert.assertEquals(DIV_ID, division.getDivisionId());
		Assert.assertEquals(DIV_NAME, division.getDivisionName());
		Assert.assertEquals(DEPT_ID, division.getDepartmentId());
		Assert.assertEquals(DEPT_NAME, division.getDepartmentName());
	}

}
