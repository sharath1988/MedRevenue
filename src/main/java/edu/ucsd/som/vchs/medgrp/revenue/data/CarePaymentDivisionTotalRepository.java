/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.CarePaymentDivisionTotal;

/**
 * Repository DAO for Carepayment tab
 * 
 * @author somdev5
 *
 */
@Repository
public interface CarePaymentDivisionTotalRepository extends EntityRepository<CarePaymentDivisionTotal, Integer> {

}
