/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.auth;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Repository for retrieving Indexes an employee has access to
 * 
 * @author somdev5
 *
 */
@Repository
public interface EmployeeIndexRepository extends EntityRepository<EmployeeIndex, Integer> { 
	
	/**
	 * Returns an index by the index and employee ucsd id
	 *  
	 * @param index
	 * @param employeeUcsdId
	 * @return
	 */
	public EmployeeIndex findOptionalByIndexAndEmployeeUcsdId(String index, Integer employeeUcsdId);
	
	/**
	 * Return a collection of indexes for an employee ucsd id
	 * 
	 * @param employeeUcsdId
	 * @return
	 */
	public List<EmployeeIndex> findByEmployeeUcsdId(Integer employeeUcsdId);
}
