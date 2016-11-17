/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.producers;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.RollingMetadataQualifer;
import edu.ucsd.som.vchs.medgrp.revenue.data.RollingMetadataRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.RollingMetadata;

/**
 * Producer to display start and end months for rolling 12 month revenue worksheet
 * 
 * @author somdev5
 *
 */
public class RollingMetadataProducer {

	@Inject
	private RollingMetadataRepository rollingRepo;
	
	private RollingMetadata rollingMetadata;
	
	@PostConstruct
	private void loadRollingMetadata() {
		rollingMetadata = rollingRepo.findBy(1);
	}

	/**
	 * @return the rollingMetadata
	 */
	@ViewScoped
	@Produces @RollingMetadataQualifer
	@Named("rollingMetadata")
	public RollingMetadata getRollingMetadata() {
		return rollingMetadata;
	}
}
