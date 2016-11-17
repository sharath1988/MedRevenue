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
import edu.ucsd.som.vchs.medgrp.revenue.controller.MedGrpAddFutureEmployeeView;

@ViewScoped
@ManagedBean(name="addFutureEmployeeDepartmentConverter")
@FacesConverter(value="addFutureEmployeeDepartmentConverter")
public class AddFutureEmployeeDepartmentConverter implements Converter {
	@ManagedProperty("#{addFutureEmployeeView}")
	MedGrpAddFutureEmployeeView addFutureEmployee;
	
	@Inject
	ViewCachedAutoCompleteHelper helper;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return helper.convertToObjectValue(value, addFutureEmployee.getDepartmentAutoCompleteResults());
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value == null) ? null : value.toString();
	}

	/**
	 * @return the addFutureEmployee
	 */
	public MedGrpAddFutureEmployeeView getAddFutureEmployee() {
		return addFutureEmployee;
	}

	/**
	 * @param addFutureEmployee the addFutureEmployee to set
	 */
	public void setAddFutureEmployee(MedGrpAddFutureEmployeeView addFutureEmployee) {
		this.addFutureEmployee = addFutureEmployee;
	}
}