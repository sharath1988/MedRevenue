/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.auth.EmployeeDivision;
import edu.ucsd.som.vchs.medgrp.revenue.auth.EmployeeDivisionRepository;

/**
 * Test harness for the MedGrpRevenueDivisionRepositoryTest
 * 
 * TODO write test case for findByDepartmentIdAndEmployeeUcsdId()
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
public class EmployeeDivisionRepositoryTest extends ShrinkWrapContainerTests {
	
	private static final Integer EMPLOYEE_UCSD_ID = 209166;
	
	@Inject
	private EmployeeDivisionRepository repo;
	
	@Test
	public void testWiring() {
		Assert.assertNotNull(repo);
	}

	@Test
	public void testFindById() {
		final String ID = "MedGrp-209166-1";
		EmployeeDivision div = repo.findOptionalById(ID);
		Assert.assertNotNull(div);
		Assert.assertEquals(ID, div.getId());
		Assert.assertEquals(new Integer("316"), div.getDepartmentId());
		Assert.assertEquals(new Integer("1"), div.getDivisionId());
		Assert.assertEquals(EMPLOYEE_UCSD_ID, div.getEmployeeUcsdId());
	}
	
	@Test
	public void testFindByIdDoesNotExist() {
		final String ID = "abcxyz";
		EmployeeDivision div = repo.findOptionalById(ID);
		Assert.assertNull(div);		
	}
		
	@Test
	public void testFindByEmployeeUcsdIdAndGroupArea() {
		List<EmployeeDivision> divList = repo.findOptionalByEmployeeUcsdIdAndGroupArea(EMPLOYEE_UCSD_ID, "MedGrp");
		Assert.assertNotNull(divList);
		Assert.assertTrue(divList.size() > 50);
	}

	@Test
	public void testFindByEmployeeUcsdIdDoesNotExist() {
		List<EmployeeDivision> divList = repo.findOptionalByEmployeeUcsdIdAndGroupArea(new Integer("9999999"), "Abc");
		Assert.assertNotNull(divList);
		Assert.assertEquals(0, divList.size());
	}

	@Test
	public void testFindOptionalByDivisionIdAndEmployeeUcsdId() {
		final Integer DIV_ID = 1;
		final Integer DEPT_ID = 316;
		EmployeeDivision div = repo.findOptionalByDivisionIdAndEmployeeUcsdId(DIV_ID, EMPLOYEE_UCSD_ID);
		Assert.assertNotNull(div);
		Assert.assertEquals(DIV_ID, div.getDivisionId());
		Assert.assertEquals(DEPT_ID, div.getDepartmentId());
		Assert.assertEquals(EMPLOYEE_UCSD_ID, div.getEmployeeUcsdId());
	}
	
	@Test
	public void testFindOptionalByDivisionIdAndEmployeeUcsdIdDNE() {
		final Integer DIV_ID = 999;
		EmployeeDivision div = repo.findOptionalByDivisionIdAndEmployeeUcsdId(DIV_ID, EMPLOYEE_UCSD_ID);
		Assert.assertNull(div);
	}	
}