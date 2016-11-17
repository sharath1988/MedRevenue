package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.util.List;

import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.data.DepartmentViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentRollupTotals;
import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentView;

public class DepartmentRollupServiceImpl implements DepartmentRollupService {
	private final DepartmentViewRepository repository;
	
	@Inject
	public DepartmentRollupServiceImpl(DepartmentViewRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public String getDepartmentName(Integer departmentId) {
		List<DepartmentView> departmentRollup = getDepartmentRollup(departmentId);
		return departmentRollup.size() > 0 ? departmentRollup.get(0).getDepartmentName() : "";
	}

	@Override
	public List<DepartmentView> getDepartmentRollup(Integer departmentId) {
		return repository.findByDepartmentId(departmentId);
	}

	@Override
	public DepartmentRollupTotals getTotals(List<DepartmentView> departmentRollup) {
		DepartmentRollupTotals totals = new DepartmentRollupTotals();
		for (DepartmentView division: departmentRollup) {
			totals.setPriorYearCharges(totals.getPriorYearCharges().add(division.getPriorYearCharges()));
			totals.setPriorYearCollections(totals.getPriorYearCollections().add(division.getPriorYearCollections()));
			totals.setPriorYearTRVUs(totals.getPriorYearTRVUs().add(division.getPriorYearTRVUs()));
			totals.setPriorYearWRVUs(totals.getPriorYearWRVUs().add(division.getPriorYearWRVUs()));
			
			totals.setProjectedCharges(totals.getProjectedCharges().add(division.getProjectedCharges()));
			totals.setProjectedCollections(totals.getProjectedCollections().add(division.getProjectedCollections()));
			totals.setProjectedTRVUs(totals.getProjectedTRVUs().add(division.getProjectedTRVUs()));
			totals.setProjectedWRVUs(totals.getProjectedWRVUs().add(division.getProjectedWRVUs()));
		}
		return totals;
	}
}
