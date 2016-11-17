/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueProviderSplit;

/**
 * Entity repository for RevenueProvider split.  This is for populating the Split tab 
 * on the revenue worksheet.
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenueProviderSplitRepository extends EntityRepository<RevenueProviderSplit, Integer> {

	
	/**
	 * Returns a list of RevenueProvider split objects by division id
	 * 
	 * @param divisionId
	 * @return
	 */
	List<RevenueProviderSplit> findByDivisionId(Integer divisionId);
}
