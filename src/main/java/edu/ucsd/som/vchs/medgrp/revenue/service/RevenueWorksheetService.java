/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.logging.Log;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheet;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;
import edu.ucsd.som.vchs.medgrp.revenue.util.BigDecimalUtil;

/**
 * Service class for revenue worksheets
 * 
 * @author somdev5
 * @see RevenueWorksheetRepository
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@Named
public class RevenueWorksheetService {
	
	@Inject
	private Log log;
	
	@Inject
	private RevenueWorksheetRepository repo;
	
	@Inject
	private EntityManager em;	
	
	@Inject
	private RevenueWorksheetViewRepository viewRepo;
		
	/**
	 * Default constructor
	 */
	public RevenueWorksheetService() {
		super();
	}
	
	/**
	 * Calls a stored procedure to boost performance
	 * 
	 * @param divisionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RevenueWorksheetView> getRevenueWorksheetByDivision(Integer divisionId) {
		
		StoredProcedureQuery sproc = em.createNamedStoredProcedureQuery(RevenueWorksheetView.GET_REVENUE_WORKSHEET_BY_DIV);
		sproc.setParameter("pDivisionId", divisionId);
		return sproc.getResultList();
	}
	
	/**
	 * Calls a stored procedure to boost performance
	 * 
	 * @param divisionId
	 * @param sos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RevenueWorksheetView> getRevenueWorksheetByDivisionAndSos(Integer divisionId, String sos) {
		
		StoredProcedureQuery sproc = em.createNamedStoredProcedureQuery(RevenueWorksheetView.GET_REVENUE_WORKSHEET_BY_DIV_AND_SOS);
		sproc.setParameter("pDivisionId", divisionId);
		sproc.setParameter("pSos", sos);
		return sproc.getResultList();
	}	
	
	/*
	 * Resets a row to prior year's wrvus.  If prior year's wRVUs are less than
	 * or equal to zero then set projected wrvus to Zero.
	 */
	private void resetEntityRowToPriorYear(RevenueWorksheetView worksheet) {
		Boolean rollingOnly = worksheet.getRollingOnly();
		if (!rollingOnly) {
			BigDecimal priorWrvus = worksheet.getPriorYearWrvus();
			BigDecimal projWrvus = BigDecimal.ZERO;
			
			if (priorWrvus != null && BigDecimalUtil.isGreaterThan(priorWrvus, BigDecimal.ZERO)) {
				projWrvus = priorWrvus;
			}
			
			// Get worksheet object and update
			RevenueWorksheet entity = repo.findBy(worksheet.getId());
			entity.setProjWrvus(projWrvus);
			repo.save(entity);
			log.debug(worksheet.getId() + " current wRVUs: " + worksheet.getProjWrvus() + " reset to " + projWrvus);			
		}
	}		

	/**
	 * Persists worksheet wrvus
	 */
	@Transactional
	public void saveWorksheetWrvus(RevenueWorksheetView worksheetView, BigDecimal projWrvus) {
		Integer revenueWorksheetId = worksheetView.getId();
		RevenueWorksheet worksheet = repo.findBy(revenueWorksheetId);
		worksheet.setProjWrvus(projWrvus);
		repo.save(worksheet);
		log.info("Saved wRVUs for worksheet id=" + revenueWorksheetId);
	}
	
	/**
	 * Resets all charges, collections, & RVUs to the prior year's values
	 * @param divisionId
	 */

	public void resetAllByDivisionId(Integer divisionId) {
		repo.resetPercentChange(divisionId);
		em.clear();
		log.info("Revenue worksheet for division " + divisionId + " reset to prior year's values");
	}
		
	/**
	 * Applies a percent change value to all worksheets in the specified division
	 * 
	 * @param divisionId
	 * @param percentChange
	 */
	@Transactional
	public void applyPercentChangeToDivision(Integer divisionId, final BigDecimal percentChange) {
		BigDecimal percentProduct = BigDecimalUtil.toPercentProduct(percentChange);
		repo.applyPercentChange(divisionId, percentProduct);
		em.clear();
		log.info("Percent change " + percentChange + " applied to divisionId=" + divisionId);
	}
	
	public RevenueWorksheetView getWorksheetRow(Integer worksheetRowId) {
		return viewRepo.findBy(worksheetRowId);
	}
	
	public Integer deleteWorksheetRow(Integer id, Integer divisionId) {
		RevenueWorksheetView view = getWorksheetRow(id);
		Integer rowsDeleted = 0;
		if (view.getRemovable() && view.getDivisionId().equals(divisionId)) {
			rowsDeleted = repo.deleteRemovableRow(id);
		}
		return rowsDeleted;
	}
}