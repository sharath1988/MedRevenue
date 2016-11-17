/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.omnifaces.util.selectitems.SelectItemsUtils;

/**
 * @author myebba
 *
 */
public abstract class SelectItemsBaseConverter implements Converter {

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		 return SelectItemsUtils.findValueByStringConversion(context, component, value, this);
	}
}
