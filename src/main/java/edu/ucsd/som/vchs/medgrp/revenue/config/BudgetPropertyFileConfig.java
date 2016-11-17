package edu.ucsd.som.vchs.medgrp.revenue.config;

import javax.faces.bean.ApplicationScoped;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

@ApplicationScoped
public class BudgetPropertyFileConfig implements PropertyFileConfig {
	private static final long serialVersionUID = -6558477149454823089L;

	@Override
	public String getPropertyFileName() {
		return "Budget.properties";
	}
}
