/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Payment information view for Psych since it is broken out by sos
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_medgrp_revenue_payment_information_psych", catalog="budget")
public class RevenuePaymentInformationViewPsych extends RevenuePaymentInformationViewBase {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 7102853204579628592L;

	/**
	 * Default constructor
	 */
	public RevenuePaymentInformationViewPsych() {
		super();
	}
}
