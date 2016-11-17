package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.util.List;

import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentRollupTotals;
import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentView;

public interface DepartmentRollupService {
	String getDepartmentName(Integer departmentId);
	List<DepartmentView> getDepartmentRollup(Integer departmentId);
	DepartmentRollupTotals getTotals(List<DepartmentView> departmentRollup);
}
