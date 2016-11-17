package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.model.AllEmployeeView;
import edu.ucsd.som.vchs.medgrp.revenue.model.Department;
import edu.ucsd.som.vchs.medgrp.revenue.model.FutureEmployee;
import edu.ucsd.som.vchs.medgrp.revenue.model.FutureEmployeeType;
import edu.ucsd.som.vchs.medgrp.revenue.model.SiteOfService;
import edu.ucsd.som.vchs.medgrp.revenue.model.faculty.CompPlan;
import edu.ucsd.som.vchs.medgrp.revenue.model.faculty.Degree;
import edu.ucsd.som.vchs.medgrp.revenue.model.faculty.Rank;
import edu.ucsd.som.vchs.medgrp.revenue.model.faculty.Step;
import edu.ucsd.som.vchs.medgrp.revenue.service.FutureEmployeeService;
import edu.ucsd.som.vchs.medgrp.revenue.service.ProviderService;

@ViewScoped
@ManagedBean(name="addFutureEmployeeView")
public class MedGrpAddFutureEmployeeView {
	private Integer divisionId;
	private FutureEmployee futureEmployee = new FutureEmployee();
	private String firstName;
	private String middleName;
	private String lastName;
	private Department department;
	private SiteOfService siteOfService = SiteOfService.BOTH;
	private BigDecimal carePaymentRate = BigDecimal.ZERO;			
	
	private final SiteOfService[] siteOfServiceValues = SiteOfService.values();
	private final FutureEmployeeType[] futureEmployeeTypes = FutureEmployeeType.values();
	private final Degree[] degreeValues = Degree.values();
	private final CompPlan[] compPlanValues = CompPlan.values();
	private final Rank[] rankValues = Rank.values();
	private final Step[] stepValues = Step.values();
	
	private List<Department> departmentAutoCompleteResults;
	
	@ManagedProperty(value="#{medGrpRevenueWorksheetView}")
	private MedGrpRevenueWorksheetView revenueWorksheetView;
	
	@ManagedProperty(value="#{divisionInfoBean}")
	private DivisionInfoBean divisionInfoBean;
	
	@Inject
	private FutureEmployeeService futureEmployeeService;
	
	@Inject
	private ProviderService providerService;
	
	@Inject
	private FacesContext facesContext;
	
	public List<Department> departmentAutoComplete(String query) {
		departmentAutoCompleteResults = futureEmployeeService.getDepartmentsByNameLike(query);
		return departmentAutoCompleteResults;
	}
	
	public Boolean getShowFacultyFields() {
		return FutureEmployeeType.FACULTY.equals(futureEmployee.getFutureEmployeeType());
	}
	
	public void add() {
		futureEmployee.setName(name());
		futureEmployee.setHomeDepartmentCode(homeDepartmentCode());
		AllEmployeeView provider = futureEmployeeService.addFutureEmployeeAsProvider(futureEmployee);
		addProvider(provider);
		siteOfService = SiteOfService.BOTH;
		futureEmployee = new FutureEmployee();
		firstName = middleName = lastName = "";
		carePaymentRate = BigDecimal.ZERO;
		department = null;
	}
	
	private void addProvider(AllEmployeeView provider) {
		if (siteOfService.equals(SiteOfService.BOTH) && divisionInfoBean.getDivisionInfo().getIsPsychiatry()) {
			providerService.addProviderToWorksheet(provider, divisionId, SiteOfService.INPATIENT, carePaymentRate);
			providerService.addProviderToWorksheet(provider, divisionId, SiteOfService.OUTPATIENT, carePaymentRate);
		} else {
			providerService.addProviderToWorksheet(provider, divisionId, siteOfService, carePaymentRate);
		}
		revenueWorksheetView.refreshProviderTotals();
    	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Worksheet Modification", "Future employee added successfully."));
    	siteOfService = SiteOfService.BOTH;
	}
	
	private String homeDepartmentCode() {
		return department.getDepartmentCode();
	}
	
	private String name() {
		StringBuilder sb = new StringBuilder();
		sb.append(lastName);
		sb.append(", ");
		sb.append(firstName);
		if (middleName != null) {
			sb.append(" ");
			sb.append(middleName);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * @return the divisionId
	 */
	public Integer getDivisionId() {
		return divisionId;
	}

	/**
	 * @param divisionId the divisionId to set
	 */
	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	/**
	 * @return the futureEmployee
	 */
	public FutureEmployee getFutureEmployee() {
		return futureEmployee;
	}

	/**
	 * @param futureEmployee the futureEmployee to set
	 */
	public void setFutureEmployee(FutureEmployee futureEmployee) {
		this.futureEmployee = futureEmployee;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}

	/**
	 * @return the siteOfService
	 */
	public SiteOfService getSiteOfService() {
		return siteOfService;
	}

	/**
	 * @param siteOfService the siteOfService to set
	 */
	public void setSiteOfService(SiteOfService siteOfService) {
		this.siteOfService = siteOfService;
	}
	
	/**
	 * @return the carePaymentRate
	 */
	public BigDecimal getCarePaymentRate() {
		return carePaymentRate;
	}

	/**
	 * @param carePaymentRate the carePaymentRate to set
	 */
	public void setCarePaymentRate(BigDecimal carePaymentRate) {
		this.carePaymentRate = carePaymentRate;
	}

	/**
	 * @return the revenueWorksheetView
	 */
	public MedGrpRevenueWorksheetView getRevenueWorksheetView() {
		return revenueWorksheetView;
	}

	/**
	 * @param revenueWorksheetView the revenueWorksheetView to set
	 */
	public void setRevenueWorksheetView(
			MedGrpRevenueWorksheetView revenueWorksheetView) {
		this.revenueWorksheetView = revenueWorksheetView;
	}

	/**
	 * @return the divisionInfoBean
	 */
	public DivisionInfoBean getDivisionInfoBean() {
		return divisionInfoBean;
	}

	/**
	 * @param divisionInfoBean the divisionInfoBean to set
	 */
	public void setDivisionInfoBean(DivisionInfoBean divisionInfoBean) {
		this.divisionInfoBean = divisionInfoBean;
	}

	/**
	 * @return the facesContext
	 */
	public FacesContext getFacesContext() {
		return facesContext;
	}

	/**
	 * @param facesContext the facesContext to set
	 */
	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	/**
	 * @return the siteOfServiceValues
	 */
	public SiteOfService[] getSiteOfServiceValues() {
		return siteOfServiceValues;
	}

	/**
	 * @return the futureEmployeeTypes
	 */
	public FutureEmployeeType[] getFutureEmployeeTypes() {
		return futureEmployeeTypes;
	}

	/**
	 * @return the degreeValues
	 */
	public Degree[] getDegreeValues() {
		return degreeValues;
	}

	/**
	 * @return the compPlanValues
	 */
	public CompPlan[] getCompPlanValues() {
		return compPlanValues;
	}

	/**
	 * @return the rankValues
	 */
	public Rank[] getRankValues() {
		return rankValues;
	}

	/**
	 * @return the stepValues
	 */
	public Step[] getStepValues() {
		return stepValues;
	}

	/**
	 * @return the departmentAutoCompleteResults
	 */
	public List<Department> getDepartmentAutoCompleteResults() {
		return departmentAutoCompleteResults;
	}
}
