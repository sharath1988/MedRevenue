/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * POJO for provider-level revenue prior year actuals
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_revenue_prior_actuals_by_prov", catalog="budget")
public class RevenuePriorYearActual implements Serializable {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 4144217911045912828L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="py_actual_id")
	private Integer priorYearActualId;
	
	@Column(name="employee_fullname")
	private String providerName;
	
	@Column(name="employee_ucsd_id",columnDefinition="DECIMAL")
	private Integer employeeUcsdId;
	
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="sos")
	private String siteOfService;
	
	@Column(name="total_charges")
	private BigDecimal charges;
	
	@Column(name="total_collections")
	private BigDecimal collections;
	
	@Column(name="total_trvus")
	private BigDecimal trvus;
	
	@Column(name="total_wrvus")
	private BigDecimal wrvus;
	
	@Column(name="copay")
	private BigDecimal copay;
		
	@Column(name="cap_wrvu_split")
	private BigDecimal capWrvuSplit;
	
	@Column(name="under_wrvu_split")
	private BigDecimal underWrvuSplit;
	
	@Column(name="ffs_wrvu_split")
	private BigDecimal ffsWrvuSplit;
	
	@Column(name="cap_collection_rate")
	private BigDecimal capCollectionRate;
	
	@Column(name="under_collection_rate")
	private BigDecimal underCollectionRate;
	
	@Column(name="ffs_collection_rate")
	private BigDecimal ffsCollectionRate;

	/**
	 * Default constructor
	 */
	public RevenuePriorYearActual() {
		super();
	}

	/**
	 * @return the priorYearActualId
	 */
	public Integer getPriorYearActualId() {
		return priorYearActualId;
	}

	/**
	 * @param priorYearActualId the priorYearActualId to set
	 */
	public void setPriorYearActualId(Integer priorYearActualId) {
		this.priorYearActualId = priorYearActualId;
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
	 * @return the siteOfService
	 */
	public String getSiteOfService() {
		return siteOfService;
	}

	/**
	 * @param siteOfService the siteOfService to set
	 */
	public void setSiteOfService(String siteOfService) {
		this.siteOfService = siteOfService;
	}

	/**
	 * @return the charges
	 */
	public BigDecimal getCharges() {
		return charges;
	}

	/**
	 * @param charges the charges to set
	 */
	public void setCharges(BigDecimal charges) {
		this.charges = charges;
	}

	/**
	 * @return the collections
	 */
	public BigDecimal getCollections() {
		return collections;
	}

	/**
	 * @param collections the collections to set
	 */
	public void setCollections(BigDecimal collections) {
		this.collections = collections;
	}

	/**
	 * @return the trvus
	 */
	public BigDecimal getTrvus() {
		return trvus;
	}

	/**
	 * @param trvus the trvus to set
	 */
	public void setTrvus(BigDecimal trvus) {
		this.trvus = trvus;
	}

	/**
	 * @return the wrvus
	 */
	public BigDecimal getWrvus() {
		return wrvus;
	}

	/**
	 * @param wrvus the wrvus to set
	 */
	public void setWrvus(BigDecimal wrvus) {
		this.wrvus = wrvus;
	}

	/**
	 * @return the copay
	 */
	public BigDecimal getCopay() {
		return copay;
	}

	/**
	 * @param copay the copay to set
	 */
	public void setCopay(BigDecimal copay) {
		this.copay = copay;
	}
	
	/**
	 * @return the capWrvuSplit
	 */
	public BigDecimal getCapWrvuSplit() {
		return capWrvuSplit;
	}

	/**
	 * @param capWrvuSplit the capWrvuSplit to set
	 */
	public void setCapWrvuSplit(BigDecimal capWrvuSplit) {
		this.capWrvuSplit = capWrvuSplit;
	}

	/**
	 * @return the underWrvuSplit
	 */
	public BigDecimal getUnderWrvuSplit() {
		return underWrvuSplit;
	}

	/**
	 * @param underWrvuSplit the underWrvuSplit to set
	 */
	public void setUnderWrvuSplit(BigDecimal underWrvuSplit) {
		this.underWrvuSplit = underWrvuSplit;
	}

	/**
	 * @return the ffsWrvuSplit
	 */
	public BigDecimal getFfsWrvuSplit() {
		return ffsWrvuSplit;
	}

	/**
	 * @param ffsWrvuSplit the ffsWrvuSplit to set
	 */
	public void setFfsWrvuSplit(BigDecimal ffsWrvuSplit) {
		this.ffsWrvuSplit = ffsWrvuSplit;
	}

	/**
	 * @return the capCollectionRate
	 */
	public BigDecimal getCapCollectionRate() {
		return capCollectionRate;
	}

	/**
	 * @param capCollectionRate the capCollectionRate to set
	 */
	public void setCapCollectionRate(BigDecimal capCollectionRate) {
		this.capCollectionRate = capCollectionRate;
	}

	/**
	 * @return the underCollectionRate
	 */
	public BigDecimal getUnderCollectionRate() {
		return underCollectionRate;
	}

	/**
	 * @param underCollectionRate the underCollectionRate to set
	 */
	public void setUnderCollectionRate(BigDecimal underCollectionRate) {
		this.underCollectionRate = underCollectionRate;
	}

	/**
	 * @return the ffsCollectionRate
	 */
	public BigDecimal getFfsCollectionRate() {
		return ffsCollectionRate;
	}

	/**
	 * @param ffsCollectionRate the ffsCollectionRate to set
	 */
	public void setFfsCollectionRate(BigDecimal ffsCollectionRate) {
		this.ffsCollectionRate = ffsCollectionRate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((priorYearActualId == null) ? 0 : priorYearActualId
						.hashCode());
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
		RevenuePriorYearActual other = (RevenuePriorYearActual) obj;
		if (priorYearActualId == null) {
			if (other.priorYearActualId != null)
				return false;
		} else if (!priorYearActualId.equals(other.priorYearActualId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenuePriorYearActual [priorYearActualId=" + priorYearActualId
				+ ", providerName=" + providerName + ", employeeUcsdId="
				+ employeeUcsdId + ", divisionId=" + divisionId
				+ ", siteOfService=" + siteOfService + ", charges=" + charges
				+ ", collections=" + collections + ", trvus=" + trvus
				+ ", wrvus=" + wrvus + ", copay=" + copay + ", capWrvuSplit="
				+ capWrvuSplit + ", underWrvuSplit=" + underWrvuSplit
				+ ", ffsWrvuSplit=" + ffsWrvuSplit + ", capCollectionRate="
				+ capCollectionRate + ", underCollectionRate="
				+ underCollectionRate + ", ffsCollectionRate="
				+ ffsCollectionRate + "]";
	}
}
