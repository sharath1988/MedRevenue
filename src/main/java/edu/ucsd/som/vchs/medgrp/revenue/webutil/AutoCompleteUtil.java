/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.webutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanPredicate;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

/**
 * Utility method for autocompletes tied to filter boxes
 * 
 * @author somdev5
 *
 */
@Named("autoCompleteUtil")
public class AutoCompleteUtil {

	@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject
	private Log log;
	
	/**
	 * Select a distinct property name from an input collection
	 * 
	 * @param inputCollection
	 * @param propertyName
	 * @param inputQuery
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<String> selectDistinctPropertyFromList(Collection inputCollection, String propertyName, String inputQuery) {
		
		List<String> results = new ArrayList<String>();
		
		try {		
			Collection objectList = CollectionUtils.select(inputCollection, 
					new BeanPredicate(propertyName, PredicateUtils.uniquePredicate()));
					
			for (Object obj : objectList) {
				String propertyValue = BeanUtils.getProperty(obj, propertyName);
				if (StringUtils.contains(propertyValue.toLowerCase(), inputQuery.toLowerCase())) {
					results.add(propertyValue);
				}			
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return results;		
	}
}
