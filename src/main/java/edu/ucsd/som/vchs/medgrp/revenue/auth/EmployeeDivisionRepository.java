/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.auth;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Repository class for reading the MedGrp revenue division list to display on the revenue homepage
 * 
 * @author somdev5
 *
 */
@Repository
public interface EmployeeDivisionRepository extends EntityRepository<EmployeeDivision, String> {

	/**
	 * Returns Division by division id
	 * 
	 * @param id
	 * @return
	 */
	public EmployeeDivision findOptionalById(String id);
		
	/**
	 * Returns all divisions in the list by user id
	 */
	public List<EmployeeDivision> findOptionalByEmployeeUcsdIdAndGroupArea(Integer employeeUcsdId, String groupArea);
	
	/**
	 * Returns a specific division by division and user id.  This is used for authorization mainly.
	 * 
	 * @param divisionId
	 * @param employeeUcsdId
	 * @return
	 */
	public EmployeeDivision findOptionalByDivisionIdAndEmployeeUcsdId(Integer divisionId, Integer employeeUcsdId);
	
	/**
	 * Returns a List of divisions by department and employee id.  This is user for authorization mainly.
	 * 
	 * @param departmentId
	 * @param employeeUcsdId
	 * @return
	 */
	public List<EmployeeDivision> findByDepartmentIdAndEmployeeUcsdId(Integer departmentId, Integer employeeUcsdId);
}