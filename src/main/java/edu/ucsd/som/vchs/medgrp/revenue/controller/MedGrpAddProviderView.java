package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployeeView;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;
import edu.ucsd.som.vchs.medgrp.revenue.service.ProviderService;

@ViewScoped
@ManagedBean(name="addProviderView")
public class MedGrpAddProviderView {
	@Inject
	private ProviderService providerService;
	
	@Inject
	private FacesContext facesContext;
	
	@ManagedProperty(value="#{medGrpRevenueWorksheetView}")
	private MedGrpRevenueWorksheetView revenueWorksheetView;
	
	private AllEmployeeView provider;
	private BigDecimal carePaymentRate;
	
	private List<AllEmployeeView> providerAutoCompleteResults = null;
	private SiteOfService siteOfService = SiteOfService.BOTH;
	private Integer divisionId;
	private static final SiteOfService[] siteOfServiceValues = SiteOfService.values();
	
	@ManagedProperty(value="#{divisionInfoBean}")
	private DivisionInfoBean divisionInfoBean;
	
	public List<AllEmployeeView> autoComplete(String query) {
		providerAutoCompleteResults = providerService.getProvidersByQueryDivisionAndSos(query,divisionId,siteOfService);
		return providerAutoCompleteResults;
	}
	
	public List<AllEmployeeView> getProviderAutoCompleteResults() {
		return providerAutoCompleteResults;
	}
	
	public void add() {
		if (siteOfService.equals(SiteOfService.BOTH) && divisionInfoBean.getDivisionInfo().getIsPsychiatry()) {
			providerService.addProviderToWorksheet(provider, divisionId, SiteOfService.INPATIENT, carePaymentRate);
			providerService.addProviderToWorksheet(provider, divisionId, SiteOfService.OUTPATIENT, carePaymentRate);
		} else {
			providerService.addProviderToWorksheet(provider, divisionId, siteOfService, carePaymentRate);
		}
		revenueWorksheetView.refreshProviderTotals();
    	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Provider added successfully."));
    	provider = null;
    	siteOfService = SiteOfService.BOTH;
    	carePaymentRate = BigDecimal.ZERO;
	}
	
	public SiteOfService[] getSiteOfServiceValues() {
		return siteOfServiceValues;
	}

	/**
	 * @return the provider
	 */
	public AllEmployeeView getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(AllEmployeeView provider) {
		this.provider = provider;
	}

	/**
	 * @return the siteOfService
	 */
	public SiteOfService getSiteOfService() {
		return siteOfService;
	}

	/**
	 * @param siteOfService the siteOfService to set
	 */
	public void setSiteOfService(SiteOfService siteOfService) {
		this.siteOfService = siteOfService;
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
	 * @return the revenueWorksheetView
	 */
	public MedGrpRevenueWorksheetView getRevenueWorksheetView() {
		return revenueWorksheetView;
	}

	/**
	 * @param revenueWorksheetView the revenueWorksheetView to set
	 */
	public void setRevenueWorksheetView(MedGrpRevenueWorksheetView revenueWorksheetView) {
		this.revenueWorksheetView = revenueWorksheetView;
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
	 * @return the carePaymentRate
	 */
	public BigDecimal getCarePaymentRate() {
		return carePaymentRate;
	}

	/**
	 * @param carePaymentRate the carePaymentRate to set
	 */
	public void setCarePaymentRate(BigDecimal carePaymentRate) {
		this.carePaymentRate = carePaymentRate;
	}
}
