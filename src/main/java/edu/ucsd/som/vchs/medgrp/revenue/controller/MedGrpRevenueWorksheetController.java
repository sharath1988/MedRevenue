/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.TabChangeEvent;

import edu.ucsd.som.vchs.medgrp.revenue.auth.AuthController;
import edu.ucsd.som.vchs.medgrp.revenue.auth.SecurityAccessException;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;

/**
 * Controller for all the views on the Revenue worksheet page
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@ViewScoped
@ManagedBean(name="medGrpRevenueWorksheetController")
public class MedGrpRevenueWorksheetController implements Serializable {

	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = -7805762385346717581L;
	
	
	@Inject
	private Log log;
	
	@Inject
	private AuthController authController;
	
	@ManagedProperty(value="#{paymentInformationView}")
	private MedGrpRevenuePaymentInformationView paymentInformationView;
	
	@ManagedProperty(value="#{medGrpRevenueWorksheetView}")
	private MedGrpRevenueWorksheetView medGrpRevenueWorksheetView;
	
	@ManagedProperty(value="#{revenueAdjustmentsView}")
	private RevenueAdjustmentsView revenueAdjustmentsView;
	
	@ManagedProperty(value="#{carePaymentsView}")
	private CarePaymentsView carePaymentsView;
	
	private Integer divisionId;
	
	private Boolean userPermitted;
	
	private SiteOfService sos = SiteOfService.BOTH;
	
	/**
	 * Default constructor
	 */
	public MedGrpRevenueWorksheetController() {
		super();
	}
		
	/**
	 * Loads the provider totals and the payment information section
	 */
	public void loadWorksheet() {
		if (getUserPermitted()) {
			medGrpRevenueWorksheetView.setDivisionId(getDivisionId());
			paymentInformationView.setDivisionId(getDivisionId());
			
			medGrpRevenueWorksheetView.loadProviderTotals();
			paymentInformationView.loadPaymentInformation(getSos());
		} else {
			throw new SecurityAccessException();
		}
	}

	/**
	 * Controller to update payment information section and edited provider row
	 * 
	 * @param e
	 */
	public void editWrvusOnBlur(AjaxBehaviorEvent e) {
		if (getUserPermitted()) {
			if (e.getSource() instanceof InputText) {
				InputText input = (InputText) e.getSource();
				
				// Get value
				//BigDecimal wrvus = (BigDecimal) input.getValue();
				RevenueWorksheetView editedWorksheet = (RevenueWorksheetView) input.getAttributes().get("editedWorksheet");
				log.info("updating wrvus for worksheet id= " + editedWorksheet + " to " + editedWorksheet.getProjWrvus());
				
				medGrpRevenueWorksheetView.handleEditProjWrvus(editedWorksheet);
				paymentInformationView.refreshPaymentInformation(getSos());
				revenueAdjustmentsView.loadData();
				carePaymentsView.loadData();
			} else {
				throw new UnsupportedOperationException("cellEditOnBlur only supports sources of type org.primefaces.component.inputtext.InputText");
			}
		} else {
			throw new SecurityAccessException();
		}
	}
	
	/**
	 * Controller to update worksheet table when payment information section is edited
	 * 
	 * @param e
	 */
	public void editCollectionRatePercOnBlur(AjaxBehaviorEvent e) {
		if (getUserPermitted()) {
			paymentInformationView.paymentInfoCellEditOnBlur();
			
			// Update provider totals and payment information grid
			medGrpRevenueWorksheetView.refreshProviderTotals();
			paymentInformationView.refreshPaymentInformation(getSos());	
			revenueAdjustmentsView.loadData();
			carePaymentsView.loadData();
		} else {
			throw new SecurityAccessException();
		}
	}
	
	/**
	 * Apply percent change across the division and update provider totals and payment information
	 */
	public void applyPercentChangeOnClick() {
		if (getUserPermitted()) {
			medGrpRevenueWorksheetView.applyPercentChangeAction();
			
			// Update provider totals and payment information grid
			medGrpRevenueWorksheetView.refreshProviderTotals();
			paymentInformationView.refreshPaymentInformation(getSos());
			revenueAdjustmentsView.loadData();
			carePaymentsView.loadData();
		} else {
			throw new SecurityAccessException();
		}
	}
	
	/**
	 * Resets to prior year wrvus
	 */
	public void resetPercentChangeOnClick() {
		if (getUserPermitted()) {
			medGrpRevenueWorksheetView.resetPercentageAction();
			
			// Update provider totals and payment information grid
			medGrpRevenueWorksheetView.refreshProviderTotals();
			paymentInformationView.refreshPaymentInformation(getSos());			
			revenueAdjustmentsView.loadData();
			carePaymentsView.loadData();
		} else {
			throw new SecurityAccessException();
		}
	}
	
	/**
	 * Filters the list for psychiatry
	 * 
	 * @param sos
	 */
	public void handleSosChange() {
		if (getUserPermitted()) {
			medGrpRevenueWorksheetView.handleSosChange(getSos());
			paymentInformationView.handleSosChange(getSos());	
			revenueAdjustmentsView.loadData();
			carePaymentsView.loadData();
		} else {
			throw new SecurityAccessException();
		}
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
	 * @return the medGrpRevenueWorksheetView
	 */
	public MedGrpRevenueWorksheetView getMedGrpRevenueWorksheetView() {
		return medGrpRevenueWorksheetView;
	}

	/**
	 * @param medGrpRevenueWorksheetView the medGrpRevenueWorksheetView to set
	 */
	public void setMedGrpRevenueWorksheetView(
			MedGrpRevenueWorksheetView medGrpRevenueWorksheetView) {
		this.medGrpRevenueWorksheetView = medGrpRevenueWorksheetView;
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
	 * @return the sos
	 */
	public SiteOfService getSos() {
		return sos;
	}

	/**
	 * @param sos the sos to set
	 */
	public void setSos(SiteOfService sos) {
		this.sos = sos;
	}

	/**
	 * @return the userPermitted
	 */
	public Boolean getUserPermitted() {
		if (userPermitted == null) {
			userPermitted = authController.hasPermissionForDivision(getDivisionId()); 
		}
		return userPermitted;
	}

	/**
	 * @return the revenueAdjustmentsView
	 */
	public RevenueAdjustmentsView getRevenueAdjustmentsView() {
		return revenueAdjustmentsView;
	}

	/**
	 * @param revenueAdjustmentsView the revenueAdjustmentsView to set
	 */
	public void setRevenueAdjustmentsView(
			RevenueAdjustmentsView revenueAdjustmentsView) {
		this.revenueAdjustmentsView = revenueAdjustmentsView;
	}

	/**
	 * @return the carePaymentsView
	 */
	public CarePaymentsView getCarePaymentsView() {
		return carePaymentsView;
	}

	/**
	 * @param carePaymentsView the carePaymentsView to set
	 */
	public void setCarePaymentsView(CarePaymentsView carePaymentsView) {
		this.carePaymentsView = carePaymentsView;
	}
}