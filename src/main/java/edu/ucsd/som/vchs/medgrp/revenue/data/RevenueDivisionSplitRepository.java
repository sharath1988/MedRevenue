/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueDivisionSplit;

/**
 * Repository DAO for division split percentage totals
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenueDivisionSplitRepository extends EntityRepository<RevenueDivisionSplit, Integer> {

	public RevenueDivisionSplit findByDivisionId(Integer divisionId);
	
	public RevenueDivisionSplit findByDivisionIdAndSos(Integer divisionId, String sos);
}
