/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueInpatientPercentage;

/**
 * DAO for displaying inpatient percentages
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenueInpatientPercentageRepository extends EntityRepository<RevenueInpatientPercentage, Integer> {

}
