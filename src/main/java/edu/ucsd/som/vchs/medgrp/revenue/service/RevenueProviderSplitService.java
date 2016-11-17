/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueDivisionSplitRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueProviderSplitRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueDivisionSplit;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueProviderSplit;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;

/**
 * Service class for RevenueProviderSplit objects
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@Named
public class RevenueProviderSplitService implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1477804217109804244L;
	
	@Inject
	private RevenueProviderSplitRepository revenueProviderSplitRepository;
	
	@Inject
	private RevenueDivisionSplitRepository revenueDivisionSplitRepository;

	/**
	 * Default constructor
	 */
	public RevenueProviderSplitService() {
		super();
	}
	
	/**
	 * Adapter method for findByDivisionId
	 * 
	 * @param divisionId
	 * @return
	 */
	public List<RevenueProviderSplit> findByDivisionId(Integer divisionId) {
		return revenueProviderSplitRepository.findByDivisionId(divisionId);
	}

	/**
	 * Returns division split percentages for the specified division
	 * 
	 * @param divisionId
	 * @return
	 */
	public RevenueDivisionSplit getDivisionSplits(Integer divisionId) {
		return revenueDivisionSplitRepository.findByDivisionIdAndSos(divisionId, SiteOfService.BOTH.toString());
	}
}
