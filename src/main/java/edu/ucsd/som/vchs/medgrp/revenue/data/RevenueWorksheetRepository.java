/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.math.BigDecimal;
import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Modifying;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheet;

/**
 * Entity Repository for RevenueWorksheet
 * @author somdev5
 *
 */
@Repository
public interface RevenueWorksheetRepository extends EntityRepository<RevenueWorksheet, Integer> {

	/**
	 * Return null if there is no worksheet for the given id
	 * 
	 * @param id
	 * @return
	 */
	public RevenueWorksheet findOptionalById(Integer id);
	
	/**
	 * Returns a list of revenue worksheet objects by division id
	 * @param divisionId
	 * @return
	 */
	public List<RevenueWorksheet> findByDivisionId(Integer divisionId);
	
	/**
	 * Apply a percent change across a division
	 * 
	 * @param divisionId
	 * @param percentChangeProduct
	 * @return
	 */
	@Modifying
	@Query(named=RevenueWorksheet.APPLY_PERCENT_CHANGE)
	public int applyPercentChange(Integer divisionId, BigDecimal percentChangeProduct);
	
	/**
	 * Resets percent change to zero
	 * 
	 * @param divisionId
	 * @return
	 */
	@Modifying
	@Query(named=RevenueWorksheet.RESET_PERCENT_CHANGE)
	public int resetPercentChange(Integer divisionId);
	
	/**
	 * Removes a removable row from the worksheet
	 * 
	 * @param id
	 * @return
	 */
	@Query("delete from RevenueWorksheet w where w.id = ?1")
	@Modifying
	public Integer deleteRemovableRow(Integer id);
}
