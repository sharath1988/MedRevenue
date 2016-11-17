package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.util.List;

import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.data.AllEmployeeRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.AllEmployeeViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.DepartmentRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.FutureEmployeeRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetDescRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployee;
import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployeeView;
import edu.ucsd.som.vchs.medgrp.revenue.model.Department;
import edu.ucsd.som.vchs.medgrp.revenue.model.FutureEmployee;
import edu.ucsd.som.vchs.medgrp.revenue.model.FutureEmployeeType;

public class FutureEmployeeServiceImpl implements FutureEmployeeService {
	@Inject
	DepartmentRepository departmentRepository;
	
	@Inject
	FutureEmployeeRepository futureEmployeeRepository;
	
	@Inject
	AllEmployeeRepository allEmployeeRepository;
	
	@Inject
	AllEmployeeViewRepository allEmployeeViewRepository;
	
	@Inject
	RevenueWorksheetDescRepository worksheetDescRepository;
	
	@Inject
	RevenueWorksheetRepository worksheetRepository;
	
	private static final Integer MAX_RESULTS = 10;
	
	@Override
	public List<Department> getDepartmentsByNameLike(String query) {
		return departmentRepository.findByDepartmentNameLike(query, MAX_RESULTS);
	}

	@Override
	public AllEmployeeView addFutureEmployeeAsProvider(FutureEmployee futureEmployee) {
		FutureEmployee saved = futureEmployeeRepository.save(futureEmployee);
		AllEmployee allEmployee = allEmployeeRepository.save(allEmployee(saved));
		persistExternalInfo(allEmployee.getAllEmployeeId(),futureEmployee);
		return allEmployeeView(allEmployee);
	}

	private AllEmployee allEmployee(FutureEmployee saved) {
		AllEmployee allEmployee = new AllEmployee();
		allEmployee.setTbnId(saved.getTbnId());
		return allEmployee;
	}
	
	private void persistExternalInfo(Integer allEmployeeId, FutureEmployee futureEmployee) {
		if (FutureEmployeeType.FACULTY.equals(futureEmployee.getFutureEmployeeType())) {
			callFacultyStoredProcedure(allEmployeeId,futureEmployee);
		}
	}
	
	private AllEmployeeView allEmployeeView(AllEmployee allEmployee) {
		return allEmployeeViewRepository.findBy(allEmployee.getAllEmployeeId());
	}

	private void callFacultyStoredProcedure(Integer allEmployeeId, FutureEmployee futureEmployee) {
		futureEmployeeRepository.addFacultyTbnInfo(allEmployeeId, futureEmployee.getFacultyEmployeeInfo());
	}
}
