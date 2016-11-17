package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentView;
import edu.ucsd.som.vchs.medgrp.revenue.model.DepartmentViewGrouping;

@Repository
public interface DepartmentViewRepository extends EntityRepository<DepartmentView, DepartmentViewGrouping> {
	/**
	 * Returns a list of Department expense totals for a given department
	 * 
	 * @param departmentId
	 * @return
	 */
	public List<DepartmentView> findByDepartmentId(Integer departmentId);
}
