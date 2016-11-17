package edu.ucsd.som.vchs.medgrp.revenue.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import edu.ucsd.som.vchs.medgrp.revenue.data.DepartmentViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentView;
import edu.ucsd.som.vchs.medgrp.revenue.test.ShrinkWrapContainerTests;

public class DepartmentViewRepositoryTest extends ShrinkWrapContainerTests {
	@Inject
	private DepartmentViewRepository repo;
	
	@Test
	public void testWiring() {
		assertNotNull(repo);
	}
	
	@Test
	public void testFindByDepartmentId() {
		List<DepartmentView> divisions = repo.findByDepartmentId(303);
		assertNotNull(divisions);
		assertFalse(divisions.isEmpty());
		for (DepartmentView division: divisions) {
			assertNotNull(division.getDepartmentId());
			assertNotNull(division.getDepartmentName());
			assertNotNull(division.getDivisionId());
			assertNotNull(division.getPriorYearCharges());
			assertNotNull(division.getPriorYearCollections());
			assertNotNull(division.getPriorYearTRVUs());
			assertNotNull(division.getPriorYearWRVUs());
			assertNotNull(division.getProjectedCharges());
			assertNotNull(division.getProjectedCollections());
			assertNotNull(division.getProjectedTRVUs());
			assertNotNull(division.getProjectedWRVUs());
		}
	}
}
