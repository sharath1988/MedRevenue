package edu.ucsd.som.vchs.medgrp.revenue.model.faculty;

import java.math.BigDecimal;

public class FacultyEmployeeInfo {
	private Degree degree;
	private CompPlan compPlan;
	private Rank rank;
	private Step step;
	private Boolean offScale;
	private BigDecimal appointment = BigDecimal.valueOf(100);
	/**
	 * @return the degree
	 */
	public Degree getDegree() {
		return degree;
	}
	/**
	 * @param degree the degree to set
	 */
	public void setDegree(Degree degree) {
		this.degree = degree;
	}
	/**
	 * @return the compPlan
	 */
	public CompPlan getCompPlan() {
		return compPlan;
	}
	/**
	 * @param compPlan the compPlan to set
	 */
	public void setCompPlan(CompPlan compPlan) {
		this.compPlan = compPlan;
	}
	/**
	 * @return the rank
	 */
	public Rank getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	/**
	 * @return the step
	 */
	public Step getStep() {
		return step;
	}
	/**
	 * @param step the step to set
	 */
	public void setStep(Step step) {
		this.step = step;
	}
	/**
	 * @return the offScale
	 */
	public Boolean getOffScale() {
		return offScale;
	}
	/**
	 * @param offScale the offScale to set
	 */
	public void setOffScale(Boolean offScale) {
		this.offScale = offScale;
	}
	/**
	 * @return the appointment
	 */
	public BigDecimal getAppointment() {
		return appointment;
	}
	/**
	 * @param appointment the appointment to set
	 */
	public void setAppointment(BigDecimal appointment) {
		this.appointment = appointment;
	}
}
