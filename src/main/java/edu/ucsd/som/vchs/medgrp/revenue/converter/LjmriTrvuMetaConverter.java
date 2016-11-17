/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvuMeta;

/**
 * @author myebba
 *
 */
@ViewScoped
@ManagedBean(name="ljmriTrvuMetaConverter")
@FacesConverter(value="ljmriTrvuMetaConverter")
public class LjmriTrvuMetaConverter extends SelectItemsBaseConverter {

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((LjmriTrvuMeta) value).getId().toString();
	}
}
