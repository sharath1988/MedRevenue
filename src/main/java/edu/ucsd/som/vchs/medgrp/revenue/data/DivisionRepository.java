/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.data;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.Division;

/**
 * Repository needed to obtain division info
 * @author somdev5
 *
 */
@Repository
public interface DivisionRepository extends EntityRepository<Division, Integer> {

}
