package edu.ucsd.som.vchs.medgrp.revenue.data;

import javax.persistence.StoredProcedureQuery;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.FutureEmployee;
import edu.ucsd.som.vchs.medgrp.revenue.model.faculty.FacultyEmployeeInfo;

@Repository
public abstract class FutureEmployeeRepository extends AbstractEntityRepository<FutureEmployee, Integer> {
	public Boolean addFacultyTbnInfo(Integer allEmployeeId, FacultyEmployeeInfo facultyEmployeeInfo) {
		StoredProcedureQuery storedProcQuery = entityManager().createNamedStoredProcedureQuery(FutureEmployee.ADD_FACULTY_TBN_INFO);
		storedProcQuery.setParameter("p_all_employee_id", allEmployeeId);
		storedProcQuery.setParameter("p_degree", facultyEmployeeInfo.getDegree().toString());
		storedProcQuery.setParameter("p_compPlan", facultyEmployeeInfo.getCompPlan().toString());
		storedProcQuery.setParameter("p_rank", facultyEmployeeInfo.getRank().toString());
		storedProcQuery.setParameter("p_step", facultyEmployeeInfo.getStep().toString());
		storedProcQuery.setParameter("p_offScale", facultyEmployeeInfo.getOffScale());
		storedProcQuery.setParameter("p_appointment", facultyEmployeeInfo.getAppointment());
		return storedProcQuery.execute();
	}
}