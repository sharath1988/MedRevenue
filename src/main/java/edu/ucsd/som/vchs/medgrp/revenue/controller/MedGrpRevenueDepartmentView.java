package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentRollupTotals;
import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentView;
import edu.ucsd.som.vchs.medgrp.revenue.service.DepartmentRollupService;

@ViewScoped
@ManagedBean(name="medGrpRevenueDepartmentView")
public class MedGrpRevenueDepartmentView {
	@Inject
	private DepartmentRollupService service;

	private Integer departmentId;
	private String departmentName;
	private List<DepartmentView> departmentRollup;
	private DepartmentRollupTotals totals;
	
	public void loadData() {
		departmentName = service.getDepartmentName(departmentId);
		departmentRollup = service.getDepartmentRollup(departmentId);
		totals = service.getTotals(departmentRollup);
	}

	public List<DepartmentView> getDepartmentRollup() {
		return departmentRollup;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public DepartmentRollupTotals getTotals() {
		return totals;
	}
}
