package edu.ucsd.som.vchs.medgrp.revenue.util;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import edu.ucsd.som.vchs.medgrp.revenue.webutil.AppInfo;

@ViewScoped
@ManagedBean(name="sessionTimeout")
public class SessionTimeout {
	@ManagedProperty(value="#{session}")
	HttpSession session;
	
	@ManagedProperty(value="#{appInfo}")
	AppInfo appInfo;
	
	@Inject
	@ConfigProperty(name="session.timeout.notice",defaultValue="30")
	Integer warningInSeconds;
	
	private static final Long MILLISECONDS_IN_A_SECOND = 1000L;
	
	public void keepAlive() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Session Restored", "Your session will remain active"));
	}
	
	public Long getTimeout() {
		return session.getMaxInactiveInterval() * MILLISECONDS_IN_A_SECOND;
	}
	
	public Long getWarning() {
		return warningInSeconds * MILLISECONDS_IN_A_SECOND;
	}
	
	public void logout() {
	    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Logged out","You have logged out!"));
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(appInfo.getLogoutRedirect());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}

	/**
	 * @return the appInfo
	 */
	public AppInfo getAppInfo() {
		return appInfo;
	}

	/**
	 * @param appInfo the appInfo to set
	 */
	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}
}
