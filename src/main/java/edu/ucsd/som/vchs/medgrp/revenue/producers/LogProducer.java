package edu.ucsd.som.vchs.medgrp.revenue.producers;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsd.som.vchs.medgrp.revenue.annotation.SomLog;

public class LogProducer {
	@Produces @SomLog
	public Log produceLog(InjectionPoint injectionPoint) {
		return LogFactory.getLog(injectionPoint.getMember().getDeclaringClass().getName());
	}
}
