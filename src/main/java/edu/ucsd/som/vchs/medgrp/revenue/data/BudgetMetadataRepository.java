/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.BudgetMetadata;

/**
 * Repository for Budget metadata
 * 
 * @author somdev5
 *
 */
@Repository
public interface BudgetMetadataRepository extends EntityRepository<BudgetMetadata, Integer> {

	/**
	 * Returns active budget metadata for the current budget year
	 * @param activeFlag
	 * @return
	 */
	@Query(named=BudgetMetadata.IS_ACTIVE)
	public BudgetMetadata getActive();
}
