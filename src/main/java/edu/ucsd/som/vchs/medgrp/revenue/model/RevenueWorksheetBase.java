/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * MappedSuperclass that only contains persisted revenue info
 * 
 * @author somdev5
 *
 */
@MappedSuperclass
public abstract class RevenueWorksheetBase implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -250681542063886459L;

	/**
	 * Default constructor
	 */
	public RevenueWorksheetBase() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", columnDefinition="BIGINT")
	private Integer id;
	
	@Column(name="division_id")
	private Integer divisionId;
		
	@Column(name="proj_wrvus")
	private BigDecimal projWrvus;
	
	@Column(name="sos")
	private String sos;
	
	@Column(name="worksheet_desc_id")
	private Integer worksheetDescId;
	
	@Column(name="care_payment_rate")
	private BigDecimal carePaymentRate;

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

	/**
	 * @return the projWrvus
	 */
	public BigDecimal getProjWrvus() {
		return projWrvus;
	}

	/**
	 * @param projWrvus the projWrvus to set
	 */
	public void setProjWrvus(BigDecimal projWrvus) {
		this.projWrvus = projWrvus;
	}

	/**
	 * @return the worksheetDescId
	 */
	public Integer getWorksheetDescId() {
		return worksheetDescId;
	}

	/**
	 * @param worksheetDescId the worksheetDescId to set
	 */
	public void setWorksheetDescId(Integer worksheetDescId) {
		this.worksheetDescId = worksheetDescId;
	}
	
	/**
	 * @return the carePaymentRate
	 */
	public BigDecimal getCarePaymentRate() {
		return carePaymentRate;
	}

	/**
	 * @param carePaymentRate the carePaymentRate to set
	 */
	public void setCarePaymentRate(BigDecimal carePaymentRate) {
		this.carePaymentRate = carePaymentRate;
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
		RevenueWorksheetBase other = (RevenueWorksheetBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
