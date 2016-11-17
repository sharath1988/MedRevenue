/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformation;

/**
 * Repository for RevenuePaymentInformation
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenuePaymentInformationRepository extends EntityRepository<RevenuePaymentInformation, Integer> {

	RevenuePaymentInformation findOptionalByDivisionId(Integer divisionId);
}
