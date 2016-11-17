/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.data.DivisionRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.Division;
import edu.ucsd.som.vchs.medgrp.revenue.service.TabRenderService;

/**
 * DivisionInfo Bean
 * @author somdev5
 *
 */
@ViewScoped
@ManagedBean(name="divisionInfoBean")
public class DivisionInfoBean {
	
	@Inject
	private DivisionRepository divisionRepo;
	
	@Inject
	private TabRenderService tabRenderService;
	
	private Integer divisionId;
	
	private Division divisionInfo;
	
	/**
	 * Default constructor
	 */
	public DivisionInfoBean() {
		super();
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
	 * @return divisionInfo
	 */
	public Division getDivisionInfo() {
		if (divisionInfo == null) {
			divisionInfo = divisionRepo.findBy(getDivisionId()); 
		}
		return divisionInfo;
	}
	
	/**
	 * Should we render the adjustments tab for this division?
	 * @return
	 */
	public Boolean getRenderAdjustmentsTabForDivision() {
		return tabRenderService.renderAdjustmentsTabForDivision(getDivisionId());
	}
	
	/**
	 * Should we render the care payments tab for this division?
	 * @return
	 */
	public Boolean getRenderCarePaymentsTabForDivision() {
		return tabRenderService.renderCarePaymentsTabForDivision(getDivisionId());
	}
}