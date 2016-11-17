/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Modifying;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueProviderMerge;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueProviderMergePK;

/**
 * @author somdev5
 *
 */
@Repository
public interface RevenueProviderMergeRepository extends EntityRepository<RevenueProviderMerge, RevenueProviderMergePK> {

	@Modifying
	@Query(isNative=true, value="call budget.medgrpRevenueMergeEINs()")
	public void callMergeProviderProc();
}