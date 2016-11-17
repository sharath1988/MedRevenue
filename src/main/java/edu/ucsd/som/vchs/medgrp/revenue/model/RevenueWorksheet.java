/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity that maps to medgrp_revenue_worksheet table.
 * This class is responsible for persisting Revenue Worksheet info
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="medgrp_revenue_worksheet", catalog="budget")
@NamedNativeQueries({
		@NamedNativeQuery(name=RevenueWorksheet.APPLY_PERCENT_CHANGE, query="update medgrp_revenue_worksheet w join v_revenue_prior_actuals_by_prov py on py.py_actual_id = w.py_actual_id set w.proj_wrvus = py.total_wrvus * ?2 where w.division_id = ?1"),
		@NamedNativeQuery(name=RevenueWorksheet.RESET_PERCENT_CHANGE, query="update medgrp_revenue_worksheet w join v_revenue_prior_actuals_by_prov py on py.py_actual_id = w.py_actual_id set w.proj_wrvus = py.total_wrvus where w.division_id = ?1")
})
public class RevenueWorksheet extends RevenueWorksheetBase {
	
	public static final String APPLY_PERCENT_CHANGE = "RevenueWorksheet.ApplyPercentChange";
	public static final String RESET_PERCENT_CHANGE = "RevenueWorksheet.ResetPercentChange";

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 7371855784466794323L;
	
	@OneToOne
	@JoinColumn(name="py_actual_id", nullable=true)
	private RevenuePriorYearActual priorYearActuals;
	
	/**
	 * Default constructor
	 */
	public RevenueWorksheet() {
		super();
	}

	/**
	 * @return the priorYearActuals
	 */
	public RevenuePriorYearActual getPriorYearActuals() {
		return priorYearActuals;
	}

	/**
	 * @param priorYearActuals the priorYearActuals to set
	 */
	public void setPriorYearActuals(RevenuePriorYearActual priorYearActuals) {
		this.priorYearActuals = priorYearActuals;
	}
}
