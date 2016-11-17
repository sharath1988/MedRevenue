/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.IsBudgetAdmin;
import edu.ucsd.som.vchs.medgrp.revenue.data.BudgetMetadataRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.BudgetMetadata;

/**
 * Managed bean for the budget metadata admin form
 * @author somdev5
 *
 */
@ViewScoped
@ManagedBean(name="budgetMetadataBean")
public class BudgetMetadataBean implements Serializable {
	
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 6714094053504566218L;
	
	@Inject
	private BudgetMetadataRepository metaRepo;
	
	@Inject
	@IsBudgetAdmin
	private Boolean isBudgetAdmin;
	
	@Inject
	private FacesContext facesContext;
	
	private BudgetMetadata budgetMetadata;

	/**
	 * Default constructor
	 */
	public BudgetMetadataBean() {
		super();
	}
	
	@PostConstruct
	public void checkPermissions() {
		if (!isBudgetAdmin) {
			throw new IllegalAccessError("You do not have permission to view this resource");
		}
	}
		
	/**
	 * Saves the updated budget metadata
	 */
	public void updateMetadataOnChange() {
		metaRepo.save(getBudgetMetadata());
		facesContext.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Budget Metadata Modification", "Budget metadata updated successfully"));			
	}

	/**
	 * @return the budgetMetadata
	 */
	public BudgetMetadata getBudgetMetadata() {
		if (budgetMetadata == null) {
			budgetMetadata = metaRepo.getActive();
		}
		return budgetMetadata;
	}

	/**
	 * @param budgetMetadata the budgetMetadata to set
	 */
	public void setBudgetMetadata(BudgetMetadata budgetMetadata) {
		this.budgetMetadata = budgetMetadata;
	}	
}