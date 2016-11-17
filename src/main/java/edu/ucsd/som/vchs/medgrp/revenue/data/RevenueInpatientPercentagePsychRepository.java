/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueInpatientPercentagePsych;

/**
 * DAO for displaying inpatient percentages
 * 
 * @author somdev5
 *
 */
@Repository
public interface RevenueInpatientPercentagePsychRepository extends EntityRepository<RevenueInpatientPercentagePsych, Integer> {

}
