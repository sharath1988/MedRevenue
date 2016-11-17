package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.LoggedInUcsdId;
import edu.ucsd.som.vchs.medgrp.revenue.data.BillingAreaRateRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.BillingAreaRate;

@ViewScoped
@ManagedBean(name="billingAreaRatesView")
public class BillingAreaRatesView {
	
	private List<BillingAreaRate> billingAreaRates;
	
	private List<BillingAreaRate> filteredBillingAreaRates;
	
	@Inject
	private BillingAreaRateRepository billingAreaRateRepo;

	@LoggedInUcsdId @Inject
	private Integer loggedInUcsdId;
	
	/**
	 * Initialize the rates
	 */
	public void load() {
		billingAreaRates = billingAreaRateRepo.findByLoggedInUcsdId(loggedInUcsdId);
	}

	/**
	 * @return the billingAreaRates
	 */
	public List<BillingAreaRate> getBillingAreaRates() {
		if (billingAreaRates == null) {
			load();
		}
		return billingAreaRates;
	}

	/**
	 * @param billingAreaRates the billingAreaRates to set
	 */
	public void setBillingAreaRates(List<BillingAreaRate> billingAreaRates) {
		this.billingAreaRates = billingAreaRates;
	}

	/**
	 * @return the filteredBillingAreaRates
	 */
	public List<BillingAreaRate> getFilteredBillingAreaRates() {
		return filteredBillingAreaRates;
	}

	/**
	 * @param filteredBillingAreaRates the filteredBillingAreaRates to set
	 */
	public void setFilteredBillingAreaRates(
			List<BillingAreaRate> filteredBillingAreaRates) {
		this.filteredBillingAreaRates = filteredBillingAreaRates;
	}
}