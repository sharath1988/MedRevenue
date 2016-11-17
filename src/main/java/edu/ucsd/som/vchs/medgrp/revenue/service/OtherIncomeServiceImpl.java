package edu.ucsd.som.vchs.medgrp.revenue.service;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import edu.ucsd.som.vchs.medgrp.revenue.data.OtherIncomeRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.OtherIncome;

public class OtherIncomeServiceImpl implements OtherIncomeService {
	@Inject
	OtherIncomeRepository repo;
	
	@Override
	public OtherIncome find(Integer divisionId) {
		OtherIncome otherIncome = repo.findBy(divisionId);
		if (otherIncome == null) {
			otherIncome = new OtherIncome();
			otherIncome.setDivisionId(divisionId);
		}
		return otherIncome;
	}
	
	@Override
	public Integer getTotal(Integer divisionId) {
		Integer totalOtherIncome;
		try {
			totalOtherIncome = repo.getOtherIncomeTotal(divisionId);
		} catch (NoResultException e) {
			totalOtherIncome = 0;
		}
		return totalOtherIncome;
	}

	@Override
	public void save(OtherIncome otherIncome) {
		repo.save(otherIncome);
	}
}
