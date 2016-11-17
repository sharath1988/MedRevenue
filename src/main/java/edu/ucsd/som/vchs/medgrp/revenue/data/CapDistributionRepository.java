/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.CapDistribution;

/**
 * Entity repository for Cap distribution on the provider worksheet.  
 * This field is used to calculate total expected revenue
 * 
 * @author somdev5
 *
 */
@Repository
public interface CapDistributionRepository extends EntityRepository<CapDistribution, Integer> {

	CapDistribution findOptionalByDivisionId(Integer divisionId);
}