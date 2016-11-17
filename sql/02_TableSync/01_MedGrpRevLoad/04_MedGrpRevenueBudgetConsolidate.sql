drop procedure if exists budget.createConsolidatedAppTables
GO

create procedure budget.createConsolidatedAppTables()
BEGIN    
	/* prior year collections for revenue worksheet */
	drop table if exists budget.revenue_prior_actual_collections_per_trvus_by_prov;
	
	create table budget.revenue_prior_actual_collections_per_trvus_by_prov as
	select somDiv as division_id, worksheet_desc_id, 'INP/OUTP' as sos,
	cap_collections, under_collections, ffs_collections,
	(select cap_rate from budget.budgetMetadata) as cap_collections_to_trvus,
	ifnull(round((cap_collections / cap_trvus), 8), 0) as cap_collections_to_trvus_calculated,
	ifnull(round((under_collections / under_trvus), 8), 0) as under_collections_to_trvus_calculated,
	ifnull(round((ffs_collections / ffs_trvus), 8), 0) as ffs_collections_to_trvus_calculated
	from budget.revenue_prior_actual_cctw_by_category where somDiv <> 22
    group by worksheet_desc_id, somDiv
    union all
	select somDiv as division_id, worksheet_desc_id, sos,
	cap_collections, under_collections, ffs_collections,
	(select cap_rate from budget.budgetMetadata) as cap_collections_to_trvus,
	ifnull(round((cap_collections / cap_trvus), 8), 0) as cap_collections_to_trvus_calculated,
	ifnull(round((under_collections / under_trvus), 8), 0) as under_collections_to_trvus_calculated,
	ifnull(round((ffs_collections / ffs_trvus), 8), 0) as ffs_collections_to_trvus_calculated
	from budget.mv_revenue_prior_actuals_cctw_by_category_and_sos where somDiv = 22;
	
	alter table budget.revenue_prior_actual_collections_per_trvus_by_prov add column py_actual_rate_id int(11) auto_increment primary key;
	
	/* Prior year collections for payment information */
	drop table if exists budget.revenue_prior_actual_collections_per_trvus;

	
	create table budget.revenue_prior_actual_collections_per_trvus as
	select somDiv as division_id, sos,
	total_cap_collections, total_under_collections, total_ffs_collections,
	(select cap_rate from budget.budgetMetadata) as cap_collections_to_trvus,
	ifnull(round((total_cap_collections / total_cap_trvus), 2), 0) as cap_collections_to_trvus_calculated,
	ifnull(round((total_under_collections / total_under_trvus), 2), 0) as under_collections_to_trvus_calculated,
	ifnull(round((total_ffs_collections / total_ffs_trvus), 2), 0) as ffs_collections_to_trvus_calculated
	from budget.revenue_prior_actual_cctw_by_category_and_div;

	
	alter table budget.revenue_prior_actual_collections_per_trvus add column py_actual_rate_id int(11) auto_increment primary key;
	alter table budget.revenue_prior_actual_collections_per_trvus add index division_id (division_id);
	alter table budget.revenue_prior_actual_collections_per_trvus add index sos (sos);
	
	drop table if exists tmp_revenue_prior_actual_cctw_by_category_and_sos;
	
	create table tmp_revenue_prior_actual_cctw_by_category_and_sos as
	select 
	    worksheet_desc_id, somDiv, sos,
	    cap_charges, cap_collections, cap_trvus, cap_wrvus,
	    under_charges, under_collections, under_trvus, under_wrvus,
	    ffs_charges, ffs_collections, ffs_trvus, ffs_wrvus,
	    copay
	from mv_revenue_prior_actuals_cctw_by_category_and_sos where somDiv = 22
	union all
	select 
	    worksheet_desc_id, somDiv, 'INP/OUTP' as sos,
	    sum(cap_charges), sum(cap_collections), sum(cap_trvus), sum(cap_wrvus),
	    sum(under_charges), sum(under_collections), sum(under_trvus), sum(under_wrvus),
	    sum(ffs_charges), sum(ffs_collections), sum(ffs_trvus), sum(ffs_wrvus),
	    sum(copay)
	from mv_revenue_prior_actuals_cctw_by_category_and_sos where somDiv <> 22
	group by worksheet_desc_id, somDiv;	
	
    drop table if exists budget.medgrp_revenue_worksheet; /* Need to drop this due to FK constraint */
	drop table if exists budget.revenue_prior_actuals_by_prov;

	/**
	 * Landing table for Revenue budget exception template by provider
	 */
	CREATE TABLE budget.revenue_prior_actuals_by_prov (
      py_actual_id int(11) primary key auto_increment,
	  worksheet_desc_id int(11),
	  division_id int(11) NOT NULL,
	  cap_charges decimal(30,8) NULL,
	  under_charges decimal(30,8) NULL,
	  ffs_charges decimal(30,8) NULL,
	  total_charges decimal(30,8) NOT NULL,
	  cap_collections decimal(30,8) NULL,
	  under_collections decimal(30,8) NULL,
	  ffs_collections decimal(30,8) NULL,	  
	  total_collections decimal(30,8) NOT NULL,
	  cap_trvus decimal(30,8) NULL,
	  under_trvus decimal(30,8) NULL,
	  ffs_trvus decimal(30,8) NULL,	  
	  total_trvus decimal(30,8) NOT NULL,
	  cap_wrvus decimal(30,8) NULL,
	  under_wrvus decimal(30,8) NULL,
	  ffs_wrvus decimal(30,8) NULL,	  
	  total_wrvus decimal(30,8) NOT NULL,
	  copay decimal(30,8) NOT NULL,
	  sos varchar(64) NOT NULL,
	  cap_wrvu_split decimal(30,8) NOT NULL,
	  under_wrvu_split decimal(30,8) NOT NULL,
	  ffs_wrvu_split decimal(30,8) NOT NULL,
	  cap_collection_rate decimal(30,8) NOT NULL,
	  under_collection_rate decimal(30,8) NOT NULL,
	  ffs_collection_rate decimal(30,8) NOT NULL,
	  external_src_ref INT
	);

	
	/**
	 * Consolidate all prior year actuals by provider
	 */
	insert into budget.revenue_prior_actuals_by_prov (worksheet_desc_id, division_id,
        cap_charges, under_charges, ffs_charges, total_charges,
        cap_collections, under_collections, ffs_collections, total_collections, 
        cap_trvus, under_trvus, ffs_trvus, total_trvus,
        cap_wrvus, under_wrvus, ffs_wrvus, total_wrvus,
        copay, sos,
        cap_wrvu_split, under_wrvu_split, ffs_wrvu_split,
        cap_collection_rate, under_collection_rate, ffs_collection_rate)
	select 
	    cctw.worksheet_desc_id, cctw.somDiv, 
        tmp.cap_charges, tmp.under_charges, tmp.ffs_charges, cctw.charges, 
        tmp.cap_collections, tmp.under_collections, tmp.ffs_collections, cctw.collections, 
        tmp.cap_trvus, tmp.under_trvus, tmp.ffs_trvus, cctw.trvus, 
        tmp.cap_wrvus, tmp.under_wrvus, tmp.ffs_wrvus, cctw.wrvus, 
        cctw.copay, cctw.sos,
	    cat.perc_cap_wrvus, cat.perc_under_wrvus, cat.perc_ffs_wrvus,
	    rates.cap_collections_to_trvus_calculated, rates.under_collections_to_trvus_calculated, rates.ffs_collections_to_trvus_calculated
	from budget.mv_revenue_prior_actual_cctw_totals cctw
    join budget.tmp_revenue_prior_actual_cctw_by_category_and_sos tmp on tmp.worksheet_desc_id = cctw.worksheet_desc_id and cctw.sos = tmp.sos and cctw.somDiv = tmp.somDiv
	join budget.revenue_prior_actual_collections_per_trvus_by_prov rates on rates.worksheet_desc_id = cctw.worksheet_desc_id and rates.sos = cctw.sos and rates.division_id = tmp.somDiv
	join budget.revenue_prior_actual_split_perc_by_category_and_prov cat on cat.worksheet_desc_id = cctw.worksheet_desc_id and cat.sos = cctw.sos and cat.division_id = tmp.somDiv;

    drop table if exists budget.medgrp_revenue_payment_information; /* Need to drop this due to FK constraint */
    drop table if exists budget.revenue_prior_actuals_by_div;

	/**
	 * Landing table for Revenue budget exception template by division
	 */
	CREATE TABLE revenue_prior_actuals_by_div (
	  division_id int(11) NOT NULL,
	  cap_charges decimal(30,8) NOT NULL,
	  under_charges decimal(30,8) NOT NULL,
	  ffs_charges decimal(30,8) NOT NULL,
	  total_charges decimal(30,8) NOT NULL,
	  cap_collections decimal(30,8) NOT NULL,
	  under_collections decimal(30,8) NOT NULL,
	  ffs_collections decimal(30,8) NOT NULL,
	  total_collections decimal(30,8) NOT NULL,
	  cap_trvus decimal(30,8) NOT NULL,
	  under_trvus decimal(30,8) NOT NULL,
	  ffs_trvus decimal(30,8) NOT NULL,
	  total_trvus decimal(30,8) NOT NULL,
	  cap_wrvus decimal(30,8) NOT NULL,
	  under_wrvus decimal(30,8) NOT NULL,
	  ffs_wrvus decimal(30,8) NOT NULL,
	  total_wrvus decimal(30,8) NOT NULL,
	  copay decimal(30,8) NOT NULL,
	  sos varchar(64) NOT NULL,
	  cap_wrvu_split decimal(30,8) NOT NULL,
	  under_wrvu_split decimal(30,8) NOT NULL,
	  ffs_wrvu_split decimal(30,8) NOT NULL,
	  cap_collection_rate decimal(30,8) NOT NULL,
	  under_collection_rate decimal(30,8) NOT NULL,
	  ffs_collection_rate decimal(30,8) NOT NULL,
	  UNIQUE (division_id, sos)
	);
	
	
	insert into budget.revenue_prior_actuals_by_div
	select 
	    cctw.somDiv, 
	    cctw.total_cap_charges, cctw.total_under_charges, cctw.total_ffs_charges, (cctw.total_cap_charges + cctw.total_under_charges + cctw.total_ffs_charges) as total_charges,
	    cctw.total_cap_collections, cctw.total_under_collections, cctw.total_ffs_collections, (cctw.total_cap_collections + cctw.total_under_collections + cctw.total_ffs_collections) as total_collections,
	    cctw.total_cap_trvus, cctw.total_under_trvus, cctw.total_ffs_trvus, (cctw.total_cap_trvus + cctw.total_under_trvus + cctw.total_ffs_trvus) as total_trvus,
	    cctw.total_cap_wrvus, cctw.total_under_wrvus, cctw.total_ffs_wrvus, (cctw.total_cap_wrvus + cctw.total_under_wrvus + cctw.total_ffs_wrvus) as total_wrvus,
	    cctw.total_copay, cctw.sos,
	    splits.cap_wrvus_split_percentage, splits.under_wrvus_split_percentage, splits.ffs_wrvus_split_percentage,
	    rates.cap_collections_to_trvus_calculated, rates.under_collections_to_trvus_calculated, rates.ffs_collections_to_trvus_calculated
	from budget.revenue_prior_actual_cctw_by_category_and_div cctw
	join budget.revenue_prior_actual_split_perc_by_category_and_div splits on splits.division_id = cctw.somDiv and splits.sos = cctw.sos
	join budget.revenue_prior_actual_collections_per_trvus rates on rates.division_id = cctw.somDiv and rates.sos = cctw.sos;
	
	alter table budget.revenue_prior_actuals_by_div add column py_actual_id int(11) primary key auto_increment;
		
END
GO

call budget.createConsolidatedAppTables()
GO

drop procedure if exists budget.createConsolidatedAppTables
GO