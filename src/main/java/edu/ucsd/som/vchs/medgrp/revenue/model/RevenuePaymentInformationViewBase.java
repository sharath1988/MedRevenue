/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author somdev5
 *
 */
@MappedSuperclass
public class RevenuePaymentInformationViewBase extends RevenuePaymentInformationBase {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -3865181912535375091L;

	/**
	 * Default constructor
	 */
	public RevenuePaymentInformationViewBase() {
		super();
	}

	@Column(name="proj_cap_collections_to_trvus")
	private BigDecimal projCapCollectionsToTrvus;
	
	@Column(name="proj_under_collections_to_trvus")
	private BigDecimal projUnderCollectionsToTrvus;
	
	@Column(name="proj_ffs_collections_to_trvus")
	private BigDecimal projFfsCollectionsToTrvus;
	
	@Column(name="proj_cap_charges")
	private BigDecimal projCapCharges;
	
	@Column(name="proj_under_charges")
	private BigDecimal projUnderCharges;
	
	@Column(name="proj_ffs_charges")
	private BigDecimal projFfsCharges;	
	
	@Column(name="total_prior_cap_charges")
	private BigDecimal totalPriorYearCapCharges;
	
	@Column(name="total_prior_under_charges")
	private BigDecimal totalPriorYearUnderCharges;
	
	@Column(name="total_prior_ffs_charges")
	private BigDecimal totalPriorYearFfsCharges;	
	
	@Column(name="proj_cap_collections")
	private BigDecimal projCapCollections;
	
	@Column(name="proj_under_collections")
	private BigDecimal projUnderCollections;
	
	@Column(name="proj_ffs_collections")
	private BigDecimal projFfsCollections;
	
	@Column(name="total_proj_collections")
	private BigDecimal totalProjCollections;
	
	@Column(name="proj_blended_rate")
	private BigDecimal projBlendedRate;
	
	@Column(name="cap_wrvus_split_percentage")
	private BigDecimal capSplitPercentage;
	
	@Column(name="under_wrvus_split_percentage")
	private BigDecimal underSplitPercentage;
	
	@Column(name="ffs_wrvus_split_percentage")
	private BigDecimal ffsSplitPercentage;
	
	@Column(name="prior_cap_collections_to_trvus")
	private BigDecimal priorYearCapCollectionsToTrvus;
	
	@Column(name="prior_under_collections_to_trvus")
	private BigDecimal priorYearUnderCollectionsToTrvus;
	
	@Column(name="prior_ffs_collections_to_trvus")
	private BigDecimal priorYearFfsCollectionsToTrvus;
	
	@Column(name="total_prior_cap_collections")
	private BigDecimal totalPriorYearCapCollections;
	
	@Column(name="total_prior_under_collections")
	private BigDecimal totalPriorYearUnderCollections;
	
	@Column(name="total_prior_ffs_collections")
	private BigDecimal totalPriorYearFfsCollections;
	
	@Column(name="total_prior_collections")
	private BigDecimal totalPriorCollections;
	
	@Column(name="prior_blended_rate")
	private BigDecimal priorBlendedRate;
	
	@Column(name="prior_copay")
	private BigDecimal priorCopay;
	
	@Column(name="total_prior_collections_inc_copay")
	private BigDecimal totalPriorCollectionsIncCopay;
	
	@Column(name="total_proj_collections_inc_copay")
	private BigDecimal totalProjCollectionsIncCopay;
	
	@Column(name="sos")
	private String sos;
	
	@Column(name="total_proj_charges")
	private BigDecimal totalProjCharges;

	@Column(name="total_proj_trvus")
	private BigDecimal totalProjTrvus;
	
	@Column(name="total_proj_wrvus")
	private BigDecimal totalProjWrvus;
	
	@Column(name="total_prior_charges")
	private BigDecimal totalPriorCharges;
	
	@Column(name="total_prior_trvus")
	private BigDecimal totalPriorTrvus;
	
	@Column(name="total_prior_wrvus")
	private BigDecimal totalPriorWrvus;
	
	@Column(name="total_12mo_charges")
	private BigDecimal rollingTotalCharges;
	
	@Column(name="total_12mo_collections")
	private BigDecimal rollingTotalCollections;
	
	@Column(name="total_12mo_trvus")
	private BigDecimal rollingTotalTrvus;
	
	@Column(name="total_12mo_wrvus")
	private BigDecimal rollingTotalWrvus;

	/**
	 * @return the projCapCollectionsToTrvus
	 */
	public BigDecimal getProjCapCollectionsToTrvus() {
		return projCapCollectionsToTrvus;
	}

	/**
	 * @param projCapCollectionsToTrvus the projCapCollectionsToTrvus to set
	 */
	public void setProjCapCollectionsToTrvus(BigDecimal projCapCollectionsToTrvus) {
		this.projCapCollectionsToTrvus = projCapCollectionsToTrvus;
	}

	/**
	 * @return the projUnderCollectionsToTrvus
	 */
	public BigDecimal getProjUnderCollectionsToTrvus() {
		return projUnderCollectionsToTrvus;
	}

	/**
	 * @param projUnderCollectionsToTrvus the projUnderCollectionsToTrvus to set
	 */
	public void setProjUnderCollectionsToTrvus(
			BigDecimal projUnderCollectionsToTrvus) {
		this.projUnderCollectionsToTrvus = projUnderCollectionsToTrvus;
	}

	/**
	 * @return the projFfsCollectionsToTrvus
	 */
	public BigDecimal getProjFfsCollectionsToTrvus() {
		return projFfsCollectionsToTrvus;
	}

	/**
	 * @param projFfsCollectionsToTrvus the projFfsCollectionsToTrvus to set
	 */
	public void setProjFfsCollectionsToTrvus(BigDecimal projFfsCollectionsToTrvus) {
		this.projFfsCollectionsToTrvus = projFfsCollectionsToTrvus;
	}

	
	/**
	 * @return the projCapCollections
	 */
	public BigDecimal getProjCapCollections() {
		return projCapCollections;
	}

	/**
	 * @param projCapCollections the projCapCollections to set
	 */
	public void setProjCapCollections(BigDecimal projCapCollections) {
		this.projCapCollections = projCapCollections;
	}

	/**
	 * @return the projUnderCollections
	 */
	public BigDecimal getProjUnderCollections() {
		return projUnderCollections;
	}

	/**
	 * @param projUnderCollections the projUnderCollections to set
	 */
	public void setProjUnderCollections(BigDecimal projUnderCollections) {
		this.projUnderCollections = projUnderCollections;
	}

	/**
	 * @return the projFfsCollections
	 */
	public BigDecimal getProjFfsCollections() {
		return projFfsCollections;
	}

	/**
	 * @param projFfsCollections the projFfsCollections to set
	 */
	public void setProjFfsCollections(BigDecimal projFfsCollections) {
		this.projFfsCollections = projFfsCollections;
	}

	/**
	 * @return the capSplitPercentage
	 */
	public BigDecimal getCapSplitPercentage() {
		return capSplitPercentage;
	}

	/**
	 * @param capSplitPercentage the capSplitPercentage to set
	 */
	public void setCapSplitPercentage(BigDecimal capSplitPercentage) {
		this.capSplitPercentage = capSplitPercentage;
	}

	/**
	 * @return the underSplitPercentage
	 */
	public BigDecimal getUnderSplitPercentage() {
		return underSplitPercentage;
	}

	/**
	 * @param underSplitPercentage the underSplitPercentage to set
	 */
	public void setUnderSplitPercentage(BigDecimal underSplitPercentage) {
		this.underSplitPercentage = underSplitPercentage;
	}

	/**
	 * @return the ffsSplitPercentage
	 */
	public BigDecimal getFfsSplitPercentage() {
		return ffsSplitPercentage;
	}

	/**
	 * @param ffsSplitPercentage the ffsSplitPercentage to set
	 */
	public void setFfsSplitPercentage(BigDecimal ffsSplitPercentage) {
		this.ffsSplitPercentage = ffsSplitPercentage;
	}

	/**
	 * @return the priorYearCapCollectionsToTrvus
	 */
	public BigDecimal getPriorYearCapCollectionsToTrvus() {
		return priorYearCapCollectionsToTrvus;
	}

	/**
	 * @param priorYearCapCollectionsToTrvus the priorYearCapCollectionsToTrvus to set
	 */
	public void setPriorYearCapCollectionsToTrvus(
			BigDecimal priorYearCapCollectionsToTrvus) {
		this.priorYearCapCollectionsToTrvus = priorYearCapCollectionsToTrvus;
	}

	/**
	 * @return the priorYearUnderCollectionsToTrvus
	 */
	public BigDecimal getPriorYearUnderCollectionsToTrvus() {
		return priorYearUnderCollectionsToTrvus;
	}

	/**
	 * @param priorYearUnderCollectionsToTrvus the priorYearUnderCollectionsToTrvus to set
	 */
	public void setPriorYearUnderCollectionsToTrvus(
			BigDecimal priorYearUnderCollectionsToTrvus) {
		this.priorYearUnderCollectionsToTrvus = priorYearUnderCollectionsToTrvus;
	}

	/**
	 * @return the priorYearFfsCollectionsToTrvus
	 */
	public BigDecimal getPriorYearFfsCollectionsToTrvus() {
		return priorYearFfsCollectionsToTrvus;
	}

	/**
	 * @param priorYearFfsCollectionsToTrvus the priorYearFfsCollectionsToTrvus to set
	 */
	public void setPriorYearFfsCollectionsToTrvus(
			BigDecimal priorYearFfsCollectionsToTrvus) {
		this.priorYearFfsCollectionsToTrvus = priorYearFfsCollectionsToTrvus;
	}

	/**
	 * @return the totalPriorYearCapCollections
	 */
	public BigDecimal getTotalPriorYearCapCollections() {
		return totalPriorYearCapCollections;
	}

	/**
	 * @param totalPriorYearCapCollections the totalPriorYearCapCollections to set
	 */
	public void setTotalPriorYearCapCollections(
			BigDecimal totalPriorYearCapCollections) {
		this.totalPriorYearCapCollections = totalPriorYearCapCollections;
	}

	/**
	 * @return the totalPriorYearUnderCollections
	 */
	public BigDecimal getTotalPriorYearUnderCollections() {
		return totalPriorYearUnderCollections;
	}

	/**
	 * @param totalPriorYearUnderCollections the totalPriorYearUnderCollections to set
	 */
	public void setTotalPriorYearUnderCollections(
			BigDecimal totalPriorYearUnderCollections) {
		this.totalPriorYearUnderCollections = totalPriorYearUnderCollections;
	}

	/**
	 * @return the totalPriorYearFfsCollections
	 */
	public BigDecimal getTotalPriorYearFfsCollections() {
		return totalPriorYearFfsCollections;
	}

	/**
	 * @param totalPriorYearFfsCollections the totalPriorYearFfsCollections to set
	 */
	public void setTotalPriorYearFfsCollections(
			BigDecimal totalPriorYearFfsCollections) {
		this.totalPriorYearFfsCollections = totalPriorYearFfsCollections;
	}
	
	/**
	 * @return the totalProjCollections
	 */
	public BigDecimal getTotalProjCollections() {
		return totalProjCollections;
	}

	/**
	 * @param totalProjCollections the totalProjCollections to set
	 */
	public void setTotalProjCollections(BigDecimal totalProjCollections) {
		this.totalProjCollections = totalProjCollections;
	}

	/**
	 * @return the projBlendedRate
	 */
	public BigDecimal getProjBlendedRate() {
		return projBlendedRate;
	}

	/**
	 * @param projBlendedRate the projBlendedRate to set
	 */
	public void setProjBlendedRate(BigDecimal projBlendedRate) {
		this.projBlendedRate = projBlendedRate;
	}

	/**
	 * @return the totalPriorCollections
	 */
	public BigDecimal getTotalPriorCollections() {
		return totalPriorCollections;
	}

	/**
	 * @param totalPriorCollections the totalPriorCollections to set
	 */
	public void setTotalPriorCollections(BigDecimal totalPriorCollections) {
		this.totalPriorCollections = totalPriorCollections;
	}

	/**
	 * @return the priorBlendedRate
	 */
	public BigDecimal getPriorBlendedRate() {
		return priorBlendedRate;
	}

	/**
	 * @param priorBlendedRate the priorBlendedRate to set
	 */
	public void setPriorBlendedRate(BigDecimal priorBlendedRate) {
		this.priorBlendedRate = priorBlendedRate;
	}
	
	/**
	 * @return the priorCopay
	 */
	public BigDecimal getPriorCopay() {
		return priorCopay;
	}

	/**
	 * @param priorCopay the priorCopay to set
	 */
	public void setPriorCopay(BigDecimal priorCopay) {
		this.priorCopay = priorCopay;
	}
	
	/**
	 * @return the totalPriorCollectionsIncCopay
	 */
	public BigDecimal getTotalPriorCollectionsIncCopay() {
		return totalPriorCollectionsIncCopay;
	}

	/**
	 * @param totalPriorCollectionsIncCopay the totalPriorCollectionsIncCopay to set
	 */
	public void setTotalPriorCollectionsIncCopay(
			BigDecimal totalPriorCollectionsIncCopay) {
		this.totalPriorCollectionsIncCopay = totalPriorCollectionsIncCopay;
	}

	/**
	 * @return the totalProjCollectionsIncCopay
	 */
	public BigDecimal getTotalProjCollectionsIncCopay() {
		return totalProjCollectionsIncCopay;
	}

	/**
	 * @param totalProjCollectionsIncCopay the totalProjCollectionsIncCopay to set
	 */
	public void setTotalProjCollectionsIncCopay(
			BigDecimal totalProjCollectionsIncCopay) {
		this.totalProjCollectionsIncCopay = totalProjCollectionsIncCopay;
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
	 * @return the totalProjCharges
	 */
	public BigDecimal getTotalProjCharges() {
		return totalProjCharges;
	}

	/**
	 * @param totalProjCharges the totalProjCharges to set
	 */
	public void setTotalProjCharges(BigDecimal totalProjCharges) {
		this.totalProjCharges = totalProjCharges;
	}

	/**
	 * @return the totalProjWrvus
	 */
	public BigDecimal getTotalProjWrvus() {
		return totalProjWrvus;
	}

	/**
	 * @param totalProjWrvus the totalProjWrvus to set
	 */
	public void setTotalProjWrvus(BigDecimal totalProjWrvus) {
		this.totalProjWrvus = totalProjWrvus;
	}

	/**
	 * @return the totalPriorCharges
	 */
	public BigDecimal getTotalPriorCharges() {
		return totalPriorCharges;
	}

	/**
	 * @param totalPriorCharges the totalPriorCharges to set
	 */
	public void setTotalPriorCharges(BigDecimal totalPriorCharges) {
		this.totalPriorCharges = totalPriorCharges;
	}

	/**
	 * @return the totalPriorTrvus
	 */
	public BigDecimal getTotalPriorTrvus() {
		return totalPriorTrvus;
	}

	/**
	 * @param totalPriorTrvus the totalPriorTrvus to set
	 */
	public void setTotalPriorTrvus(BigDecimal totalPriorTrvus) {
		this.totalPriorTrvus = totalPriorTrvus;
	}

	/**
	 * @return the totalPriorWrvus
	 */
	public BigDecimal getTotalPriorWrvus() {
		return totalPriorWrvus;
	}

	/**
	 * @param totalPriorWrvus the totalPriorWrvus to set
	 */
	public void setTotalPriorWrvus(BigDecimal totalPriorWrvus) {
		this.totalPriorWrvus = totalPriorWrvus;
	}

	/**
	 * @return the rollingTotalCharges
	 */
	public BigDecimal getRollingTotalCharges() {
		return rollingTotalCharges;
	}

	/**
	 * @param rollingTotalCharges the rollingTotalCharges to set
	 */
	public void setRollingTotalCharges(BigDecimal rollingTotalCharges) {
		this.rollingTotalCharges = rollingTotalCharges;
	}

	/**
	 * @return the rollingTotalCollections
	 */
	public BigDecimal getRollingTotalCollections() {
		return rollingTotalCollections;
	}

	/**
	 * @param rollingTotalCollections the rollingTotalCollections to set
	 */
	public void setRollingTotalCollections(BigDecimal rollingTotalCollections) {
		this.rollingTotalCollections = rollingTotalCollections;
	}

	/**
	 * @return the rollingTotalTrvus
	 */
	public BigDecimal getRollingTotalTrvus() {
		return rollingTotalTrvus;
	}

	/**
	 * @param rollingTotalTrvus the rollingTotalTrvus to set
	 */
	public void setRollingTotalTrvus(BigDecimal rollingTotalTrvus) {
		this.rollingTotalTrvus = rollingTotalTrvus;
	}

	/**
	 * @return the rollingTotalWrvus
	 */
	public BigDecimal getRollingTotalWrvus() {
		return rollingTotalWrvus;
	}

	/**
	 * @param rollingTotalWrvus the rollingTotalWrvus to set
	 */
	public void setRollingTotalWrvus(BigDecimal rollingTotalWrvus) {
		this.rollingTotalWrvus = rollingTotalWrvus;
	}
	
	/**
	 * @return the totalProjTrvus
	 */
	public BigDecimal getTotalProjTrvus() {
		return totalProjTrvus;
	}

	/**
	 * @param totalProjTrvus the totalProjTrvus to set
	 */
	public void setTotalProjTrvus(BigDecimal totalProjTrvus) {
		this.totalProjTrvus = totalProjTrvus;
	}
	
	/**
	 * @return the projCapCharges
	 */
	public BigDecimal getProjCapCharges() {
		return projCapCharges;
	}

	/**
	 * @param projCapCharges the projCapCharges to set
	 */
	public void setProjCapCharges(BigDecimal projCapCharges) {
		this.projCapCharges = projCapCharges;
	}

	/**
	 * @return the projUnderCharges
	 */
	public BigDecimal getProjUnderCharges() {
		return projUnderCharges;
	}

	/**
	 * @param projUnderCharges the projUnderCharges to set
	 */
	public void setProjUnderCharges(BigDecimal projUnderCharges) {
		this.projUnderCharges = projUnderCharges;
	}

	/**
	 * @return the projFfsharges
	 */
	public BigDecimal getProjFfsCharges() {
		return projFfsCharges;
	}

	/**
	 * @param projFfsharges the projFfsharges to set
	 */
	public void setProjFfsCharges(BigDecimal projFfsCharges) {
		this.projFfsCharges = projFfsCharges;
	}

	/**
	 * @return the totalPriorYearCapCharges
	 */
	public BigDecimal getTotalPriorYearCapCharges() {
		return totalPriorYearCapCharges;
	}

	/**
	 * @param totalPriorYearCapCharges the totalPriorYearCapCharges to set
	 */
	public void setTotalPriorYearCapCharges(BigDecimal totalPriorYearCapCharges) {
		this.totalPriorYearCapCharges = totalPriorYearCapCharges;
	}

	/**
	 * @return the totalPriorYearUnderCharges
	 */
	public BigDecimal getTotalPriorYearUnderCharges() {
		return totalPriorYearUnderCharges;
	}

	/**
	 * @param totalPriorYearUnderCharges the totalPriorYearUnderCharges to set
	 */
	public void setTotalPriorYearUnderCharges(BigDecimal totalPriorYearUnderCharges) {
		this.totalPriorYearUnderCharges = totalPriorYearUnderCharges;
	}

	/**
	 * @return the totalPriorYearFfsCharges
	 */
	public BigDecimal getTotalPriorYearFfsCharges() {
		return totalPriorYearFfsCharges;
	}

	/**
	 * @param totalPriorYearFfsCharges the totalPriorYearFfsCharges to set
	 */
	public void setTotalPriorYearFfsCharges(BigDecimal totalPriorYearFfsCharges) {
		this.totalPriorYearFfsCharges = totalPriorYearFfsCharges;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenuePaymentInformationViewBase [getId()=" + getId()
				+ ", getDivisionId()=" + getDivisionId()
				+ ", getProjCapCollectionsToTrvusPercentChange()="
				+ getProjCapCollectionsToTrvusPercentChange()
				+ ", getProjUnderCollectionsToTrvusPercentChange()="
				+ getProjUnderCollectionsToTrvusPercentChange()
				+ ", getProjFfsCollectionsToTrvusPercentChange()="
				+ getProjFfsCollectionsToTrvusPercentChange()
				+ ", getProjCopay()=" + getProjCopay() + "]";
	}
}