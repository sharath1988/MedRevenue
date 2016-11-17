package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.logging.Log;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.BudgetMetadataQualifer;
import edu.ucsd.som.vchs.medgrp.revenue.model.BudgetMetadata;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetMode;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;
import edu.ucsd.som.vchs.medgrp.revenue.service.ProviderService;
import edu.ucsd.som.vchs.medgrp.revenue.service.RevenueWorksheetService;

/**
 * Managed Bean for the Med Group Revenue Worksheet page
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@ViewScoped
@ManagedBean(name="medGrpRevenueWorksheetView")
public class MedGrpRevenueWorksheetView implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -916675661956615727L;
	
	private static final String REVENUE_PROJECTIONS_PREFIX = "revenueProjections_vs_";
	private static final String ROLLING_PROJECTIONS_PREFIX = REVENUE_PROJECTIONS_PREFIX + "Rolling12Months"; 
	
	
	@Inject
	private Log log;
		
	@Inject
	private RevenueWorksheetService worksheetService;
	
	@Inject @BudgetMetadataQualifer
	private BudgetMetadata budgetMetadata;
	
	@Inject
	private ProviderService providerService;
	
	private RevenueWorksheetMode revenueWorksheetMode;
	
	@Inject
	private FacesContext facesContext;
	
	@ManagedProperty(value="#{paymentInformationView}")
	private MedGrpRevenuePaymentInformationView paymentInformationView;

	private Integer divisionId;

	private List<RevenueWorksheetView> revenueWorksheetList;
	
	private List<RevenueWorksheetView> selectedProvidersToMerge;

	private BigDecimal appliedPercentChange;
	
	@PostConstruct
	private void postConstruct() {
		revenueWorksheetMode = RevenueWorksheetMode.LAST_CALENDAR_YEAR;
	}
	
	/**
	 * View Action to load the worksheet when the page is first accessed	
	 */	
	public void loadProviderTotals() {
		log.info("loading revenue worksheet for divisionId=" + getDivisionId());
		
		if (revenueWorksheetList == null) {			
			revenueWorksheetList = worksheetService.getRevenueWorksheetByDivision(getDivisionId());
		}
	}
	
	/**
	 * Refreshes provider totals
	 */
	public void refreshProviderTotals() {
		revenueWorksheetList = worksheetService.getRevenueWorksheetByDivision(getDivisionId());
	}
	
	/**
	 * Refreshed provider totals for SOS
	 * @param sos
	 */
	public void refreshProviderTotals(SiteOfService sos) {
		revenueWorksheetList = worksheetService.getRevenueWorksheetByDivisionAndSos(getDivisionId(), sos.toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "medGrpRevenueWorksheetView";
	}

	public RevenueWorksheetMode getRevenueWorksheetMode() {
		return revenueWorksheetMode;
	}
	
	public String getExcelFileNamePrefix() {
		if (revenueWorksheetMode.getLastCalendarYear()) {
			StringBuilder sb = new StringBuilder();
			sb.append(REVENUE_PROJECTIONS_PREFIX);
			sb.append(budgetMetadata.getBudgetPriorYear());
			return sb.toString();
		} else {
			return ROLLING_PROJECTIONS_PREFIX;
		}
	}

	public void setRevenueWorksheetMode(RevenueWorksheetMode revenueWorkSheetMode) {
		this.revenueWorksheetMode = revenueWorkSheetMode;
	}
	
	public void noRemovableProviders() {
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unable to Perform Selected Action", "No providers on this worksheet are removable."));
	}
	
	public void removeProvider() {
	    Integer id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
	    log.info("Remove row = " + id);
	    Integer numberRemoved = worksheetService.deleteWorksheetRow(id,divisionId);
	    if (numberRemoved == 0) {
	    	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unable to Perform Selected Action", "The provider you selected is not removable."));
	    } else {
	    	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Provider deleted successfully."));
	    	refreshProviderTotals();
	    }
	}
	
	public void mergeProviders() {
		try {
			providerService.mergeProviders(selectedProvidersToMerge);
			refreshProviderTotals();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Merge Providers", "Providers merged successfully"));
		} catch (Exception e) {
			log.error(e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Merge Providers", e.getMessage()));	
		}		
	}
		
	/**
	 * Filters the list for psychiatry
	 * 
	 * @param sos
	 */
	public void handleSosChange(SiteOfService sos) {
		log.info("loading " + sos + " revenue worksheet for divisionId=" + getDivisionId());
		if (sos.isBoth()) {
			refreshProviderTotals();
		} else {
			refreshProviderTotals(sos);		
		}
	}
		
		
	/**
	 * Handles when the projected wRVUs cell is edited
	 * 
	 * @param editedWorksheet
	 */
	public void handleEditProjWrvus(RevenueWorksheetView editedWorksheet) {

		worksheetService.saveWorksheetWrvus(editedWorksheet, editedWorksheet.getProjWrvus()); // TODO fix interface
		
		updateViewRow(editedWorksheet.getId());
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Projected wRVUs updated successfully"));		
	}
	
	/*
	 * Updates the view row on edit
	 */
	private void updateViewRow(Integer id) {
		RevenueWorksheetView updatedRow = worksheetService.getWorksheetRow(id);
		revenueWorksheetList.set(revenueWorksheetList.indexOf(updatedRow),updatedRow);
	}

	/**
	 * Apply a percent change across the board for all wRVUs. 
	 * This will overwrite any individual percentages that may have been set.
	 * 
	 * @param e
	 */
	public void applyPercentChangeAction() {
		log.info("Applying " + appliedPercentChange + " to division " + divisionId);
		worksheetService.applyPercentChangeToDivision(divisionId, appliedPercentChange);
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "% Change applied successfully"));
	}

	/**
	 * Resets all the current year's wRVUs to the prior fiscal year's value
	 * @param e
	 */
	public void resetPercentageAction() {
		log.info("Resetting all changes.");
		appliedPercentChange = null;
		worksheetService.resetAllByDivisionId(divisionId);
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Worksheet projections reset successfully"));
	}

	/**
	 * Getter for revenueWorksheetList
	 * @return
	 */
	public List<RevenueWorksheetView> getRevenueWorksheetList() {
		return revenueWorksheetList;
	}

	/**
	 * @param revenueWorksheetList the revenueWorksheetList to set
	 */
	public void setRevenueWorksheetList(List<RevenueWorksheetView> revenueWorksheetList) {
		this.revenueWorksheetList = revenueWorksheetList;
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
	 * @return the appliedPercentChange
	 */
	public BigDecimal getAppliedPercentChange() {
		return appliedPercentChange;
	}

	/**
	 * @param appliedPercentChange the appliedPercentChange to set
	 */
	public void setAppliedPercentChange(BigDecimal appliedPercentChange) {
		this.appliedPercentChange = appliedPercentChange;
	}

	/**
	 * @return the paymentInformationView
	 */
	public MedGrpRevenuePaymentInformationView getPaymentInformationView() {
		return paymentInformationView;
	}

	/**
	 * @param paymentInformationView the paymentInformationView to set
	 */
	public void setPaymentInformationView(
			MedGrpRevenuePaymentInformationView paymentInformationView) {
		this.paymentInformationView = paymentInformationView;
	}

	/**
	 * @return the selectedProvidersToMerge
	 */
	public List<RevenueWorksheetView> getSelectedProvidersToMerge() {
		if (selectedProvidersToMerge == null) {
			selectedProvidersToMerge = new ArrayList<RevenueWorksheetView>();
		}
		return selectedProvidersToMerge;
	}

	/**
	 * @param selectedProvidersToMerge the selectedProvidersToMerge to set
	 */
	public void setSelectedProvidersToMerge(
			List<RevenueWorksheetView> selectedProvidersToMerge) {
		this.selectedProvidersToMerge = selectedProvidersToMerge;
	}
}