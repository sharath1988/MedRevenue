/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.RollingMetadata;

/**
 * Repository for rolling 12 month metadata 
 * 
 * @author somdev5
 *
 */
@Repository
public interface RollingMetadataRepository extends EntityRepository<RollingMetadata, Integer> {

}
