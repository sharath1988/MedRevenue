/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import edu.ucsd.som.vchs.medgrp.revenue.util.excel.Exportable;
import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvu;
import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvuMeta;
import edu.ucsd.som.vchs.medgrp.revenue.service.LjmriTrvuService;

/**
 * @author myebba
 *
 */
@ViewScoped
@ManagedBean(name = "ljmriTrvuView")
public class LjmriTrvuView {

	@Inject
	LjmriTrvuService service;

	@Inject
	FacesContext facesContext;

	@Inject
	private Log log;

	@Inject
	MenuBean menuBean;

	private boolean mriMenu;

	List<LjmriTrvu> list;

	List<LjmriTrvuMeta> metaList;

	LjmriTrvu newRow;
	
	private Date fiscalStart;
	

	BigDecimal totalCharges = new BigDecimal(0);
	BigDecimal totalCollections = new BigDecimal(0);
	BigDecimal total_tRVUS = new BigDecimal(0);
	BigDecimal total_wRVUs = new BigDecimal(0);
	int size = 0;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void load() {
		list = service.list();
		metaList = service.getMetaList();
		newRow = new LjmriTrvu();
	}
	
	/**
	 * This method returns the correct start of the current
	 * fiscal year. Always, june 1 of the appropriate year
	 * @param cal an instance of the current calendar
	 * @return the start of the fiscal year
	 */
	private int getStartFiscalYear(Calendar cal) {
		int month = cal.get(Calendar.MONTH);
		if (month < Calendar.JULY) {
			return cal.get(Calendar.YEAR) - 1;
		}
		return cal.get(Calendar.YEAR) ;
	}


	/**
	 * This method gets called from the ytd.xhtml metaview
	 * It gets the beginning of the fiscal year and 
	 * then fetches all data from the start of the current
	 * fiscal year to the present. It stuffs that result set
	 * into the list that is used by the datatable.
	 */
	public void ytd() {
		Calendar cal = Calendar.getInstance();
		int startFiscalYear = getStartFiscalYear(cal);
		cal.set(Calendar.YEAR, startFiscalYear);
		cal.set(Calendar.MONTH, Calendar.JULY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		setFiscalStart(cal.getTime());
		
		list = service.getYearToDate(fiscalStart);
		size = list.size();
		
		for(LjmriTrvu ljmriTrvu : list){
			totalCharges = totalCharges.add(ljmriTrvu.getCharges());
		    totalCollections = totalCollections.add(ljmriTrvu.getFFSPayments());
			total_tRVUS = total_tRVUS.add(ljmriTrvu.gettRVU());
			total_wRVUs = total_wRVUs.add(ljmriTrvu.getwRVU());
		}
		
	}
	
	public String getCurrency(BigDecimal amt){
		
		NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        Currency currency = Currency.getInstance("USD");
        format.setCurrency(currency);

		return format.format(amt);
	}
	

	public void update_onBlur(AjaxBehaviorEvent e) {
		log.info("updating field");
		Map<String, Object> attr = e.getComponent().getAttributes();
		LjmriTrvu lj = (LjmriTrvu) attr.get("lj");
		service.save(lj);
		load();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "LJRMI TRVU Modification",
				"Amounts updated successfully"));
	}

	public void addNew_onClick() {
		//log.info("Adding new record");
		assembleMeta();
		service.save(getNewRow());
		//log.info(getNewRow().getDate());
		load();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "LJRMI TRVU Addition",
				"Amounts added successfully"));
	}

	private void assembleMeta() {
		LjmriTrvuMeta meta = getNewRow().getMeta();
		getNewRow().setDepartment(meta.getDepartment());
		getNewRow().setDivision(meta.getDivision());
		getNewRow().setDivisionId(meta.getDivisionId());
		getNewRow().setIndx(meta.getIndx());
		getNewRow().setSomMajorGroup(meta.getSomMajorGroup());
		getNewRow().setSomMajorGroupId(meta.getSomMajorGroupId());
	}
	
	public BigDecimal getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(BigDecimal totalCharges) {
		this.totalCharges = totalCharges;
	}

	public BigDecimal getTotalCollections() {
		return totalCollections;
	}

	public void setTotalCollections(BigDecimal totalCollections) {
		this.totalCollections = totalCollections;
	}

	public BigDecimal getTotal_tRVUS() {
		return total_tRVUS;
	}

	public void setTotal_tRVUS(BigDecimal total_tRVUS) {
		this.total_tRVUS = total_tRVUS;
	}

	public BigDecimal getTotal_wRVUs() {
		return total_wRVUs;
	}

	public void setTotal_wRVUs(BigDecimal total_wRVUs) {
		this.total_wRVUs = total_wRVUs;
	}
	
	public List<LjmriTrvu> getList() {
		return list;
	}

	public void setList(List<LjmriTrvu> list) {
		this.list = list;
	}

	public List<LjmriTrvuMeta> getMetaList() {
		return metaList;
	}

	public void setMetaList(List<LjmriTrvuMeta> metaList) {
		this.metaList = metaList;
	}

	public LjmriTrvu getNewRow() {
		return newRow;
	}

	public void setNewRow(LjmriTrvu newRow) {
		this.newRow = newRow;
	}

	public boolean isMriMenu() {
		return mriMenu;
	}

	public void setMriMenu(boolean mriMenu) {
		this.mriMenu = mriMenu;
	}
	
	
	private StreamedContent streamedContent(Exportable exportable) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		exportable.write(outputStream);
		InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		outputStream.close();
		return new DefaultStreamedContent(inputStream,exportable.getContentType(),exportable.getFilename());
	}

	
	public StreamedContent excelExport() throws IOException {
		return streamedContent(service.getExcelExportable(this));
	}

	public Date getFiscalStart() {
		return fiscalStart;
	}

	public void setFiscalStart(Date fiscalStart) {
		this.fiscalStart = fiscalStart;
	}
	


}