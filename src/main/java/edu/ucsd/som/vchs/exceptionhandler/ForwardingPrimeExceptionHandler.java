package edu.ucsd.som.vchs.exceptionhandler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.primefaces.application.exceptionhandler.ExceptionInfo;
import org.primefaces.application.exceptionhandler.PrimeExceptionHandler;
import org.primefaces.context.RequestContext;

/**
 * @author somdev9
 * 
 * This class is a modified version of the standard PrimeExceptionHandler.  Parent methods are used where possible and
 * the goal and intent of this class is to use the exact same error page framework as PrimeFaces, but using forwards instead
 * of redirects to the error pages and adding a UUID to allow developers to have a key with which to search the log files
 * if there are issues in a different environment.
 *
 */
public class ForwardingPrimeExceptionHandler extends PrimeExceptionHandler {
	private static final String GENERATING_ERROR_PAGE = "CurrentlyGeneratingErrorPage";

	public ForwardingPrimeExceptionHandler(ExceptionHandler wrapped) {
		super(wrapped);
	}
	
	// Logger used here mirrors that of the PrimeExceptionHandler super class.
	private static final Logger LOG = Logger.getLogger(ForwardingPrimeExceptionHandler.class.getName());

	/* (non-Javadoc)
	 * @see org.primefaces.application.exceptionhandler.PrimeExceptionHandler#handleRedirect(javax.faces.context.FacesContext, java.lang.Throwable, org.primefaces.application.exceptionhandler.ExceptionInfo)
	 */
	@Override
	protected void handleRedirect(FacesContext context, Throwable rootCause, ExceptionInfo info, boolean bool) throws IOException {
		HttpServletRequest request = ((HttpServletRequest)context.getExternalContext().getRequest());
		String uuid = UUID.randomUUID().toString();
		LOG.log(Level.SEVERE,String.format("ERROR: UUID=%s",uuid),info.getException());
		
		Map<String, String> errorPages = RequestContext.getCurrentInstance().getApplicationContext().getConfig().getErrorPages();
		// get error page by exception type
		String errorPage = errorPages.get(rootCause.getClass().getName());
		// get default error page
		if (errorPage == null) {
		    errorPage = errorPages.get(null);
		}
		if (errorPage == null) {
		    throw new IllegalArgumentException("No default error page (Status 500 or java.lang.Throwable) and no error page for type \"" + rootCause.getClass() + "\" defined!");
		}
		if (request.getAttribute(GENERATING_ERROR_PAGE) != null) {
			throw new RuntimeException(String.format("Exception thrown generating error page for %s.  Verify that the error page for this exception type is syntactically correct.",rootCause.getClass().toString()));
		}
		request.setAttribute("exception", info);
		request.setAttribute("uuid", uuid);
		request.setAttribute(GENERATING_ERROR_PAGE,true);
		// TODO: Add an interface to allow mapping between status codes and error types.  Currently, a status 500 is the only thing returned from an error.
		context.getExternalContext().setResponseStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		context.getExternalContext().dispatch(errorPage);
	}	
}
