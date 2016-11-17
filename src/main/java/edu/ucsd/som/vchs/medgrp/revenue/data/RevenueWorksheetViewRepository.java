/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;

/**
 * Repository class for displaying the RevenueWorksheetView
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenueWorksheetViewRepository extends EntityRepository<RevenueWorksheetView, Integer> {

	/**
	 * Returns a list of Revenue worksheets for a given division
	 * 
	 * @param divisionId
	 * @param employeeUcsdId
	 * @return
	 */
	public List<RevenueWorksheetView> findOptionalByDivisionId(Integer divisionId);
	
	/**
	 * Returns a list of revenue worksheets for a given division id and site of service.
	 * Currently, this method is primarily (only) used for Psychiatry (divisionId=22)
	 * 
	 * @param divisionId
	 * @param sos
	 * @return
	 */
	public List<RevenueWorksheetView> findByDivisionIdAndSos(Integer divisionId, String sos);
	
	@Query("select w.employeeUcsdId from RevenueWorksheetView w where w.divisionId = ?1 group by w.employeeUcsdId")
	public List<Integer> findEmployeeIdsByDivisionId(Integer divisionId);
	
	/**
	 * Returns a single worksheet by employee id and division id
	 * @param divisionId
	 * @param employeeUcsdId
	 * @return
	 */
	public RevenueWorksheetView findOptionalByDivisionIdAndEmployeeUcsdId(Integer divisionId, Integer employeeUcsdId);
}
