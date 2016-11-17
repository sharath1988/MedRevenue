/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for persisting collections to trvus rate changes
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="medgrp_revenue_payment_information", catalog="budget")
public class RevenuePaymentInformation extends RevenuePaymentInformationBase {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 6365203868225430945L;

	/**
	 * Default constructor
	 */
	public RevenuePaymentInformation() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenuePaymentInformation [getId()=" + getId()
				+ ", getDivisionId()=" + getDivisionId()
				+ ", getProjCapCollectionsToTrvusPercentChange()="
				+ getProjCapCollectionsToTrvusPercentChange()
				+ ", getProjUnderCollectionsToTrvusPercentChange()="
				+ getProjUnderCollectionsToTrvusPercentChange()
				+ ", getProjFfsCollectionsToTrvusPercentChange()="
				+ getProjFfsCollectionsToTrvusPercentChange() + "]";
	}
}
