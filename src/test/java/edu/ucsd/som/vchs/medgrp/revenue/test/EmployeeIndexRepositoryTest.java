/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.LoggedInUcsdId;
import edu.ucsd.som.vchs.medgrp.revenue.auth.EmployeeIndex;
import edu.ucsd.som.vchs.medgrp.revenue.auth.EmployeeIndexRepository;

/**
 * Test harness for employee index repository.  Includes both positive
 * and negative test methods for retrieving employee indexes.
 * 
 * @author somdev5
 * @see TestResources.java:  Injects loggedInUcsdId
 */
@SuppressWarnings("cdi-ambiguous-dependency")
public class EmployeeIndexRepositoryTest extends ShrinkWrapContainerTests {

	private static final String TEST_REAL_INDEX = "CAR0742";
	
	private static final String TEST_FAKE_INDEX = "MJY1234";
	
	private static final Integer TEST_EMP_INDEX_ID = 7290;
	
	private static final Integer EXPECTED_EMP_UCSD_ID = 209166;
	
	
	@Inject @LoggedInUcsdId
	private Integer employeeUcsdId;
	
	@Inject
	private EmployeeIndexRepository employeeIndexRepository;
	
	@Test
	public void testWiring() {
		Assert.assertEquals(EXPECTED_EMP_UCSD_ID, employeeUcsdId);
		Assert.assertNotNull(employeeIndexRepository);
	}

	@Test
	public void testFindByEmpIndexId() {
		EmployeeIndex idx = employeeIndexRepository.findBy(TEST_EMP_INDEX_ID);
		Assert.assertNotNull(idx);
		Assert.assertEquals(TEST_EMP_INDEX_ID, idx.getEmpIndexId());
		Assert.assertEquals("MSCRO00", idx.getIndex());
		Assert.assertEquals(new Integer("654"), idx.getEmployeeUcsdId());
	}
	
	@Test
	public void testFindByFakeEmpIndexId() {
		EmployeeIndex idx = employeeIndexRepository.findBy(new Integer("9999999"));
		Assert.assertNull(idx);
	}	
	
	@Test
	public void testFindByIndexAndEmployeeUcsdId() {
		EmployeeIndex idx = employeeIndexRepository.findOptionalByIndexAndEmployeeUcsdId(TEST_REAL_INDEX, employeeUcsdId);
		Assert.assertNotNull(idx);
		Assert.assertEquals(TEST_REAL_INDEX, idx.getIndex());
		Assert.assertEquals(EXPECTED_EMP_UCSD_ID, idx.getEmployeeUcsdId());
	}
	
	@Test
	public void testFindByFakeIndexAndEmployeeUcsdId() {
		EmployeeIndex idx = employeeIndexRepository.findOptionalByIndexAndEmployeeUcsdId(TEST_FAKE_INDEX, employeeUcsdId);
		Assert.assertNull(idx);		
	}
	
	@Test
	public void testFindByEmployeeUcsdId() {
		List<EmployeeIndex> idxList = employeeIndexRepository.findByEmployeeUcsdId(employeeUcsdId);
		Assert.assertNotNull(idxList);
		Assert.assertTrue(idxList.size() > 100);
	}
	
	@Test
	public void testFindByFakeEmployeeUcsdId() {
		List<EmployeeIndex> idxList = employeeIndexRepository.findByEmployeeUcsdId(new Integer("123456789"));
		Assert.assertNotNull(idxList);
		Assert.assertEquals(0, idxList.size());
	}	
}
