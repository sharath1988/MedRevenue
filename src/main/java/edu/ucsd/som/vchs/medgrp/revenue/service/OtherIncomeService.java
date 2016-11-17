package edu.ucsd.som.vchs.medgrp.revenue.service;

import edu.ucsd.som.vchs.medgrp.revenue.model.OtherIncome;

public interface OtherIncomeService {
	OtherIncome find(Integer divisionId);
	Integer getTotal(Integer divisionId);
	void save(OtherIncome otherIncome);
}