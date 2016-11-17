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
@Table(name="medgrp_revenue_inpatient_perc", catalog="budget")
public class RevenueInpatientPercentage extends RevenueInpatientPercentageBase {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -662599309089907450L;
	
	/**
	 * Default constructor
	 */
	public RevenueInpatientPercentage() {
		super();
	}
}
