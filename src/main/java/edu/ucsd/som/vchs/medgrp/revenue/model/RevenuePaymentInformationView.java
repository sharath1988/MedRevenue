/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * View that calculates the new rates based on what was entered
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_medgrp_revenue_payment_information", catalog="budget")
@NamedStoredProcedureQuery(
		name=RevenuePaymentInformationView.GET_PAYMENT_INFORMATION_BY_DIV_PROC,
		procedureName=RevenuePaymentInformationView.GET_PAYMENT_INFORMATION_BY_DIV_PROC,
		resultClasses=RevenuePaymentInformationView.class,
		parameters={
			@StoredProcedureParameter(type=Integer.class, mode=ParameterMode.IN, name="pDivisionId")
		}
	)
public class RevenuePaymentInformationView extends RevenuePaymentInformationViewBase {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -233408890769029358L;
	
	public static final String GET_PAYMENT_INFORMATION_BY_DIV_PROC = "spGetPaymentInformationByDivision";

	/**
	 * Default constructor
	 */
	public RevenuePaymentInformationView() {
		super();
	}	
}
