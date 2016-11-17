package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvuMeta;

@Repository
public interface LjmriTrvuMetaRepository extends EntityRepository<LjmriTrvuMeta,String> {
	
	@Query("SELECT lj FROM LjmriTrvuMeta lj ORDER BY divisionId")
	public List<LjmriTrvuMeta> findAllOrderByDivisionId();
}