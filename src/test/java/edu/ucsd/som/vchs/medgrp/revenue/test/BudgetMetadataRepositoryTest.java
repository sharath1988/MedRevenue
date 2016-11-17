/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.data.BudgetMetadataRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.BudgetMetadata;

/**
 * Test case for BudgetMetadataRepository
 * 
 * @author somdev5
 *
 */
public class BudgetMetadataRepositoryTest extends ShrinkWrapContainerTests {
	
	@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject
	private BudgetMetadataRepository repo;

	@Test
	public void testWiring() {
		Assert.assertNotNull(repo);
	}

	@Test
	public void testGetBudgetMetadata() {
		
		BudgetMetadata metadata = repo.findBy(1);
		Assert.assertNotNull(metadata);
		
	}
	
	@Test
	public void testGetActive() {
		BudgetMetadata metadata = repo.getActive();
		Assert.assertNotNull(metadata);
		Assert.assertTrue(metadata.getActiveFlag());
	}
}
