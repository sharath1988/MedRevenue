/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author myebba
 *
 */
@Named
public class TabRenderService {

	@Inject
	private EntityManager em;
	
	public Boolean renderAdjustmentsTabForDivision(Integer divisionId) {
		// Display the adjustments tab if we have a prior year ZNB payer mix AND if the division is not outside billed
		Query q = em.createNativeQuery("select (CASE WHEN count(*) = 0 then 'false' else 'true' end) as render from budget.MV_ZNB_ADJUSTMENTS_PRIOR_FY where SOM_DIVISION_ID = :division_id and SOM_DIVISION_ID not in (select distinct mg.somDiv from mgfs.mgbs_outside_billers o join mgfs.mgBilling mg on o.billingAreaNumber = mg.number)");
		q.setParameter("division_id", divisionId);
		String result = (String) q.getSingleResult();
		return result.equals("true");
	}
	
	public Boolean renderCarePaymentsTabForDivision(Integer divisionId) {
		Query q = em.createNativeQuery("select (CASE WHEN count(*) = 0 then 'false' else 'true' end) as render from budget.MV_CARE_PAYMENT_WORKSHEET where SOM_DIVISION_ID = :division_id");
		q.setParameter("division_id", divisionId);
		String result = (String) q.getSingleResult();
		return result.equals("true");		
	}	

}
