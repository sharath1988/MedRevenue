package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.OtherIncome;

@Repository
public interface OtherIncomeRepository extends EntityRepository<OtherIncome,Integer> {
	@Query("select oi.coPayment + oi.incentive + oi.membership + oi.stip + oi.other from OtherIncome oi where divisionId = ?1 group by oi.divisionId")
	public Integer getOtherIncomeTotal(Integer divisionId);
}