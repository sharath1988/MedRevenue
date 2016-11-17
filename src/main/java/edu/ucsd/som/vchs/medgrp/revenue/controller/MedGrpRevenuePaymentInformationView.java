/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.apache.commons.logging.Log;

import edu.ucsd.som.vchs.medgrp.revenue.model.CapDistribution;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueInpatientPercentageBase;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformationViewBase;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;
import edu.ucsd.som.vchs.medgrp.revenue.service.PaymentInformationService;

/**
 * View controller for the Payment information form
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@ViewScoped
@ManagedBean(name="paymentInformationView")
public class MedGrpRevenuePaymentInformationView {
		
	@Inject
	private Log log;
	
	@Inject
	private PaymentInformationService paymentInformationService;
		
	@Inject
	private FacesContext facesContext;

	private RevenuePaymentInformationViewBase paymentInformation;
	
	private RevenueInpatientPercentageBase inpatientPercentage;
	
	private CapDistribution capDistribution;
	
	private Integer divisionId;
	 
	@ManagedProperty(value="#{divisionInfoBean}")
	private DivisionInfoBean divisionInfoBean;
	
	@ManagedProperty(value="#{revenueAdjustmentsView}")
	private RevenueAdjustmentsView revenueAdjustmentsView;
		
	/**
	 * Default constructor
	 */
	public MedGrpRevenuePaymentInformationView() {
		super();
	}
	
	/**
	 * Handle editing the oncell edit on blur
	 * 
	 * @param e
	 */
	public void paymentInfoCellEditOnBlur() {
		log.info("saving payment information");
		paymentInformationService.savePaymentInformation(paymentInformation);
		facesContext.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Payment information updated successfully"));
	}
	
	/**
	 * Handle editing the projected Inpatient percentage cell
	 * @param e
	 */
	public void projInpatientPercCellOnBlur(AjaxBehaviorEvent e) {
		log.info("saving projected inpatient percentage for division " + divisionId);
		paymentInformationService.saveProjInpatientPercentage(getInpatientPercentage());
		facesContext.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Inpatient % saved successfully"));
	}
	
	/**
	 * Handle editing the projeted cap distribution cell
	 * @param e
	 */
	public void capDistributionCellOnBlur(AjaxBehaviorEvent e) {
		log.info("saving CAP distribution amount for division " + divisionId);
		paymentInformationService.saveCapDistribution(getCapDistribution());
		revenueAdjustmentsView.refreshTotals();
		facesContext.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Cap distribution saved successfully"));		
	}
	
	/**
	 * Only pull payment information for a particular sos
	 * Currently this is only used by Pscyhiatry division
	 * 
	 * @param sos
	 */
	public void handleSosChange(SiteOfService sos) {
		log.info("loading " + sos + " payment information for divisionId=" + getDivisionId());
		paymentInformation = paymentInformationService.getPaymentInformationBySos(divisionId, sos.toString());		
	}
	
	/**
	 * preView render method
	 */
	public void loadPaymentInformation(SiteOfService sos) {
		log.info("isPsych=" + isPsychiatry());
		if (paymentInformation == null && inpatientPercentage == null) {
			if (isPsychiatry()) {
				paymentInformation = paymentInformationService.getPaymentInformationBySos(divisionId, sos.toString());
				inpatientPercentage = paymentInformationService.getInpatientPercentageForPsych();
			} else {
				paymentInformation = paymentInformationService.getPaymentInformation(divisionId);
				inpatientPercentage = paymentInformationService.getInpatientPercentage(divisionId);
			}
		}
		
		if (capDistribution == null) {
			capDistribution = paymentInformationService.getCapDistributionByDivision(divisionId);	
		}
	}
	
	/**
	 * Refreshes the current payment information
	 */
	public void refreshPaymentInformation(SiteOfService sos) {
		if (isPsychiatry()) {
			paymentInformation = paymentInformationService.getPaymentInformationBySos(divisionId, sos.toString());
			inpatientPercentage = paymentInformationService.getInpatientPercentageForPsych();
		} else {
			paymentInformation = paymentInformationService.getPaymentInformation(divisionId);
			inpatientPercentage = paymentInformationService.getInpatientPercentage(divisionId);
		}
		capDistribution = paymentInformationService.getCapDistributionByDivision(divisionId);
	}
	
	/**
	 * @return the paymentInformation
	 */
	public RevenuePaymentInformationViewBase getPaymentInformation() {
		return paymentInformation;
	}

	/**
	 * @param paymentInformation the paymentInformation to set
	 */
	public void setPaymentInformation(
			RevenuePaymentInformationViewBase paymentInformation) {
		this.paymentInformation = paymentInformation;
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
	 * @return the inpatientPercentage
	 */
	public RevenueInpatientPercentageBase getInpatientPercentage() {
		return inpatientPercentage;
	}

	/**
	 * @param inpatientPercentage the inpatientPercentage to set
	 */
	public void setInpatientPercentage(
			RevenueInpatientPercentageBase inpatientPercentage) {
		this.inpatientPercentage = inpatientPercentage;
	}
	
	/**
	 * @return the divisionInfoBean
	 */
	public DivisionInfoBean getDivisionInfoBean() {
		return divisionInfoBean;
	}

	/**
	 * @param divisionInfoBean the divisionInfoBean to set
	 */
	public void setDivisionInfoBean(DivisionInfoBean divisionInfoBean) {
		this.divisionInfoBean = divisionInfoBean;
	}

	/**
	 * @return isPsychiatry
	 */
	public Boolean isPsychiatry() {
		return divisionInfoBean.getDivisionInfo().getIsPsychiatry();
	}

	/**
	 * @return the capDistribution
	 */
	public CapDistribution getCapDistribution() {
		return capDistribution;
	}

	/**
	 * @param capDistribution the capDistribution to set
	 */
	public void setCapDistribution(CapDistribution capDistribution) {
		this.capDistribution = capDistribution;
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
}
