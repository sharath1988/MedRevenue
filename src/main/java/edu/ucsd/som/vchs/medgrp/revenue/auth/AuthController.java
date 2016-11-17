/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.auth;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.apache.commons.logging.Log;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.LoggedInUcsdId;

/**
 * Authorization Controller for access control to various resources
 *  
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@SessionScoped
@ManagedBean(name="authController")
public class AuthController {
	
	@Inject
	private Log log;
	
	@Inject @LoggedInUcsdId
	private Integer loggedInUcsdId;
	
	@Inject
	private EmployeeIndexRepository employeeIndexRepository;
	
	@Inject
	private EmployeeDivisionRepository medGrpRevenueDivRepository;
	
	/**
	 * Determines if the logged in user has access to a specific index
	 * 
	 * @param index
	 * @return
	 */
	public boolean hasPermissionForIndex(String index) {
		log.debug("Checking permission for " + index + " for employee " + loggedInUcsdId);
		EmployeeIndex idx = employeeIndexRepository.findOptionalByIndexAndEmployeeUcsdId(index, loggedInUcsdId);
		return (idx != null);
	}
	
	/**
	 * Determines if the logged in user has access to a specific division
	 * 
	 * @param divisionId
	 * @return
	 */
	public boolean hasPermissionForDivision(Integer divisionId) {
		log.debug("Checking permission for division " + divisionId + " for employee " + loggedInUcsdId);
		EmployeeDivision div = medGrpRevenueDivRepository.findOptionalByDivisionIdAndEmployeeUcsdId(divisionId, loggedInUcsdId);
		return (div != null);
	}
	
	/**
	 * Determines if the logged in user has access to a specific department
	 * 
	 * @param departmentId
	 * @return
	 */
	public boolean hasPermissionForDepartment(Integer departmentId) {
		log.debug("Checking permission for department " + departmentId + " for employee " + loggedInUcsdId);
		List<EmployeeDivision> divList = medGrpRevenueDivRepository.findByDepartmentIdAndEmployeeUcsdId(departmentId, loggedInUcsdId);
		return (divList.size() > 0);		
	}
}