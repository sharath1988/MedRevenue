package edu.ucsd.som.vchs.medgrp.revenue.webutil;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named
public class AppInfo implements Serializable {
	private static final long serialVersionUID = 4584696232615086448L;
	
	private static final String PROD_SERVER = "som-prod4.ucsd.edu";
	
	@Inject
	private FacesContext facesContext;
	
	private boolean development = false;
	private boolean qa = false;
	private boolean localhost = false;
	private String hostname = null;

	public AppInfo() {}
	
	@PostConstruct
	public void init() {
		Map<String, String> headers = facesContext.getExternalContext().getRequestHeaderMap();
		String host = headers.get("HOST");
		if (host != null) {
			setHostname(host);
			if (host.toLowerCase().contains("localhost")) {
				localhost = true;
			}
			if (localhost || host.toLowerCase().contains("som-dev")) {
				development = true;
			} else if (host.toLowerCase().contains("som-qa")) {
				qa = true;
			}
		} else {
			setHostname(PROD_SERVER);
		}
	}

	public boolean isDevelopment() {
		return development;
	}

	public void setDevelopment(boolean development) {
		this.development = development;
	}

	public boolean isQa() {
		return qa;
	}

	public void setQa(boolean qa) {
		this.qa = qa;
	}

	public boolean isLocalhost() {
		return localhost;
	}

	public void setLocalhost(boolean localhost) {
		this.localhost = localhost;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getWebServiceServer() {
		if (isDevelopment()) {
			return hostname;
		} else {
			return PROD_SERVER;
		}
	}
	
	public String getLogoutRedirect() {
		if (localhost) {
			// No SSO; just redirect to SOM home page
			return "http://som.ucsd.edu/";
		} else {
			return "http://" +
					hostname +
					"/Shibboleth.sso/Logout?return=https://a4.ucsd.edu/tritON/logout?target=http://som.ucsd.edu/";
		}
	}
	
	public boolean isTestServer() {
		return (development || qa);
	}
	
	public boolean keepAlive() {
		// dummy method
		return true;
	}

	/**
	 * Method used by the idle timeout monitor when OK is clicked	
	 */
	public void welcomeListener() {
	    FacesContext.getCurrentInstance().addMessage(
		null,
		new FacesMessage(FacesMessage.SEVERITY_WARN, "Session Restored",
			"Your session will remain active"));
 
	}

	/**
	 * Method used by the idle timeout monitor when LogMeOut is clicked
	 */
	public void logoutListener() {
	    FacesContext.getCurrentInstance().addMessage(
		null,
		new FacesMessage(FacesMessage.SEVERITY_WARN,
			"You Have Logged Out!",
			"Thank you for using [YOUR APP NAME HERE]"));
 
		// invalidate session, and redirect to other pages
	    FacesContext.getCurrentInstance().getExternalContext()
        .invalidateSession();
		try {
			FacesContext.getCurrentInstance().getExternalContext()
			        .redirect(getLogoutRedirect());
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	}	
}
