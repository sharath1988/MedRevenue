use budget
GO

DROP PROCEDURE IF EXISTS budget.sp_retroFitPsych
GO

/*
 * This procedure combines psych records into INP/OUTP sos
 */
CREATE PROCEDURE budget.sp_retroFitPsych()
BEGIN
	
	create or replace view budget.v_proj_trvus_by_prov as
	select 
	    rev.id, rev.division_id, md.employee_fullname, md.employee_ucsd_id, rev.sos, pyProv.py_actual_id,
	    ifnull((budget.calculateProjectedTrvus(rev.proj_wrvus, ifnull(pyProv.total_trvus,0), ifnull(pyProv.total_wrvus,0), pyDiv.total_trvus, pyDiv.total_wrvus)),0) as proj_trvus,
	    
		ifnull(budget.calculateTrvusByCategory(rev.proj_wrvus, ifnull(pyProv.total_trvus,0), ifnull(pyProv.total_wrvus,0), pyDiv.total_trvus, pyDiv.total_wrvus, 
			pyDiv.cap_wrvu_split, ifnull(pyProv.cap_wrvu_split,0)),0) as proj_cap_trvus,
			
		ifnull(budget.calculateTrvusByCategory(rev.proj_wrvus, ifnull(pyProv.total_trvus,0), ifnull(pyProv.total_wrvus,0), pyDiv.total_trvus, pyDiv.total_wrvus, 
			pyDiv.under_wrvu_split, ifnull(pyProv.under_wrvu_split,0)),0) as proj_under_trvus,
			
		ifnull(budget.calculateTrvusByCategory(rev.proj_wrvus, ifnull(pyProv.total_trvus,0), ifnull(pyProv.total_wrvus,0), pyDiv.total_trvus, pyDiv.total_wrvus, 
			pyDiv.ffs_wrvu_split, ifnull(pyProv.ffs_wrvu_split,0)),0) as proj_ffs_trvus,
			
		ifnull(budget.calculateProjWrvusByCategory(rev.proj_wrvus, ifnull(pyProv.total_wrvus,0), 
			pyDiv.cap_wrvu_split, ifnull(pyProv.cap_wrvu_split,0)),0) as proj_cap_wrvus,
			
		ifnull(budget.calculateProjWrvusByCategory(rev.proj_wrvus, ifnull(pyProv.total_wrvus,0), 
			pyDiv.under_wrvu_split, ifnull(pyProv.under_wrvu_split,0)),0) as proj_under_wrvus,
			
		ifnull(budget.calculateProjWrvusByCategory(rev.proj_wrvus, ifnull(pyProv.total_wrvus,0), 
			pyDiv.ffs_wrvu_split, ifnull(pyProv.ffs_wrvu_split,0)),0) as proj_ffs_wrvus,
			
	    pyProv.total_charges as charges, pyProv.total_collections as collections, pyProv.total_trvus as trvus, pyProv.total_wrvus as wrvus, 
	    pyDiv.total_charges, pyDiv.total_wrvus,
	    pyProv.cap_wrvu_split as prov_cap_wrvu_split, pyProv.under_wrvu_split as prov_under_wrvu_split, pyProv.ffs_wrvu_split as prov_ffs_wrvu_split,
	    pyDiv.cap_wrvu_split as div_cap_wrvu_split, pyDiv.under_wrvu_split as div_under_wrvu_split, pyDiv.ffs_wrvu_split as div_ffs_wrvu_split
	from budget.medgrp_revenue_worksheet rev
	JOIN budget.v_revenue_worksheet_metadata md ON md.id = rev.id
	/*left outer join budget.mv_revenue_prior_actuals_by_prov_sum pyProv on pyProv.py_actual_id = rev.py_actual_id*/
    left outer join budget.mv_revenue_prior_actuals_by_prov_sum pyProv on pyProv.worksheet_desc_id = rev.worksheet_desc_id and pyProv.division_id = rev.division_id
	join budget.revenue_prior_actuals_by_div pyDiv on pyDiv.division_id = rev.division_id and pyDiv.sos = rev.sos
	;	
	
	/*Sum by SOS for Psych*/
	DROP TABLE IF EXISTS budget.mv_revenue_12mo_actual_cctw_totals_sum
	;
	
	CREATE TABLE budget.mv_revenue_12mo_actual_cctw_totals_sum as
	SELECT 
	    worksheet_desc_id, somDiv, 'INP/OUTP' as sos, 
	    sum(charges) charges, sum(collections) collections, sum(trvus) trvus, sum(wrvus) wrvus, sum(copay) copay, max(12mo_actual_id) 12mo_actual_id
	FROM budget.mv_revenue_12mo_actual_cctw_totals 
	GROUP BY worksheet_desc_id, somDiv
	;
	
	create or replace view budget.v_medgrp_revenue_worksheet_base as
	select 
	    rev.id,
	    rev.division_id, 
		rev.worksheet_desc_id,
		md.employee_fullname, md.employee_ucsd_id, 
	    ' ' as status, md.home_department_code, md.home_department_name,
	    ifnull((budget.calculateProjectedCharges(rev.proj_wrvus, ifnull(projTrvus.charges,0), ifnull(projTrvus.wrvus,0), projTrvus.total_charges, projTrvus.total_wrvus)),0) as proj_charges,
	    ifnull((budget.calculateProjectedCollections ( 
	            projTrvus.proj_trvus,
	            ifnull(projTrvus.wrvus,0),
	            divRates.proj_cap_collections_to_trvus, divRates.proj_under_collections_to_trvus, divRates.proj_ffs_collections_to_trvus,
	            provRates.proj_cap_collections_to_trvus, provRates.proj_under_collections_to_trvus, provRates.proj_ffs_collections_to_trvus,
	            ifnull(projTrvus.prov_cap_wrvu_split,0), ifnull(projTrvus.prov_under_wrvu_split,0), ifnull(projTrvus.prov_ffs_wrvu_split,0),
	            projTrvus.div_cap_wrvu_split, projTrvus.div_under_wrvu_split, projTrvus.div_ffs_wrvu_split
	        ) 
	    ),0) as proj_collections,
	    projTrvus.proj_trvus,
	    projTrvus.proj_cap_trvus,
		projTrvus.proj_under_trvus,
		projTrvus.proj_ffs_trvus,
			
	    rev.proj_wrvus as proj_wrvus,
	    projTrvus.proj_cap_wrvus,
	    projTrvus.proj_under_wrvus,
	    projTrvus.proj_ffs_wrvus,
	    ifnull(round(((rev.proj_wrvus - projTrvus.wrvus)) / projTrvus.wrvus, 2), 'N/A') as wrvu_percent_change,
	
	    budget.calculateCollectionsByCategory(
	        projTrvus.proj_trvus, ifnull(projTrvus.wrvus,0),
	        divRates.proj_cap_collections_to_trvus,
	        provRates.proj_cap_collections_to_trvus,
	        ifnull(projTrvus.prov_cap_wrvu_split,0), projTrvus.div_cap_wrvu_split) as proj_cap_collections,
	
	    budget.calculateCollectionsByCategory(
	        projTrvus.proj_trvus, ifnull(projTrvus.wrvus,0),
	        divRates.proj_under_collections_to_trvus,
	        provRates.proj_under_collections_to_trvus,
	        ifnull(projTrvus.prov_under_wrvu_split,0), projTrvus.div_under_wrvu_split) as proj_under_collections,
	
	    budget.calculateCollectionsByCategory(
	        projTrvus.proj_trvus, ifnull(projTrvus.wrvus,0),
	        divRates.proj_ffs_collections_to_trvus,
	        provRates.proj_ffs_collections_to_trvus,
	        ifnull(projTrvus.prov_ffs_wrvu_split,0), projTrvus.div_ffs_wrvu_split) as proj_ffs_collections,
	        
		ifnull((budget.calculateProjectedChargesByCategory(
			rev.proj_wrvus, ifnull(projTrvus.charges,0), 
			ifnull(projTrvus.wrvus,0), 
			projTrvus.total_charges, projTrvus.total_wrvus,
			ifnull(projTrvus.prov_cap_wrvu_split,0), projTrvus.div_cap_wrvu_split)),0) as proj_cap_charges,
			
		ifnull((budget.calculateProjectedChargesByCategory(
			rev.proj_wrvus, ifnull(projTrvus.charges,0), 
			ifnull(projTrvus.wrvus,0), 
			projTrvus.total_charges, projTrvus.total_wrvus,
			ifnull(projTrvus.prov_under_wrvu_split,0), projTrvus.div_under_wrvu_split)),0) as proj_under_charges,
			
		ifnull((budget.calculateProjectedChargesByCategory(
			rev.proj_wrvus, ifnull(projTrvus.charges,0), 
			ifnull(projTrvus.wrvus,0), 
			projTrvus.total_charges, projTrvus.total_wrvus,
			ifnull(projTrvus.prov_ffs_wrvu_split,0), projTrvus.div_ffs_wrvu_split)),0) as proj_ffs_charges,		
			
	    projTrvus.charges as py_charges,
	    projTrvus.collections as py_collections,
	    projTrvus.trvus as py_trvus,
	    projTrvus.wrvus as py_wrvus,
	    roll.charges as 12mo_charges,
	    roll.collections as 12mo_collections,
	    roll.trvus as 12mo_trvus,
	    roll.wrvus as 12mo_wrvus,
	    rev.sos,
	    if(projTrvus.py_actual_id is null, 'Y', 'N') as removable_flag,
	    'N' as 12mo_only_flag,
	    rev.exception_flag,
	    rev.py_actual_id,
	    rev.care_payment_rate
	from budget.medgrp_revenue_worksheet rev
	join budget.v_revenue_worksheet_metadata md ON md.id = rev.id
	join budget.v_proj_trvus_by_prov projTrvus on projTrvus.id = rev.id
	join budget.v_collections_to_trvus_rates divRates on divRates.division_id = rev.division_id and divRates.sos = rev.sos
	left outer join budget.v_collections_to_trvus_rates_by_prov provRates on provRates.id = rev.id
	left outer join budget.mv_revenue_12mo_actual_cctw_totals_sum roll on roll.somDiv = rev.division_id and roll.worksheet_desc_id = rev.worksheet_desc_id and roll.sos = rev.sos
	order by md.employee_fullname, md.employee_ucsd_id
	;
	
	/*
	 * Include psych in views and set IsPsychiatry session variable in CDI to false for everything
	 */
	create or replace view budget.v_medgrp_revenue_worksheet_totals as
	select rev.division_id as id, rev.division_id, _latin1'INP/OUTP' as sos, 
	    ifnull(sum(rev.proj_charges),0.00) as total_proj_charges,
	    ifnull(sum(rev.proj_cap_charges),0.00) as total_proj_cap_charges,
	    ifnull(sum(rev.proj_under_charges),0.00) as total_proj_under_charges,
	    ifnull(sum(rev.proj_ffs_charges),0.00) as total_proj_ffs_charges,
	    ifnull(sum(rev.proj_collections),0.00) as total_proj_collections,
	    ifnull(sum(rev.proj_cap_collections),0.00) as total_proj_cap_collections,
	    ifnull(sum(rev.proj_under_collections),0.00) as total_proj_under_collections,
	    ifnull(sum(rev.proj_ffs_collections),0.00) as total_proj_ffs_collections,    
	    ifnull(sum(rev.proj_trvus),0.00) as total_proj_trvus,
	    ifnull(sum(rev.proj_cap_trvus),0.00) as total_proj_cap_trvus,
	    ifnull(sum(rev.proj_under_trvus),0.00) as total_proj_under_trvus,
	    ifnull(sum(rev.proj_ffs_trvus),0.00) as total_proj_ffs_trvus,
	    ifnull(sum(rev.proj_wrvus),0.00) as total_proj_wrvus,
	    py.total_charges as total_py_charges,
	    py.total_collections as total_py_collections,
	    py.total_trvus as total_py_trvus,
	    py.total_wrvus as total_py_wrvus,
	    roll.total_charges as total_12mo_charges,
	    roll.total_collections as total_12mo_collections,
	    roll.total_trvus as total_12mo_trvus,
	    roll.total_wrvus as total_12mo_wrvus
	from budget.v_medgrp_revenue_worksheet_base rev
	left outer join budget.revenue_prior_actuals_by_div py on py.division_id=rev.division_id and rev.sos = py.sos
	left outer join budget.mv_revenue_12mo_actual_cctw_totals_by_div roll on roll.somDiv = rev.division_id
	group by rev.division_id
	;
	
	create or replace view budget.v_medgrp_revenue_payment_information as
	select rev.id, rev.division_id,
	    rev.proj_cap_collections_to_trvus_perc_chg, rev.proj_under_collections_to_trvus_perc_chg, rev.proj_ffs_collections_to_trvus_perc_chg,
	    projRates.proj_cap_collections_to_trvus, projRates.proj_under_collections_to_trvus, projRates.proj_ffs_collections_to_trvus,
	    proj_totals.total_proj_trvus,
	    proj_totals.total_proj_cap_trvus,
	    proj_totals.total_proj_under_trvus,
	    proj_totals.total_proj_ffs_trvus,
	    
	    round((proj_totals.total_proj_cap_charges),2) as proj_cap_charges,
	    round((proj_totals.total_proj_under_charges),2) as proj_under_charges,
	    round((proj_totals.total_proj_ffs_charges),2) as proj_ffs_charges,
	    
	    round((proj_totals.total_proj_cap_collections),2) as proj_cap_collections,
	    round((proj_totals.total_proj_under_collections),2) as proj_under_collections,
	    round((proj_totals.total_proj_ffs_collections),2) as proj_ffs_collections,
	    proj_totals.total_proj_collections,
	    round((proj_totals.total_proj_collections / proj_totals.total_proj_trvus),2) as proj_blended_rate,
	    round(pyDiv.cap_wrvu_split,4) as cap_wrvus_split_percentage, 
	    round(pyDiv.under_wrvu_split,4) as under_wrvus_split_percentage, 
	    round(pyDiv.ffs_wrvu_split,4) as ffs_wrvus_split_percentage,
	    
	    round(pyDiv.cap_charges, 2) as total_prior_cap_charges,
	    round(pyDiv.under_charges,2) as total_prior_under_charges,
	    round(pyDiv.ffs_charges,2) as total_prior_ffs_charges,
	    
	    round(pyDiv.cap_collections, 2) as total_prior_cap_collections,
	    round(pyDiv.under_collections,2) as total_prior_under_collections,
	    round(pyDiv.ffs_collections,2) as total_prior_ffs_collections,
	    pyDiv.cap_collection_rate as prior_cap_collections_to_trvus,
	    pyDiv.under_collection_rate as prior_under_collections_to_trvus,
	    pyDiv.ffs_collection_rate as prior_ffs_collections_to_trvus,
	    round(proj_totals.total_py_collections,2) as total_prior_collections,
	    round((proj_totals.total_py_collections / proj_totals.total_py_trvus),2) as prior_blended_rate,
	    rev.proj_copay,
	    pyDiv.copay as prior_copay,
	    round((proj_totals.total_py_collections + pyDiv.copay),2) as total_prior_collections_inc_copay,
	    round((proj_totals.total_proj_collections + rev.proj_copay),2) as total_proj_collections_inc_copay,
	    rev.sos,
	    proj_totals.total_proj_charges,
	    proj_totals.total_proj_wrvus,
	    proj_totals.total_py_charges as total_prior_charges,
	    proj_totals.total_py_trvus as total_prior_trvus,
	    proj_totals.total_py_wrvus as total_prior_wrvus,
	    proj_totals.total_12mo_charges,
	    proj_totals.total_12mo_collections,
	    proj_totals.total_12mo_trvus,
	    proj_totals.total_12mo_wrvus
	from budget.medgrp_revenue_payment_information rev
	join budget.v_collections_to_trvus_rates projRates on projRates.division_id = rev.division_id and projRates.sos = rev.sos
	join budget.revenue_prior_actuals_by_div pyDiv on pyDiv.py_actual_id = rev.py_actual_id
	join budget.v_medgrp_revenue_worksheet_totals proj_totals on proj_totals.division_id = rev.division_id and proj_totals.sos = rev.sos
	;	
	
	DROP TABLE IF EXISTS budget.medgrp_revenue_worksheet_bkup
	;
	
	CREATE TABLE budget.medgrp_revenue_worksheet_bkup AS
	SELECT * FROM budget.medgrp_revenue_worksheet
	;
	
	DROP TABLE IF EXISTS budget.medgrp_revenue_payment_information_bkup
	;
	
	CREATE TABLE budget.medgrp_revenue_payment_information_bkup AS
	SELECT * FROM budget.medgrp_revenue_payment_information
	;
	
	DELETE FROM budget.medgrp_revenue_payment_information WHERE sos <> 'INP/OUTP'
	;
	
	DROP TABLE IF EXISTS budget.tmp_medgrp_revenue_worksheet_psych
	;
		
	/*
	CREATE TABLE tmp_medgrp_revenue_worksheet_psych AS
	SELECT max(id) AS id, max(py_actual_id) as py_actual_id, worksheet_desc_id, 'INP/OUTP' as sos, division_id, sum(proj_wrvus) as proj_wrvus, exception_flag 
	FROM medgrp_revenue_worksheet 
	WHERE division_id = 22
	GROUP BY worksheet_desc_id, division_id, exception_flag
	;
	
	DELETE FROM medgrp_revenue_worksheet WHERE division_id = 22
	;
	
	INSERT INTO medgrp_revenue_worksheet SELECT * FROM tmp_medgrp_revenue_worksheet_psych
	;
	*/
	
	DROP TABLE IF EXISTS budget.medgrp_revenue_worksheet_bkup
	;
	DROP TABLE IF EXISTS budget.medgrp_revenue_payment_information_bkup
	;
	DROP TABLE IF EXISTS budget.tmp_medgrp_revenue_worksheet_psych
	;
END
GO


call budget.sp_retroFitPsych()
GO


/**
 * 
 * Validations
 */
/*

-- check to make sure prior and current equal to start
select worksheet_desc_id, division_id, employee_fullname, employee_ucsd_id, proj_wrvus, py_wrvus from v_medgrp_revenue_worksheet where proj_wrvus <> py_wrvus and proj_wrvus <> 0
order by division_id, employee_fullname
/

select w.division_id, employee_ucsd_id, w.worksheet_desc_id, proj_wrvus, py_wrvu, py.employeeId, py.somDiv
from v_medgrp_revenue_worksheet w
left join (
    select b.somDiv, c.employeeId, sum(c.wrvu) py_wrvu, d.worksheet_desc_id
    from mgfs.mgbs_composite c
    join mgfs.mgBilling b on b.number = c.billingAreaNumber
    join mgfs.mgbs_medgrp_rev_worksheet_desc d ON d.rowId = c.rowId
    group by b.somDiv, c.employeeId, worksheet_desc_id
) py on py.somDiv = w.division_id and py.worksheet_desc_id = w.worksheet_desc_id
--where py.somDiv is null or py.employeeId is null
where py.py_wrvu <> proj_wrvus 
and proj_wrvus <> 0
order by division_id, employee_ucsd_id
/
*/
/*
 * 
truncate medgrp_revenue_worksheet
/

select * from medgrp_revenue_worksheet
/

insert into medgrp_revenue_worksheet 
select * from medgrp_revenue_worksheet_bkup
/

truncate medgrp_revenue_payment_information
/

insert into medgrp_revenue_payment_information
select * from medgrp_revenue_payment_information_bkup
/
 */
*/