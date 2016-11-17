/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * POJO for holding revenue provider percentage split by CAP, UNDER, and FFS
 * 
 * @author somdev5
 * 
 */
@Entity
@Table(name="v_revenue_prior_actual_split_perc_by_category_and_prov", catalog="budget")
public class RevenueProviderSplit implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -4937918512989992175L;
		
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="employee_fullname")
	private String providerName;
	
	@Column(name="employee_ucsd_id",columnDefinition="DECIMAL")
	private Integer employeeUcsdId;
	
	@Column(name="sos")
	private String sos;
	
	@Column(name="total_prov_wrvus")
	private BigDecimal totalProviderWrvus;
	
	@Column(name="total_cap_wrvus")
	private BigDecimal totalCapWrvus;
	
	@Column(name="perc_cap_wrvus")
	private BigDecimal capWrvusPercent;
	
	@Column(name="total_under_wrvus")
	private BigDecimal totalUnderWrvus;
	
	@Column(name="perc_under_wrvus")
	private BigDecimal underWrvusPercent;
	
	@Column(name="total_ffs_wrvus")
	private BigDecimal totalFfsWrvus;
	
	@Column(name="perc_ffs_wrvus")
	private BigDecimal ffsWrvusPercent;
	
	/**
	 * Default constructor
	 */
	public RevenueProviderSplit() {
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
	 * @return the providerName
	 */
	public String getProviderName() {
		return providerName;
	}

	/**
	 * @param providerName the providerName to set
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * @return the employeeUcsdId
	 */
	public Integer getEmployeeUcsdId() {
		return employeeUcsdId;
	}

	/**
	 * @param employeeUcsdId the employeeUcsdId to set
	 */
	public void setEmployeeUcsdId(Integer employeeUcsdId) {
		this.employeeUcsdId = employeeUcsdId;
	}

	/**
	 * @return the totalProviderWrvus
	 */
	public BigDecimal getTotalProviderWrvus() {
		return totalProviderWrvus;
	}

	/**
	 * @param totalProviderWrvus the totalProviderWrvus to set
	 */
	public void setTotalProviderWrvus(BigDecimal totalProviderWrvus) {
		this.totalProviderWrvus = totalProviderWrvus;
	}

	/**
	 * @return the totalCapWrvus
	 */
	public BigDecimal getTotalCapWrvus() {
		return totalCapWrvus;
	}

	/**
	 * @param totalCapWrvus the totalCapWrvus to set
	 */
	public void setTotalCapWrvus(BigDecimal totalCapWrvus) {
		this.totalCapWrvus = totalCapWrvus;
	}

	/**
	 * @return the capWrvusPercent
	 */
	public BigDecimal getCapWrvusPercent() {
		return capWrvusPercent;
	}

	/**
	 * @param capWrvusPercent the capWrvusPercent to set
	 */
	public void setCapWrvusPercent(BigDecimal capWrvusPercent) {
		this.capWrvusPercent = capWrvusPercent;
	}

	/**
	 * @return the totalUnderWrvus
	 */
	public BigDecimal getTotalUnderWrvus() {
		return totalUnderWrvus;
	}

	/**
	 * @param totalUnderWrvus the totalUnderWrvus to set
	 */
	public void setTotalUnderWrvus(BigDecimal totalUnderWrvus) {
		this.totalUnderWrvus = totalUnderWrvus;
	}

	/**
	 * @return the underWrvusPercent
	 */
	public BigDecimal getUnderWrvusPercent() {
		return underWrvusPercent;
	}

	/**
	 * @param underWrvusPercent the underWrvusPercent to set
	 */
	public void setUnderWrvusPercent(BigDecimal underWrvusPercent) {
		this.underWrvusPercent = underWrvusPercent;
	}

	/**
	 * @return the totalFfsWrvus
	 */
	public BigDecimal getTotalFfsWrvus() {
		return totalFfsWrvus;
	}

	/**
	 * @param totalFfsWrvus the totalFfsWrvus to set
	 */
	public void setTotalFfsWrvus(BigDecimal totalFfsWrvus) {
		this.totalFfsWrvus = totalFfsWrvus;
	}

	/**
	 * @return the ffsWrvusPercent
	 */
	public BigDecimal getFfsWrvusPercent() {
		return ffsWrvusPercent;
	}
	
	/**
	 * @return the sos
	 */
	public String getSos() {
		return sos;
	}

	/**
	 * @param sos the sos to set
	 */
	public void setSos(String sos) {
		this.sos = sos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((employeeUcsdId == null) ? 0 : employeeUcsdId.hashCode());
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
		RevenueProviderSplit other = (RevenueProviderSplit) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (employeeUcsdId == null) {
			if (other.employeeUcsdId != null)
				return false;
		} else if (!employeeUcsdId.equals(other.employeeUcsdId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenueProviderSplit [id=" + id + ", divisionId=" + divisionId
				+ ", providerName=" + providerName + ", employeeUcsdId="
				+ employeeUcsdId + ", totalProviderWrvus=" + totalProviderWrvus
				+ ", totalCapWrvus=" + totalCapWrvus + ", capWrvusPercent="
				+ capWrvusPercent + ", totalUnderWrvus=" + totalUnderWrvus
				+ ", underWrvusPercent=" + underWrvusPercent
				+ ", totalFfsWrvus=" + totalFfsWrvus + ", ffsWrvusPercent="
				+ ffsWrvusPercent + "]";
	}
}
