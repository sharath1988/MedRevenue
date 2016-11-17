package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.Date;
import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvu;

@Repository
public interface LjmriTrvuRepository extends EntityRepository<LjmriTrvu,Integer> {
	
	@Query("SELECT lj FROM LjmriTrvu lj ORDER BY date DESC")
	public List<LjmriTrvu> findAllOrderByDateDesc();

	@Query("SELECT lj FROM LjmriTrvu lj WHERE lj.date >= :thisYear ORDER BY lj.date DESC")
	public List<LjmriTrvu> findYearToDate(@QueryParam("thisYear") Date thisYear);
}