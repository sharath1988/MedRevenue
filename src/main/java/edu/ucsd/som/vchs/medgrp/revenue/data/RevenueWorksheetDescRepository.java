package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueWorksheetDesc;

@Repository
public interface RevenueWorksheetDescRepository extends EntityRepository<RevenueWorksheetDesc,Integer>{
	@Query(named = RevenueWorksheetDesc.FIND_ID_BY_ALL_EMPLOYEE_ID, singleResult = SingleResultType.OPTIONAL)
	Integer findDescIdByAllEmployeeId(Integer allEmployeeId);
}
