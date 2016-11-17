package edu.ucsd.som.vchs.medgrp.revenue.exception;

public class BudgetConfigurationException extends RuntimeException {
	private static final long serialVersionUID = 6472894024392220029L;

	public BudgetConfigurationException() {}

	public BudgetConfigurationException(String message) {
		super(message);
	}

	public BudgetConfigurationException(Throwable cause) {
		super(cause);
	}

	public BudgetConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public BudgetConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
