package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.BillingAreaRate;

@Repository
public interface BillingAreaRateRepository extends EntityRepository<BillingAreaRate, Integer> {
	
	
	public List<BillingAreaRate> findByLoggedInUcsdId(Integer loggedInUcsdId);
}
