package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.util.Date;
import java.util.List;

import edu.ucsd.som.vchs.medgrp.revenue.controller.LjmriTrvuView;
import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvu;
import edu.ucsd.som.vchs.medgrp.revenue.model.LjmriTrvuMeta;
import edu.ucsd.som.vchs.medgrp.revenue.util.excel.Exportable;

public interface LjmriTrvuService {

	public abstract List<LjmriTrvu> list();

	public abstract LjmriTrvu save(LjmriTrvu entity);
	
	public abstract List<LjmriTrvuMeta> getMetaList();
	
	public abstract List<LjmriTrvu> getYearToDate(Date thisYear);

	public abstract Exportable getExcelExportable(LjmriTrvuView ljmriTrvuView);

}

