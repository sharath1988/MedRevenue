/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueAdjChargesByPayer;

/**
 * Repository DAO for adjustment tab - payer breakout
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenueAdjChargesByPayerRepository extends EntityRepository<RevenueAdjChargesByPayer, String> {

	public List<RevenueAdjChargesByPayer> findByDivisionId(Integer divisionId);
}
