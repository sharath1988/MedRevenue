/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for displaying revenue inpatient percentages by division
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_medgrp_revenue_inpatient_perc_psych", catalog="budget")
public class RevenueInpatientPercentagePsych extends RevenueInpatientPercentageBase {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -3968583546055599918L;

	/**
	 * Default constructor
	 */
	public RevenueInpatientPercentagePsych() {
		super();
	}
}
