/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueAdjChargesByPayer;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueAdjChargesTotalByDiv;

/**
 * @author myebba
 *
 */
@Named
public class RevenueAdjustmentsService {

	@Inject
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<RevenueAdjChargesByPayer> getRevenueAdjustmentsByPayer(Integer divisionId) {
		StoredProcedureQuery sproc = em.createNamedStoredProcedureQuery(RevenueAdjChargesByPayer.GET_REVENUE_ADJ_CHARGES_BY_PL_PROC);
		sproc.setParameter("pDivisionId", divisionId);
		return sproc.getResultList();
	}
	
	public RevenueAdjChargesTotalByDiv getRevenueAdjustmentTotalsByDivision(Integer divisionId) {
		StoredProcedureQuery sproc = em.createNamedStoredProcedureQuery(RevenueAdjChargesTotalByDiv.GET_TOTALS_BY_DIV_SPROC);
		sproc.setParameter("pDivisionId", divisionId);
		return (RevenueAdjChargesTotalByDiv) sproc.getSingleResult();
	}
}
