/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformationViewPsych;

/**
 * Repository for loading payment information on the Revenue worksheet screen
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenuePaymentInformationViewPsychRepository extends EntityRepository<RevenuePaymentInformationViewPsych, Integer> {

	/**
	 * Returns payment information by division id
	 * 
	 * @param divisionId
	 * @return
	 */
	RevenuePaymentInformationViewPsych findOptionalByDivisionId(Integer divisionId);
	
	/**
	 * Returns a list of revenue worksheets for a given division id and site of service.
	 * Currently, this method is primarily (only) used for Psychiatry (divisionId=22)
	 * 
	 * @param divisionId
	 * @param sos
	 * @return
	 */
	RevenuePaymentInformationViewPsych findOptionalByDivisionIdAndSos(Integer divisionId, String sos);
}
