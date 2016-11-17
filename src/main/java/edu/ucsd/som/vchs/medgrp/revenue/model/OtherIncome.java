package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="medgrp_revenue_other_income", catalog="budget")
public class OtherIncome implements Serializable {
	private static final long serialVersionUID = 3046233742555655767L;

	@Id
	@Column(name="division_id")
	Integer divisionId;
	
	@Column(name="co_payment")
	Integer coPayment = 0;
	
	@Column(name="incentive")
	Integer incentive = 0;
	
	@Column(name="membership")
	Integer membership = 0;
	
	@Column(name="stip")
	Integer stip = 0;
	
	@Column(name="other")
	Integer other = 0;
	
	public Integer getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	public Integer getCoPayment() {
		return coPayment;
	}

	public void setCoPayment(Integer coPayment) {
		this.coPayment = coPayment;
	}

	public Integer getIncentive() {
		return incentive;
	}

	public void setIncentive(Integer incentive) {
		this.incentive = incentive;
	}

	public Integer getMembership() {
		return membership;
	}

	public void setMembership(Integer membership) {
		this.membership = membership;
	}

	public Integer getStip() {
		return stip;
	}

	public void setStip(Integer stip) {
		this.stip = stip;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
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
		OtherIncome other = (OtherIncome) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OtherIncome [divisionId=" + divisionId + ", coPayment="
				+ coPayment + ", incentive=" + incentive + ", membership="
				+ membership + ", stip=" + stip + ", other=" + other
				+ "]";
	}
}
