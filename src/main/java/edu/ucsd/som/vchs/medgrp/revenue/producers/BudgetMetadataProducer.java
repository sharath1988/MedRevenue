/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.producers;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.BudgetMetadataQualifer;
import edu.ucsd.som.vchs.medgrp.revenue.data.BudgetMetadataRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.BudgetMetadata;

/**
 * Producer for budget metadata
 * 
 * @author somdev5
 *
 */
public class BudgetMetadataProducer {

	@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject
	private BudgetMetadataRepository repo;
	
	private BudgetMetadata activeBudgetMetadata;
	
	@PostConstruct
	private void loadActiveBudgetMetadata() {
		activeBudgetMetadata = repo.getActive();
	}

	@ViewScoped
	@Produces @BudgetMetadataQualifer
	@Named("budgetMetadata")
	public BudgetMetadata getBudgetMetadata() {
		return activeBudgetMetadata;
	}
}
