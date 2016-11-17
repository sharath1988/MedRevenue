/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.LoggedInUcsdId;
import edu.ucsd.som.vchs.medgrp.revenue.auth.EmployeeDivision;
import edu.ucsd.som.vchs.medgrp.revenue.auth.EmployeeDivisionRepository;
import edu.ucsd.som.vchs.medgrp.revenue.webutil.AutoCompleteUtil;

/**
 * Producer for the division list on the revenue homepage
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@ViewScoped
@ManagedBean(name="medGrpRevenueView")
public class MedGrpRevenueView implements Serializable {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1499089292507240762L;
	
	private static final String GROUP_AREA_MED_GROUP = "MedGrp";
	
	@Inject 
	private EmployeeDivisionRepository repository;
	
	@Inject @LoggedInUcsdId 
	private Integer loggedInUcsdId;

	private List<EmployeeDivision> medGrpRevenueDivisions;
	
	private List<EmployeeDivision> filteredMedGrpRevenueDivisions;
	
	@Inject
	private AutoCompleteUtil autoCompleteUtil;
	
	private Integer medGrpRevenueDivisionCount;
	
	@Inject
	@ConfigProperty(name="vchs.medgrp.revenue.anesthesiology.url")
	private String anesthesiologyUrl;
		
	/**
	 * Getter for medGrpRevenueDivisions
	 * @return
	 */
	public List<EmployeeDivision> getMedGrpRevenueDivisions() {
		return medGrpRevenueDivisions;
	}
	
	/**
	 * Getter for medGrpRevenueDivisionCount
	 * @return
	 */
	public Integer getMedGrpRevenueDivisionCount() {
		return medGrpRevenueDivisionCount;
	}
		
	@PostConstruct
	public void retrieveMedGrpRevenueDivisionsByUserId() { 
		medGrpRevenueDivisions = repository.findOptionalByEmployeeUcsdIdAndGroupArea(loggedInUcsdId, GROUP_AREA_MED_GROUP);
		medGrpRevenueDivisionCount = medGrpRevenueDivisions.size();
	}
	
	/**
	 * Autocomplete method for department names filter
	 * 
	 * @param query
	 * @return
	 */
	public List<String> completeDepartmentName(String query) {
		return autoCompleteUtil.selectDistinctPropertyFromList(medGrpRevenueDivisions, "departmentName", query);
	}
	
	/**
	 * Autocomplete method for division names filter
	 * 
	 * @param query
	 * @return
	 */
	public List<String> completeDivisonName(String query) {
		return autoCompleteUtil.selectDistinctPropertyFromList(medGrpRevenueDivisions, "divisionName", query);
	}
	
	/**
	 * @return the filteredMedGrpRevenueDivisions
	 */
	public List<EmployeeDivision> getFilteredMedGrpRevenueDivisions() {
		return filteredMedGrpRevenueDivisions;
	}

	/**
	 * @param filteredMedGrpRevenueDivisions the filteredMedGrpRevenueDivisions to set
	 */
	public void setFilteredMedGrpRevenueDivisions(
			List<EmployeeDivision> filteredMedGrpRevenueDivisions) {
		this.filteredMedGrpRevenueDivisions = filteredMedGrpRevenueDivisions;
	}

	/**
	 * @return the anesthesiologyUrl
	 */
	public String getAnesthesiologyUrl() {
		return anesthesiologyUrl;
	}

	/**
	 * @param anesthesiologyUrl the anesthesiologyUrl to set
	 */
	public void setAnesthesiologyUrl(String anesthesiologyUrl) {
		this.anesthesiologyUrl = anesthesiologyUrl;
	}
}
