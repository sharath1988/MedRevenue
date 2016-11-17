package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

import edu.ucsd.som.vchs.medgrp.revenue.data.AllEmployeeViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueProviderMergeRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetDescRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueWorksheetRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployeeView;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueProviderMerge;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueProviderMergePK;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheet;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetDesc;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;

public class ProviderServiceImpl implements ProviderService {
	@Inject
	private Log log;
	
	@Inject
	AllEmployeeViewRepository allEmployeeRepo;
	
	@Inject
	RevenueWorksheetDescRepository revWorksheetDescRepo;
	
	@Inject
	RevenueWorksheetRepository revWorksheetRepo;
	
	//@Inject
	//RevenueWorksheetViewRepository revWorksheetViewRepo;
	
	@Inject
	RevenueProviderMergeRepository revProviderMergeRepo;
	
	private static final Integer MAX_RESULTS = 10;
	
	@Override
	public void mergeProviders(List<RevenueWorksheetView> providersToMerge) throws Exception {
		// validate
		validateProvidersToMerge(providersToMerge);
		
		RevenueProviderMerge mergeEntity = assembleMergeEntity(providersToMerge);
		revProviderMergeRepo.saveAndFlush(mergeEntity);
		revProviderMergeRepo.callMergeProviderProc();
	}

	@Override
	public List<AllEmployeeView> getProvidersByQueryDivisionAndSos(String query, Integer divisionId, SiteOfService siteOfService) {
		log.info(String.format("Querying for provider.  Query: %s, DivisionId: %d, SiteOfService: %s",query,divisionId,siteOfService));
		return allEmployeeRepo.findAddableProviders(query, divisionId, siteOfService, MAX_RESULTS);
	}

	@Override
	public void addProviderToWorksheet(AllEmployeeView allEmployee, Integer divisionId, SiteOfService siteOfService, BigDecimal carePaymentRate) {
		Integer worksheetDescId = revWorksheetDescRepo.findDescIdByAllEmployeeId(allEmployee.getAllEmployeeId());
		if (worksheetDescId != null) {
			insertExistingDescIntoWorksheet(worksheetDescId,divisionId,siteOfService, carePaymentRate);
		} else {
			insertNewDescIntoWorksheet(allEmployee,divisionId,siteOfService, carePaymentRate);
		}
	}

	private void insertExistingDescIntoWorksheet(Integer worksheetDescId, Integer divisionId, SiteOfService siteOfService, BigDecimal carePaymentRate) {
		RevenueWorksheet rw = new RevenueWorksheet();
		rw.setDivisionId(divisionId);
		rw.setProjWrvus(BigDecimal.ZERO);
		rw.setSos(siteOfService.toString());
		rw.setWorksheetDescId(worksheetDescId);
		rw.setCarePaymentRate(carePaymentRate);
		rw = revWorksheetRepo.save(rw);
	}
	
	private void insertNewDescIntoWorksheet(AllEmployeeView allEmployee, Integer divisionId, SiteOfService siteOfService, BigDecimal carePaymentRate) {
		RevenueWorksheetDesc rwd = new RevenueWorksheetDesc();
		rwd.setAllEmployeeId(allEmployee.getAllEmployeeId());
		rwd.setReferenceNumber(allEmployee.getEmployeeReferenceId());
		rwd.setDescription(allEmployee.getEmployeeFullname());
		rwd = revWorksheetDescRepo.save(rwd);
		insertExistingDescIntoWorksheet(rwd.getWorksheetDescId(), divisionId, siteOfService, carePaymentRate);
	}
	
	private Integer getSourceEin(List<RevenueWorksheetView> providersToMerge) {
		for (RevenueWorksheetView wv : providersToMerge) {
			Integer sourceEin = wv.getEmployeeUcsdId();
			if (StringUtils.startsWith(sourceEin.toString(), "99")) {
				return sourceEin;
			}
		}
		throw new IllegalStateException("Cannot find a source provider to merge with a 99* employee ucsd id");
	}
	
	private Integer getTargetEin(List<RevenueWorksheetView> providersToMerge) {
		for (RevenueWorksheetView wv : providersToMerge) {
			Integer targetEin = wv.getEmployeeUcsdId();
			if (!StringUtils.startsWith(targetEin.toString(), "99")) {
				return targetEin;
			}
		}
		throw new IllegalStateException("Cannot find a target provider to merge with a legit employee ucsd id");
	}
	
	private Integer getDivisionId(List<RevenueWorksheetView> providersToMerge) {
		return providersToMerge.get(0).getDivisionId();
	}
	
	private RevenueProviderMerge assembleMergeEntity(List<RevenueWorksheetView> providersToMerge) {
		// get source
		Integer sourceEin = getSourceEin(providersToMerge);
		
		// get target
		Integer targetEin = getTargetEin(providersToMerge);
		
		// get division
		Integer divisionId = getDivisionId(providersToMerge);
		
		RevenueProviderMergePK pk = new RevenueProviderMergePK();
		pk.setSourceEin(sourceEin);
		pk.setTargetEin(targetEin);
		pk.setDivisionId(divisionId);
		
		RevenueProviderMerge entity = new RevenueProviderMerge();
		entity.setId(pk);
		return entity;
	}
	/*
	 * Validate the two providers selected to be merged
	 */
	private void validateProvidersToMerge(List<RevenueWorksheetView> providersToMerge) throws Exception {
		
		// Check if they have selected two
		if (providersToMerge.size() != 2) {
			throw new Exception("You must select exactly two providers to merge.");
		}
		
		// check if fullnames are alike
		String e1 = providersToMerge.get(0).getEmployeeFullName();
		String e2 = providersToMerge.get(1).getEmployeeFullName();
		
		int matches = 0;
		if (e1.length() > e2.length()) {
			matches = StringUtils.countMatches(e1, e2); 	
		} else {
			matches = StringUtils.countMatches(e2, e1);
		}
		
		if (matches < 1) {
			throw new Exception("The providers you are attempting to merge dont appear to be the same person.");
		}
	}
}