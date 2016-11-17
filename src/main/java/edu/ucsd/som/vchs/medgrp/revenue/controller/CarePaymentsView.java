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

import edu.ucsd.som.vchs.medgrp.revenue.data.CarePaymentDetailRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.CarePaymentDivisionTotalRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.CarePaymentDetail;
import edu.ucsd.som.vchs.medgrp.revenue.model.CarePaymentDivisionTotal;


/**
 * View Managed bean for populating the Care payments tab
 * 
 * @author somdev5
 *
 */
@ViewScoped
@ManagedBean(name="carePaymentsView")
public class CarePaymentsView implements Serializable {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -4009687847201106683L;

	@Inject
	private Log log;

	@Inject
	private CarePaymentDetailRepository carePaymentRepo;
	
	@Inject
	private CarePaymentDivisionTotalRepository carePaymentDivTotalRepo;
			
	private Integer divisionId;

	private List<CarePaymentDetail> carePayments;
	
	private List<CarePaymentDetail> filteredCarePayments;
	
	private CarePaymentDivisionTotal carePaymentDivisionTotal;
	
	/**
	 * Default constructor
	 */
	public CarePaymentsView() {
		super();
	}
	
	/**
	 * loads from revenue split.  Also computes totals for each category
	 */
	public void loadData() {
		log.info("loading carepayments for divisionId=" + divisionId);		
		refreshTotals();
	}

	/**
	 * Refreshes totals when CAP distribution input field is updated on the revenue worksheet
	 */
	public void refreshTotals() {
		this.carePayments = carePaymentRepo.findBySomDivisionId(divisionId);
		this.carePaymentDivisionTotal = carePaymentDivTotalRepo.findBy(divisionId);
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
	 * @return the carePayments
	 */
	public List<CarePaymentDetail> getCarePayments() {
		return carePayments;
	}

	/**
	 * @param carePayments the carePayments to set
	 */
	public void setCarePayments(List<CarePaymentDetail> carePayments) {
		this.carePayments = carePayments;
	}

	/**
	 * @return the filteredCarePayments
	 */
	public List<CarePaymentDetail> getFilteredCarePayments() {
		return filteredCarePayments;
	}

	/**
	 * @param filteredCarePayments the filteredCarePayments to set
	 */
	public void setFilteredCarePayments(List<CarePaymentDetail> filteredCarePayments) {
		this.filteredCarePayments = filteredCarePayments;
	}

	/**
	 * @return the carePaymentDivisionTotal
	 */
	public CarePaymentDivisionTotal getCarePaymentDivisionTotal() {
		return carePaymentDivisionTotal;
	}

	/**
	 * @param carePaymentDivisionTotal the carePaymentDivisionTotal to set
	 */
	public void setCarePaymentDivisionTotal(
			CarePaymentDivisionTotal carePaymentDivisionTotal) {
		this.carePaymentDivisionTotal = carePaymentDivisionTotal;
	}
}