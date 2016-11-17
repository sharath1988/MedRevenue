package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.util.List;

import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployeeView;
import edu.ucsd.som.vchs.medgrp.revenue.model.Department;
import edu.ucsd.som.vchs.medgrp.revenue.model.FutureEmployee;

public interface FutureEmployeeService {
	List<Department> getDepartmentsByNameLike(String query);
	AllEmployeeView addFutureEmployeeAsProvider(FutureEmployee futureEmployee);
}
