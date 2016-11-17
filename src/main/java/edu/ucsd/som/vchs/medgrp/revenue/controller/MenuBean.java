/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.IsBudgetAdmin;

/**
 * @author somdev5
 *
 */
@SessionScoped
@ManagedBean(name="menuBean")
public class MenuBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3664130464079902687L;

	@Inject
	@ConfigProperty(name="mgfs.revenue.path")
	private String mgfsRootPath;
	
	@Inject
	@ConfigProperty(name="som.ucsd.edu.budget.help.url")
	private String budgetHelpUrl;
	
	@Inject
	@ConfigProperty(name="som.ucsd.edu.budget.help.developer.url")
	private String budgetDeveloperGuideUrl;
	
	@Inject
	@ConfigProperty(name="vchs.budget.module.url")
	private String vchsBudgetModuleUrl;
	
	@Inject 
	@IsBudgetAdmin
	private Boolean isBudgetAdmin;
	

	/**
	 * Default constructor
	 */
	public MenuBean() {
		super();
	}

	/**
	 * @return the mgfsRootPath
	 */
	public String getMgfsRootPath() {
		return mgfsRootPath;
	}

	/**
	 * @param mgfsRootPath the mgfsRootPath to set
	 */
	public void setMgfsRootPath(String mgfsRootPath) {
		this.mgfsRootPath = mgfsRootPath;
	}

	/**
	 * @return the isBudgetAdmin
	 */
	public Boolean getIsBudgetAdmin() {
		return isBudgetAdmin;
	}

	/**
	 * @param isBudgetAdmin the isBudgetAdmin to set
	 */
	public void setIsBudgetAdmin(Boolean isBudgetAdmin) {
		this.isBudgetAdmin = isBudgetAdmin;
	}

	/**
	 * @return the budgetHelpUrl
	 */
	public String getBudgetHelpUrl() {
		return budgetHelpUrl;
	}

	/**
	 * @param budgetHelpUrl the budgetHelpUrl to set
	 */
	public void setBudgetHelpUrl(String budgetHelpUrl) {
		this.budgetHelpUrl = budgetHelpUrl;
	}

	/**
	 * @return the budgetDeveloperGuideUrl
	 */
	public String getBudgetDeveloperGuideUrl() {
		return budgetDeveloperGuideUrl;
	}

	/**
	 * @param budgetDeveloperGuideUrl the budgetDeveloperGuideUrl to set
	 */
	public void setBudgetDeveloperGuideUrl(String budgetDeveloperGuideUrl) {
		this.budgetDeveloperGuideUrl = budgetDeveloperGuideUrl;
	}

	/**
	 * @return the vchsBudgetModuleUrl
	 */
	public String getVchsBudgetModuleUrl() {
		return vchsBudgetModuleUrl;
	}

	/**
	 * @param vchsBudgetModuleUrl the vchsBudgetModuleUrl to set
	 */
	public void setVchsBudgetModuleUrl(String vchsBudgetModuleUrl) {
		this.vchsBudgetModuleUrl = vchsBudgetModuleUrl;
	}


}
