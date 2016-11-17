package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.math.BigDecimal;
import java.util.List;

import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployeeView;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetView;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;

public interface ProviderService {
	List<AllEmployeeView> getProvidersByQueryDivisionAndSos(String query, Integer divisionId, SiteOfService siteOfService);
	void addProviderToWorksheet(AllEmployeeView allEmployee, Integer divisionId, SiteOfService siteOfService, BigDecimal carePaymentRate);
	void mergeProviders(List<RevenueWorksheetView> providersToMerge) throws Exception;
}
