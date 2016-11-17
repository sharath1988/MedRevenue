/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformationView;

/**
 * Repository for loading payment information on the Revenue worksheet screen
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenuePaymentInformationViewRepository extends EntityRepository<RevenuePaymentInformationView, Integer> {

	/**
	 * Returns payment information by division id
	 * 
	 * @param divisionId
	 * @return
	 */
	RevenuePaymentInformationView findOptionalByDivisionId(Integer divisionId);
}
