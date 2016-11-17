package edu.ucsd.som.vchs.medgrp.revenue.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.model.OtherIncome;
import edu.ucsd.som.vchs.medgrp.revenue.service.OtherIncomeService;

@ViewScoped
@ManagedBean(name="otherIncomeView")
public class OtherIncomeView {
	Integer divisionId;
	
	@Inject
	OtherIncomeService service;
	
	@Inject
	FacesContext facesContext;
	
	OtherIncome otherIncome;
	
	Integer otherIncomeTotal;
	
	public void load() {
		otherIncome = service.find(divisionId);
		refreshDynamicFormValues();
	}
	
	public void update() {
		service.save(otherIncome);
		refreshDynamicFormValues();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Other Income Modification", "Other income updated successfully"));
	}
	
	public void refreshDynamicFormValues() {
		otherIncomeTotal = service.getTotal(divisionId);
	}

	public Integer getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	public OtherIncome getOtherIncome() {
		return otherIncome;
	}

	public void setOtherIncome(OtherIncome otherIncome) {
		this.otherIncome = otherIncome;
	}

	public Integer getOtherIncomeTotal() {
		return otherIncomeTotal;
	}

	public void setOtherIncomeTotal(Integer otherIncomeTotal) {
		this.otherIncomeTotal = otherIncomeTotal;
	}
}