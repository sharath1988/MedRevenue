/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.auth.AuthController;

/**
 * Test harness for the auth controller.  Includes both positive and negative
 * test cases for testing if the current logged in user has priviledges to
 * view a specific index.
 * 
 * @author somdev5
 * @see TestResources.java:  Injects loggedInUcsdId
 */
public class AuthControllerTest extends ShrinkWrapContainerTests {
	
	private static final String TEST_HAS_INDEX = "CAR0742";
	private static final String TEST_NO_INDEX = "MJY1234";
	
	private static final Integer TEST_HAS_DIV = 1;
	private static final Integer TEST_NO_DIV = 999;
	
	@Inject
	private AuthController authController;
	
	@Test
	public void testWiring() {
		Assert.assertNotNull(authController);
	}

	@Test
	public void testHasPermissionForIndex() {
		boolean hasPerm = authController.hasPermissionForIndex(TEST_HAS_INDEX);
		Assert.assertTrue(hasPerm);
	}
	
	@Test
	public void testDoesNotHavePermissionForIndex() {
		boolean hasPerm = authController.hasPermissionForIndex(TEST_NO_INDEX);
		Assert.assertFalse(hasPerm);
	}
	
	@Test
	public void testHasPermissionForDivision() {
		boolean hasPerm = authController.hasPermissionForDivision(TEST_HAS_DIV);
		Assert.assertTrue(hasPerm);
	}
	
	@Test
	public void testDoesNotHavePermissionForDivision() {
		boolean hasPerm = authController.hasPermissionForDivision(TEST_NO_DIV);
		Assert.assertFalse(hasPerm);
	}	
}