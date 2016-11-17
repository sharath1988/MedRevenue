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

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueDivisionSplit;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueProviderSplit;
import edu.ucsd.som.vchs.medgrp.revenue.service.RevenueProviderSplitService;
import edu.ucsd.som.vchs.medgrp.revenue.webutil.AutoCompleteUtil;


/**
 * View Managed bean for populating the Split percentage tab
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@ViewScoped
@ManagedBean(name="revenueProviderSplitView")
public class RevenueProviderSplitView implements Serializable {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2044773938418350947L;
	
	@Inject
	private Log log;
		
	@Inject
	private RevenueProviderSplitService revenueProviderSplitService;
	
	@Inject
	private AutoCompleteUtil autoCompleteUtil;
	
	private Integer divisionId;
	
	private List<RevenueProviderSplit> revenueProviderSplits;
	
	private List<RevenueProviderSplit> filteredRevenueProviderSplits;
	
	private RevenueDivisionSplit revenueDivisionSplit;
	
	/**
	 * Default constructor
	 */
	public RevenueProviderSplitView() {
		super();
	}
	
	/**
	 * loads from revenue split.  Also computes totals for each category
	 */
	public void loadData() {
		log.info("loading provider splits for divisionId=" + divisionId);
		this.revenueProviderSplits = revenueProviderSplitService.findByDivisionId(divisionId);
		this.revenueDivisionSplit = revenueProviderSplitService.getDivisionSplits(divisionId);
	}
	
	/**
	 * Autocomplete for the provider filter box
	 * 
	 * @param query
	 * @return
	 */
	public List<String> completeProviderName(String query) {
		return autoCompleteUtil.selectDistinctPropertyFromList(revenueProviderSplits, "providerName", query);
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
	 * @return the revenueProviderSplits
	 */
	public List<RevenueProviderSplit> getRevenueProviderSplits() {
		return revenueProviderSplits;
	}

	/**
	 * @param revenueProviderSplits the revenueProviderSplits to set
	 */
	public void setRevenueProviderSplits(
			List<RevenueProviderSplit> revenueProviderSplits) {
		this.revenueProviderSplits = revenueProviderSplits;
	}

	/**
	 * @return the revenueDivisionSplit
	 */
	public RevenueDivisionSplit getRevenueDivisionSplit() {
		return revenueDivisionSplit;
	}

	/**
	 * @param revenueDivisionSplit the revenueDivisionSplit to set
	 */
	public void setRevenueDivisionSplit(RevenueDivisionSplit revenueDivisionSplit) {
		this.revenueDivisionSplit = revenueDivisionSplit;
	}

	/**
	 * @return the filteredRevenueProviderSplits
	 */
	public List<RevenueProviderSplit> getFilteredRevenueProviderSplits() {
		return filteredRevenueProviderSplits;
	}

	/**
	 * @param filteredRevenueProviderSplits the filteredRevenueProviderSplits to set
	 */
	public void setFilteredRevenueProviderSplits(
			List<RevenueProviderSplit> filteredRevenueProviderSplits) {
		this.filteredRevenueProviderSplits = filteredRevenueProviderSplits;
	}
}
