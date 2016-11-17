package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * POJO for displaying the payment information screen
 * 
 * @author somdev5
 *
 */
@MappedSuperclass
public class RevenuePaymentInformationBase implements Serializable {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = -1100332110080330689L;

	/**
	 * Default constructor
	 */
	public RevenuePaymentInformationBase() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="proj_cap_collections_to_trvus_perc_chg")
	private BigDecimal projCapCollectionsToTrvusPercentChange;
	
	@Column(name="proj_under_collections_to_trvus_perc_chg")
	private BigDecimal projUnderCollectionsToTrvusPercentChange;
	
	@Column(name="proj_ffs_collections_to_trvus_perc_chg")
	private BigDecimal projFfsCollectionsToTrvusPercentChange;
	
	@Column(name="proj_copay")
	private BigDecimal projCopay;

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
	 * @return the projCapCollectionsToTrvusPercentChange
	 */
	public BigDecimal getProjCapCollectionsToTrvusPercentChange() {
		return projCapCollectionsToTrvusPercentChange;
	}

	/**
	 * @param projCapCollectionsToTrvusPercentChange the projCapCollectionsToTrvusPercentChange to set
	 */
	public void setProjCapCollectionsToTrvusPercentChange(
			BigDecimal projCapCollectionsToTrvusPercentChange) {
		this.projCapCollectionsToTrvusPercentChange = projCapCollectionsToTrvusPercentChange;
	}

	/**
	 * @return the projUnderCollectionsToTrvusPercentChange
	 */
	public BigDecimal getProjUnderCollectionsToTrvusPercentChange() {
		return projUnderCollectionsToTrvusPercentChange;
	}

	/**
	 * @param projUnderCollectionsToTrvusPercentChange the projUnderCollectionsToTrvusPercentChange to set
	 */
	public void setProjUnderCollectionsToTrvusPercentChange(
			BigDecimal projUnderCollectionsToTrvusPercentChange) {
		this.projUnderCollectionsToTrvusPercentChange = projUnderCollectionsToTrvusPercentChange;
	}

	/**
	 * @return the projFfsCollectionsToTrvusPercentChange
	 */
	public BigDecimal getProjFfsCollectionsToTrvusPercentChange() {
		return projFfsCollectionsToTrvusPercentChange;
	}

	/**
	 * @param projFfsCollectionsToTrvusPercentChange the projFfsCollectionsToTrvusPercentChange to set
	 */
	public void setProjFfsCollectionsToTrvusPercentChange(
			BigDecimal projFfsCollectionsToTrvusPercentChange) {
		this.projFfsCollectionsToTrvusPercentChange = projFfsCollectionsToTrvusPercentChange;
	}
	
	/**
	 * @return the projCopay
	 */
	public BigDecimal getProjCopay() {
		return projCopay;
	}

	/**
	 * @param projCopay the projCopay to set
	 */
	public void setProjCopay(BigDecimal projCopay) {
		this.projCopay = projCopay;
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
		RevenuePaymentInformationBase other = (RevenuePaymentInformationBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
