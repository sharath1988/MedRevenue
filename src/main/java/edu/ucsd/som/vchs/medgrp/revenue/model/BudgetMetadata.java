/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * POJO for BudgetMetadata.  Making it all columns so we can join on start and end date
 * when loading prior year data from mgbs.
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="budgetMetadata", catalog="budget")
@NamedQuery(name=BudgetMetadata.IS_ACTIVE, query="SELECT m FROM BudgetMetadata m WHERE m.activeFlag = true")
public class BudgetMetadata implements Serializable {
	
	public static final String IS_ACTIVE = "BudgetMetadata.IsActive";
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2814429777283691599L;
	
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="budget_start_date")
	private Date budgetStartDate;
	
	@Column(name="budget_end_date")
	private Date budgetEndDate;
	
	@Column(name="budget_current_year")
	private String budgetCurrentYear;
	
	@Column(name="budget_prior_year")
	private String budgetPriorYear;
	
	@Column(name="cap_rate")
	private BigDecimal defaultCapRate;
	
	@Column(name="active_flag")
	private Boolean activeFlag;
	
	@Transient
	public String getCurrentBudgetYearLabel() {
		return "FY " + this.getBudgetCurrentYear();
	}
	
	@Transient
	public String getPriorBudgetYearLabel() {
		return "CY " + this.getBudgetPriorYear();
	}

	/**
	 * Default constructor
	 */
	public BudgetMetadata() {
		super();
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the budgetStartDate
	 */
	public Date getBudgetStartDate() {
		return budgetStartDate;
	}

	/**
	 * @param budgetStartDate the budgetStartDate to set
	 */
	public void setBudgetStartDate(Date budgetStartDate) {
		this.budgetStartDate = budgetStartDate;
	}

	/**
	 * @return the budgetEndDate
	 */
	public Date getBudgetEndDate() {
		return budgetEndDate;
	}

	/**
	 * @param budgetEndDate the budgetEndDate to set
	 */
	public void setBudgetEndDate(Date budgetEndDate) {
		this.budgetEndDate = budgetEndDate;
	}

	/**
	 * @return the budgetCurrentYear
	 */
	public String getBudgetCurrentYear() {
		return budgetCurrentYear;
	}

	/**
	 * @param budgetCurrentYear the budgetCurrentYear to set
	 */
	public void setBudgetCurrentYear(String budgetCurrentYear) {
		this.budgetCurrentYear = budgetCurrentYear;
	}

	/**
	 * @return the budgetPriorYear
	 */
	public String getBudgetPriorYear() {
		return budgetPriorYear;
	}

	/**
	 * @param budgetPriorYear the budgetPriorYear to set
	 */
	public void setBudgetPriorYear(String budgetPriorYear) {
		this.budgetPriorYear = budgetPriorYear;
	}

	/**
	 * @return the defaultCapRate
	 */
	public BigDecimal getDefaultCapRate() {
		return defaultCapRate;
	}

	/**
	 * @param defaultCapRate the defaultCapRate to set
	 */
	public void setDefaultCapRate(BigDecimal defaultCapRate) {
		this.defaultCapRate = defaultCapRate;
	}
	
	

	/**
	 * @return the activeFlag
	 */
	public Boolean getActiveFlag() {
		return activeFlag;
	}

	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BudgetMetadata other = (BudgetMetadata) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BudgetMetadata [id=" + id + ", budgetStartDate="
				+ budgetStartDate + ", budgetEndDate=" + budgetEndDate
				+ ", budgetCurrentYear=" + budgetCurrentYear
				+ ", budgetPriorYear=" + budgetPriorYear + ", defaultCapRate="
				+ defaultCapRate + ", activeFlag=" + activeFlag + "]";
	}
}
