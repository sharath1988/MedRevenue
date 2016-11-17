/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueAdjChargesTotalByDiv;

/**
 * Repository DAO for adjustment tab - totals
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenueAdjChargesTotalsByDivRepository extends EntityRepository<RevenueAdjChargesTotalByDiv, Integer> {
}
