/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.CarePaymentDetail;
import edu.ucsd.som.vchs.medgrp.revenue.model.CarePaymentDetailPK;

/**
 * Repository DAO for Carepayment tab
 * 
 * @author somdev5
 *
 */
@Repository
public interface CarePaymentDetailRepository extends EntityRepository<CarePaymentDetail, CarePaymentDetailPK> {

	List<CarePaymentDetail> findBySomDivisionId(Integer somDivisionId);
}
