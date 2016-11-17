/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.logging.Log;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueAdjChargesByPayer;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueAdjChargesTotalByDiv;
import edu.ucsd.som.vchs.medgrp.revenue.service.RevenueAdjustmentsService;
import edu.ucsd.som.vchs.medgrp.revenue.service.TabRenderService;


/**
 * View Managed bean for populating the Adjustments tab
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@ViewScoped
@ManagedBean(name="revenueAdjustmentsView")
public class RevenueAdjustmentsView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 691523338514864152L;

	@Inject
	private Log log;
	
	@Inject
	private RevenueAdjustmentsService revenueAdjService;
	
	@Inject
	private TabRenderService tabRenderService;
			
	private Integer divisionId;
	
	private List<RevenueAdjChargesByPayer> adjByPayerList;
	
	private RevenueAdjChargesTotalByDiv adjTotals;

	
	/**
	 * Default constructor
	 */
	public RevenueAdjustmentsView() {
		super();
	}
	
	/**
	 * loads from revenue split.  Also computes totals for each category
	 */
	public void loadData() {
		if (tabRenderService.renderAdjustmentsTabForDivision(getDivisionId())) {
			log.info("loading adjustments for divisionId=" + divisionId);		
			this.adjByPayerList = revenueAdjService.getRevenueAdjustmentsByPayer(divisionId);
			refreshTotals();			
		}
	}

	/**
	 * Refreshes totals when CAP distribution input field is updated on the revenue worksheet
	 */
	public void refreshTotals() {
		this.adjTotals = revenueAdjService.getRevenueAdjustmentTotalsByDivision(divisionId);
	}
	
	/**
	 * @return the divisionId
	 */
	public Integer getDivisionId() {
		return divisionId;
	}

	/**
	 * @param divisionId the divisionId to set
	 */
	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	/**
	 * @return the adjByPayerList
	 */
	public List<RevenueAdjChargesByPayer> getAdjByPayerList() {
		return adjByPayerList;
	}

	/**
	 * @param adjByPayerList the adjByPayerList to set
	 */
	public void setAdjByPayerList(List<RevenueAdjChargesByPayer> adjByPayerList) {
		this.adjByPayerList = adjByPayerList;
	}

	/**
	 * @return the adjTotals
	 */
	public RevenueAdjChargesTotalByDiv getAdjTotals() {
		return adjTotals;
	}

	/**
	 * @param adjTotals the adjTotals to set
	 */
	public void setAdjTotals(RevenueAdjChargesTotalByDiv adjTotals) {
		this.adjTotals = adjTotals;
	}
}