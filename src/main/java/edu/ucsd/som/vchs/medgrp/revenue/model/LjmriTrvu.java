package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.ucsd.som.vchs.medgrp.revenue.util.FiscalDate;

/**
 * 
 * TODO:
 * 
alter table tblLJMRI_TRVU MODIFY an int auto_increment primary key
/
 * 
 * @author myebba
 *
 */
@Entity
@Table(name = "tblLJMRI_TRVU", catalog = "mgfs")
@NamedQuery(name = "TblLJMRI_TRVU.findAll", query = "SELECT t FROM LjmriTrvu t")
public class LjmriTrvu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="an")
	private int an;
	
	@Column(name="capType")
	private String capType;
	
	@Column(name="Charges")
	private BigDecimal charges;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="department")
	private String department;
	
	@Column(name="division")
	private String division;
	
	@Column(name="divisionId", columnDefinition="smallint")
	private Integer divisionId;
	
	@Column(name="FFSPayments")
	private BigDecimal FFSPayments;
	
	@Column(name="indx")
	private String indx;
	
	@Column(name="somMajorGroup")
	private String somMajorGroup;
	
	@Column(name="somMajorGroupId")
	private Integer somMajorGroupId;
	
	@Column(name="tRVU")
	private BigDecimal tRVU;
	
	@Column(name="wRVU")
	private BigDecimal wRVU;
	
	@Transient
	private LjmriTrvuMeta meta;
	
	@Transient
	private Boolean editable;

	public LjmriTrvu() {
	}

	public int getAn() {
		return an;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public String getCapType() {
		return capType;
	}

	public void setCapType(String capType) {
		this.capType = capType;
	}

	public BigDecimal getCharges() {
		return charges;
	}

	public void setCharges(BigDecimal charges) {
		this.charges = charges;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public Integer getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	public BigDecimal getFFSPayments() {
		return FFSPayments;
	}

	public void setFFSPayments(BigDecimal fFSPayments) {
		FFSPayments = fFSPayments;
	}

	public String getIndx() {
		return indx;
	}

	public void setIndx(String indx) {
		this.indx = indx;
	}

	public String getSomMajorGroup() {
		return somMajorGroup;
	}

	public void setSomMajorGroup(String somMajorGroup) {
		this.somMajorGroup = somMajorGroup;
	}

	public Integer getSomMajorGroupId() {
		return somMajorGroupId;
	}

	public void setSomMajorGroupId(Integer somMajorGroupId) {
		this.somMajorGroupId = somMajorGroupId;
	}

	public BigDecimal gettRVU() {
		return tRVU;
	}

	public void settRVU(BigDecimal tRVU) {
		this.tRVU = tRVU;
	}

	public BigDecimal getwRVU() {
		return wRVU;
	}

	public void setwRVU(BigDecimal wRVU) {
		this.wRVU = wRVU;
	}

	public LjmriTrvuMeta getMeta() {
		return meta;
	}

	public void setMeta(LjmriTrvuMeta meta) {
		this.meta = meta;
	}
	
	/**
	 * Disable editing of this data on the UI if the date field's
	 * fiscal year does not equal the current fiscal year.
	 * @return
	 */
	public Boolean isDisabled() {
		
		// get current fiscal year based on today's date
		int currentFy = new FiscalDate(Calendar.getInstance()).getFiscalYear();
		
		// get object's fiscal year based on date field
		Calendar c = Calendar.getInstance();
		c.setTime(getDate());
		int dateFy = new FiscalDate(c).getFiscalYear();
				
		// If current FY and date FY do not match then field should be disabled in the interface
		return (currentFy != dateFy);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + an;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LjmriTrvu other = (LjmriTrvu) obj;
		if (an != other.an)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LjmriTrvu [an=" + an + ", capType=" + capType + ", charges="
				+ charges + ", date=" + date + ", department=" + department
				+ ", division=" + division + ", divisionId=" + divisionId
				+ ", Collections=" + FFSPayments + ", indx=" + indx
				+ ", somMajorGroup=" + somMajorGroup + ", somMajorGroupId="
				+ somMajorGroupId + ", tRVU=" + tRVU + ", wRVU=" + wRVU + "]";
	}
}