package edu.ucsd.som.vchs.medgrp.revenue.test.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.IsBudgetAdmin;
import edu.ucsd.som.vchs.medgrp.revenue.annotation.LoggedInUcsdId;

/**
 * Test Producer for JUNIT
 * 
 * @author somdev5
 *
 */
public class TestResources {
    
    @Produces
    public Log produceLog(InjectionPoint injectionPoint) {
    	return LogFactory.getLog(injectionPoint.getMember().getDeclaringClass().getName());
    }
        
    @Produces
    @LoggedInUcsdId
    public Integer getLoggedInUcsdId() {
    	System.out.println("Using TestResources.java");
    	return new Integer("209166");
    }
    
    @Produces
    @IsBudgetAdmin
    public Boolean getIsBudgetAdmin() {
    	return Boolean.TRUE;
    }
}
