package edu.ucsd.som.vchs.medgrp.revenue.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import edu.ucsd.som.vchs.converter.helper.ViewCachedAutoCompleteHelper;
import edu.ucsd.som.vchs.medgrp.revenue.controller.MedGrpAddProviderView;

@ViewScoped
@ManagedBean(name="addProviderProviderConverter")
@FacesConverter(value="addProviderProviderConverter")
public class AddProviderProviderConverter implements Converter {
	@ManagedProperty("#{addProviderView}")
	MedGrpAddProviderView addProviderView;
	
	@Inject
	ViewCachedAutoCompleteHelper helper;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return helper.convertToObjectValue(value, addProviderView.getProviderAutoCompleteResults());
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value == null) ? null : value.toString();
	}

	public MedGrpAddProviderView getAddProviderView() {
		return addProviderView;
	}

	public void setAddProviderView(MedGrpAddProviderView addProviderView) {
		this.addProviderView = addProviderView;
	}
}