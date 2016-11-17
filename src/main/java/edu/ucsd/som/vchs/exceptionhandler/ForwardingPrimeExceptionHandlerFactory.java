package edu.ucsd.som.vchs.exceptionhandler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.primefaces.application.exceptionhandler.PrimeExceptionHandlerFactory;

public class ForwardingPrimeExceptionHandlerFactory extends PrimeExceptionHandlerFactory {

	public ForwardingPrimeExceptionHandlerFactory(ExceptionHandlerFactory wrapped) {
		super(wrapped);
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return new ForwardingPrimeExceptionHandler(getWrapped().getExceptionHandler());
	}
}
