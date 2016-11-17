/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Rolling 12 months metadata
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="rolling_12_month_range", catalog="budget")
public class RollingMetadata implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 339145710947210842L;
	
	@Id
	@Column
	private Integer id;
	
	@Column(name="budget_start_date")
	private Date budgetStartDate;
	
	@Column(name="budget_end_date")
	private Date budgetEndDate;
	
	@Transient
	public String getRolling12moStartLabel() {
		Date start = this.getBudgetStartDate();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return format.format(start);
	}
	
	@Transient
	public String getRolling12moEndLabel() {
		Date end = this.getBudgetEndDate();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return format.format(end);
	}

	/**
	 * Default constructor
	 */
	public RollingMetadata() {
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
		RollingMetadata other = (RollingMetadata) obj;
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
		return "RollingMetadata [id=" + id + ", budgetStartDate="
				+ budgetStartDate + ", budgetEndDate=" + budgetEndDate + "]";
	}
}
