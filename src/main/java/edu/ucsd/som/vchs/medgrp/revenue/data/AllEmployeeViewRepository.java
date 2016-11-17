package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import javax.persistence.StoredProcedureQuery;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployeeView;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;

@Repository
public abstract class AllEmployeeViewRepository extends AbstractEntityRepository<AllEmployeeView,Integer> {
	public List<AllEmployeeView> findAddableProviders(String query, Integer divisionId, SiteOfService sos, Integer maxResults) {
		StoredProcedureQuery storedProcQuery = entityManager().createNamedStoredProcedureQuery(AllEmployeeView.FIND_ADDABLE_PROVIDERS_QUERY_NAME);
		storedProcQuery.setParameter("p_query", query);
		storedProcQuery.setParameter("p_division_id", divisionId);
		storedProcQuery.setParameter("p_sos", sos.toString());
		storedProcQuery.setParameter("p_limit", maxResults);
		storedProcQuery.execute();
		return (List<AllEmployeeView>)storedProcQuery.getResultList();
	}
}
