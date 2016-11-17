/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import edu.ucsd.som.vchs.medgrp.revenue.controller.LjmriTrvuView;
import edu.ucsd.som.vchs.medgrp.revenue.data.LjmriTrvuMetaRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.LjmriTrvuRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvu;
import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvuMeta;
import edu.ucsd.som.vchs.medgrp.revenue.util.excel.Exportable;

/**
 * @author myebba
 *
 */
public class LjmriTrvuServiceImpl implements LjmriTrvuService {

	@Inject
	private LjmriTrvuRepository repo;
	
	@Inject
	private LjmriTrvuMetaRepository metaRepo;
	
	
	/* (non-Javadoc)
	 * @see edu.ucsd.som.vchs.medgrp.revenue.service.LjmriTrvuService#list()
	 */
	@Override
	public List<LjmriTrvu> list() {
		return repo.findAllOrderByDateDesc();
	}
	
	/* (non-Javadoc)
	 * @see edu.ucsd.som.vchs.medgrp.revenue.service.LjmriTrvuService#save(edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvu)
	 */
	@Override
	public LjmriTrvu save(LjmriTrvu entity) {
		return repo.save(entity);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.ucsd.som.vchs.medgrp.revenue.service.LjmriTrvuService#getMetaList()
	 */
	@Override
	public List<LjmriTrvuMeta> getMetaList() {
		return metaRepo.findAllOrderByDivisionId();
	}

	@Override
	public List<LjmriTrvu> getYearToDate(Date thisYear) {
		return repo.findYearToDate(thisYear);
	}
	
	@Override
	public Exportable getExcelExportable(LjmriTrvuView ljmriTrvuView) {
		
		//ExcelWorkbook workbook = excelUtil.createWorkbook();
		//ExcelSheet sheet = excelUtil.createSheet(workbook,"LJMRI_TRVU");
		//formatWorkbook(workbook);
		//Exportable exportable = new Exportable();
		//exportable.setFilename("ljMriTrvu");
		//exportable.setWorkbook(workbook.getPoiWorkbook());
		//return exportable;
		return null;
	}
	
	
}
