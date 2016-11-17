/**
 * EMPLOYEE
 *
 * Rewrite of employee view going against the new centralized employee schema
 */
 CREATE OR REPLACE VIEW budget.employee AS (
	 SELECT
		aev.all_employee_id,
        aev.employee_reference_id AS employee_ucsd_id,
		aev.employee_fullname,
		aev.status AS status,
        he.title_code,
		he.payroll_title,
		he.working_title,
		COALESCE(he.pay_rate,0) AS pay_rate,
		he.bargaining_unit,
		aev.home_department_code,
		ud.deptName AS home_department_name,
		he.division_id,
		he.pay_schedule_code
	FROM
		dw.all_employee_view aev
	JOIN
		som_portal.ucsd_department ud ON
			ud.deptCode = aev.home_department_code
	LEFT OUTER JOIN
		hris.employee he ON
			he.employee_ucsd_id = aev.employee_ucsd_id
	WHERE
		(aev.status IS NULL OR aev.status = 'A')
)
GO
		
/**
 * VIEW budget.v_budget_admin
 * 
 * This view selects all budget admins from som_portal and obtains the employee_ucsd_id
 */
create or replace view budget.v_budget_admin as
select cur.*, convert(cu.employee_id, unsigned integer) as employee_ucsd_id
from som_portal.core_user_role cur
join som_portal.core_user cu on cu.core_user_id = cur.user_id
where cur.role = 'budget_admin' and cu.employee_id is not null
GO


/**
 * VIEW v_medgrp_revenue_division
 * 
 * Provides a unique list of medgrp divisions
 */
create or replace view budget.v_medgrp_revenue_division as
select distinct division_id from budget.v_medgrp_revenue_worksheet
union all
select 1 from dual
GO

/*
 * V_EMPLOYEE_DIVISION
 * 
 * View for displaying a list of MedGrp divisions on the landing screen.
 * Only displays divisions that have active indexes in the index map
 * with a group area = 'MedGrp'.  
 * 
 * IMPORTANT: Returns employeeUcsdId so we can list only the revenue
 * divisions they have access to
 * 
 * NOTE: Doing a RIGHT OUTER JOIN on emp_divisions to pull in Anes records
 */
create or replace view budget.v_employee_division_base as
select distinct 
    concat('MedGrp', '-', cast(ei.employee_ucsd_id as char), '-', cast(ei.division_id as char)) as id,
    sr.department_id, COALESCE(sr.department_name) as department_name, 
    sr.division_id, COALESCE(sr.division_name) as division_name, 
    ei.employee_ucsd_id,
    'MedGrp' as group_area,
    ifnull((select count(*) as provider_count from budget.medgrp_revenue_worksheet where division_id=mg.division_id group by division_id),0) as provider_count
from budget.v_medgrp_revenue_division mg
join budget.emp_divisions ei on ei.division_id = mg.division_id
join budget.v_som_division sr on sr.division_id = ei.division_id
--left outer join budget.index_map im on im.division_id = mg.division_id
where sr.division_id <> 54 and sr.division_id <> -99
union all
select distinct 
    concat('MedGrp', '-', cast(ba.employee_ucsd_id as char), '-', cast(sr.division_id as char)) as id,
    sr.department_id, COALESCE(sr.department_name) as department_name,
    sr.division_id, COALESCE(sr.division_name) as division_name, 
    ba.employee_ucsd_id,
    'MedGrp' as group_area,
    ifnull((select count(*) as provider_count from budget.medgrp_revenue_worksheet where division_id=mg.division_id group by division_id),0) as provider_count
from budget.v_medgrp_revenue_division mg
join budget.v_som_division sr on sr.division_id = mg.division_id
--left outer join budget.index_map im on im.division_id = mg.division_id
cross join budget.v_budget_admin ba
where sr.division_id <> 54 and sr.division_id <> -99
--and ba.employee_ucsd_id = 209166 and sr.division_id=206
order by department_name, division_name
GO

create or replace view budget.v_employee_division as
select * from budget.v_employee_division_base q where (q.provider_count > 0 or q.division_id = 1)
GO


/**
 * V_SOM_DIVISION
 * 
 * View for som_division div/department mapping.  Used across the board in DivisionProducer
 * so Division and Department names are sourced from the same tables application-wide.
 * 
 */
create or replace view budget.v_som_division as
select sdiv.division_id, sdiv.name as division_name, sdept.som_department_id as department_id, sdept.name as department_name 
from som_portal.som_division sdiv
join som_portal.som_department sdept on sdept.som_department_id = sdiv.department_id
order by sdept.name, sdiv.name
GO

/**
 * V_COLLECTIONS_TO_TRVUS_RATES
 * 
 * Responsible for calculating the projected Collections/tRVUs Rates for display
 * in the PAYMENT INFORMATION section of the revenue worksheet. Users can enter 
 * the percent change and that value is persisted in the medgrp_revenue_payment_information
 * table.
 */
create or replace view budget.v_collections_to_trvus_rates as
select 
    rev.division_id, rev.sos, rev.py_actual_id,
    round(((rev.proj_cap_collections_to_trvus_perc_chg * py.cap_collection_rate) + py.cap_collection_rate),2) AS proj_cap_collections_to_trvus,
    round(((rev.proj_under_collections_to_trvus_perc_chg * py.under_collection_rate) + py.under_collection_rate),2) AS  proj_under_collections_to_trvus,
    round(((rev.proj_ffs_collections_to_trvus_perc_chg * py.ffs_collection_rate) + py.ffs_collection_rate),2) AS proj_ffs_collections_to_trvus
from budget.medgrp_revenue_payment_information rev
join budget.revenue_prior_actuals_by_div py on py.py_actual_id = rev.py_actual_id
GO

/**
 * V_COLLECTIONS_TO_TRVUS_RATES_BY_PROVIDER
 * 
 * Responsible for calculating the projected Collections/tRVUs Rates for calculating
 * projected collections at the PROVIDER level.  These values are never displayed on the front-end
 */
create or replace view budget.v_collections_to_trvus_rates_by_prov as
select rev.id, rev.division_id, rev.sos,
    round(((pinf.proj_cap_collections_to_trvus_perc_chg * py.cap_collection_rate) + py.cap_collection_rate),8) AS proj_cap_collections_to_trvus,
    round(((pinf.proj_under_collections_to_trvus_perc_chg * py.under_collection_rate) + py.under_collection_rate),8) AS  proj_under_collections_to_trvus,
    round(((pinf.proj_ffs_collections_to_trvus_perc_chg * py.ffs_collection_rate) + py.ffs_collection_rate),8) AS proj_ffs_collections_to_trvus
from budget.medgrp_revenue_worksheet rev
join budget.medgrp_revenue_payment_information pinf on rev.division_id = pinf.division_id and rev.sos = pinf.sos
join budget.revenue_prior_actuals_by_prov py on py.py_actual_id = rev.py_actual_id
GO

CREATE OR REPLACE VIEW budget.v_revenue_worksheet_desc AS (
	SELECT
		rwd.worksheet_desc_id, 
		COALESCE(aev.employee_fullname,description) AS employee_fullname, 
		COALESCE(aev.employee_ucsd_id,reference_number) AS employee_ucsd_id, 
		COALESCE(aev.home_department_code,' ') AS home_department_code,
		COALESCE(ud.deptName,' ') AS home_department_name,
		rwd.all_employee_id
	FROM
		budget.medgrp_revenue_worksheet_desc rwd
	LEFT OUTER JOIN
		dw.all_employee_view aev ON
			aev.all_employee_id = rwd.all_employee_id
	LEFT OUTER JOIN
		som_portal.ucsd_department ud ON
			ud.deptCode = aev.home_department_code
	)
GO

CREATE OR REPLACE VIEW budget.v_revenue_worksheet_metadata AS (
	SELECT
		rw.id, rwd.employee_fullname, rwd.employee_ucsd_id, rwd.home_department_code, rwd.home_department_name
	FROM
		budget.medgrp_revenue_worksheet rw
	JOIN
		budget.v_revenue_worksheet_desc rwd ON
			rwd.worksheet_desc_id = rw.worksheet_desc_id
	ORDER BY
		rw.id
)
GO

/**
 * V_PROJ_TRVUS_BY_PROV
 * 
 * This view calculates the total projected trvus and projected trvus by category for each provider
 */
/*
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
left outer join budget.revenue_prior_actuals_by_prov pyProv on pyProv.py_actual_id = rev.py_actual_id
join budget.revenue_prior_actuals_by_div pyDiv on pyDiv.division_id = rev.division_id and pyDiv.sos = rev.sos
GO
*/
/*
 * V_MEDGRP_REVENUE_WORKSHEET_BASE
 * 
 * View for display on the front end that is responsible for pulling prior year actuals and 
 * obtaining extended employee info from Provider schema.
 * 
 * This view is also responsible for calculating tRVUs and Charges (based on entered wRVUs)
 * and Collections (based on calculated tRVUs and Collections/tRVU rates entered in
 * the PAYMENT INFORMATION section).
 * 
 * NOTE: The prior year data is loaded by the yearly job and is driven off the start
 * and end dates specified in the budgetMetadata table.
 */
/*
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
    rev.py_actual_id
from budget.medgrp_revenue_worksheet rev
join budget.v_revenue_worksheet_metadata md ON md.id = rev.id
join budget.v_proj_trvus_by_prov projTrvus on projTrvus.id = rev.id
join budget.v_collections_to_trvus_rates divRates on divRates.division_id = rev.division_id and divRates.sos = rev.sos
left outer join budget.v_collections_to_trvus_rates_by_prov provRates on provRates.id = rev.id
left outer join budget.mv_revenue_12mo_actual_cctw_totals roll on roll.somDiv = rev.division_id and roll.worksheet_desc_id = rev.worksheet_desc_id and roll.sos = rev.sos
order by md.employee_fullname, md.employee_ucsd_id
GO
*/
create or replace view budget.v_medgrp_revenue_worksheet_12mo_only as
select 
    ((select max(id) from budget.medgrp_revenue_worksheet) + roll.12mo_actual_id) as id,
    roll.somDiv as division_id,
	rwd.worksheet_desc_id,
    rwd.employee_fullname,
    rwd.employee_ucsd_id,
    null as status, rwd.home_department_code, rwd.home_department_name,
    null as proj_charges, null as proj_collections, 
    null as proj_trvus, null as proj_cap_trvus, null as proj_under_trvus, null as proj_ffs_trvus, 
    0.00 as proj_wrvus,null as proj_cap_wrvus, null as proj_under_wrvus, null as proj_ffs_wrvus,
    'N/A' as wrvu_percent_change, 
    null as proj_cap_collections, null as proj_under_collections, null as proj_ffs_collections, 
    null as proj_cap_charges, null as proj_under_charges, null as proj_ffs_charges,
    null as py_charges, null as py_collections, null as py_trvus, null as py_wrvus,
    roll.charges as 12mo_charges, roll.collections as 12mo_collections,
    roll.trvus as 12mo_trvus, roll.wrvus as 12mo_wrvus,
    roll.sos,
    'N' as removable_flag,
    'Y' as 12mo_only_flag,
    rev.exception_flag,
    rev.py_actual_id,
    null as care_payment_rate
from budget.mv_revenue_12mo_actual_cctw_totals roll
JOIN budget.v_revenue_worksheet_desc rwd ON rwd.worksheet_desc_id = roll.worksheet_desc_id
left outer join budget.medgrp_revenue_worksheet rev on rev.division_id = roll.somDiv and rev.worksheet_desc_id = roll.worksheet_desc_id
where rev.division_id is null and rev.worksheet_desc_id is null and rev.sos is null
order by 12mo_only_flag, division_id, employee_fullname, employee_ucsd_id
GO

/**
 * V_MEDGRP_REVENUE_WORKSHEET
 * 
 * This view adds any new providers that pop-up in the rolling 12 months data pull
 */
create or replace view budget.v_medgrp_revenue_worksheet as
select * from budget.v_medgrp_revenue_worksheet_base rev

union all

select * from budget.v_medgrp_revenue_worksheet_12mo_only
order by division_id, employee_fullname, employee_ucsd_id, sos
GO

/**
 * V_MEDGRP_REVENUE_WORKSHEET_TOTALS
 * 
 * Calculates the total projected & prior year charges, collections, tRVUs, and wRVUS
 * for each division.
 */
/*
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
left outer join budget.revenue_prior_actuals_by_div py on py.division_id=rev.division_id
left outer join budget.mv_revenue_12mo_actual_cctw_totals_by_div roll on roll.somDiv = rev.division_id
where rev.division_id <> 22
group by rev.division_id
GO
*/
/**
 * V_MEDGRP_REVENUE_WORKSHEET_TOTALS_PSYCH
 * 
 * Provider totals for Psychiatry division only
 */
-- First pull totals for INP/OUTP
create or replace view budget.v_medgrp_revenue_worksheet_totals_psych as
select py.py_actual_id as id, rev.division_id, py.sos, 
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
left outer join budget.revenue_prior_actuals_by_div py on py.division_id=rev.division_id
left outer join budget.mv_revenue_12mo_actual_cctw_totals_by_div roll on roll.somDiv = rev.division_id 
where rev.division_id = 22 and py.sos = 'INP/OUTP'
group by rev.division_id

union all

-- Next pull for INPATIENT and OUTPATIENT separately
select py.py_actual_id as id, rev.division_id, py.sos, 
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
left outer join budget.mv_revenue_12mo_actual_cctw_totals_by_div_and_sos roll on roll.somDiv = rev.division_id and roll.sos=py.sos
where rev.division_id = 22 
group by rev.division_id, py.sos
order by division_id
GO

/**
 * V_MEDGRP_REVENUE_DEPT
 * 
 * View used for revenue department rollup
 */
create or replace view budget.v_medgrp_revenue_dept as
select
    sdiv.department_id,
    sdiv.department_name,
    sdiv.division_id,
    sdiv.division_name,
    sum(proj_charges) as proj_charges,
    sum(proj_collections) as proj_collections,
    sum(proj_trvus) as proj_trvus,
    sum(proj_wrvus) as proj_wrvus,
    sum(py_charges) as py_actual_charges,
    sum(py_collections) as py_actual_collections,
    sum(py_trvus) as py_actual_trvus,
    sum(py_wrvus) as py_actual_wrvus
from budget.v_medgrp_revenue_worksheet rev
join budget.v_som_division sdiv on sdiv.division_id = rev.division_id
group by sdiv.department_id, sdiv.department_name, sdiv.division_id, sdiv.division_name
order by sdiv.department_name, sdiv.division_name
GO

/**
 * V_MEDGRP_REVENUE_PAYMENT_INFORMATION
 * 
 * This view is responsible for Category level (aka CAP, UNDERCOMP, and FFS) collection calculations
 * based on the projected totals and the split percentages from mgbs.
 */
/*
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
where rev.division_id <> 22
GO
*/
/**
 * V_MEDGRP_REVENUE_PAYMENT_INFORMATION_PSYCH
 */
create or replace view budget.v_medgrp_revenue_payment_information_psych as
select rev.id, rev.division_id, pyDiv.cap_collection_rate as cap_collections_to_trvus,
    (case when rev.sos='INP/OUTP' then round((((proj_totals.total_proj_cap_collections / proj_totals.total_proj_cap_trvus) - pyDiv.cap_collection_rate) / pyDiv.cap_collection_rate),2)  else rev.proj_cap_collections_to_trvus_perc_chg end) as proj_cap_collections_to_trvus_perc_chg, 
    (case when rev.sos='INP/OUTP' then round((((proj_totals.total_proj_under_collections / proj_totals.total_proj_under_trvus) - pyDiv.under_collection_rate) / pyDiv.under_collection_rate),2)  else rev.proj_under_collections_to_trvus_perc_chg end) as proj_under_collections_to_trvus_perc_chg, 
    (case when rev.sos='INP/OUTP' then round((((proj_totals.total_proj_ffs_collections / proj_totals.total_proj_ffs_trvus) - pyDiv.ffs_collection_rate) / pyDiv.ffs_collection_rate),2)  else rev.proj_ffs_collections_to_trvus_perc_chg end) as proj_ffs_collections_to_trvus_perc_chg,
    (case when rev.sos='INP/OUTP' then round((proj_totals.total_proj_cap_collections / proj_totals.total_proj_cap_trvus),2) else projRates.proj_cap_collections_to_trvus end) as proj_cap_collections_to_trvus,
    (case when rev.sos='INP/OUTP' then round((proj_totals.total_proj_under_collections / proj_totals.total_proj_under_trvus),2) else projRates.proj_under_collections_to_trvus end) as proj_under_collections_to_trvus,
    (case when rev.sos='INP/OUTP' then round((proj_totals.total_proj_ffs_collections / proj_totals.total_proj_ffs_trvus),2) else projRates.proj_ffs_collections_to_trvus end) as proj_ffs_collections_to_trvus,
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
    (case when rev.sos='INP/OUTP' then (select sum(i.proj_copay) from budget.medgrp_revenue_payment_information i where i.division_id = 22 and i.sos <> 'INP/OUTP') else rev.proj_copay end) as proj_copay,
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
join budget.v_medgrp_revenue_worksheet_totals_psych proj_totals on proj_totals.division_id = rev.division_id and proj_totals.sos = rev.sos
where rev.division_id=22
GO

/**
 * View that calculates inpatient percentage for psych since the field is readonly 
 * and controller enters by sos
 */
create or replace view budget.v_medgrp_revenue_inpatient_perc_psych as
select 
    total.division_id, inp.total_proj_trvus as total_inpatient_trvus, total.total_proj_trvus as total_div_trvus,
    round(rev.prior_inpatient_percentage,4) as prior_inpatient_percentage, 
    round((inp.total_proj_trvus / total.total_proj_trvus),4) as proj_inpatient_percentage
from budget.v_medgrp_revenue_payment_information_psych total
join budget.v_medgrp_revenue_payment_information_psych inp on inp.division_id = total.division_id and inp.sos = 'INPATIENT'
join budget.medgrp_revenue_inpatient_perc rev on rev.division_id = total.division_id
group by total.division_id
GO

/**
 * VIEW v_revenue_prior_actual_split_perc_by_category_and_div
 * Displays DIVISION level wrvu split information
 */
create or replace view budget.v_revenue_prior_actual_split_perc_by_category_and_div as
select 
	py.py_actual_id,
    py.division_id, py.sos, 
    py.total_wrvus as total_wrvus_amount,
    py.cap_wrvus as total_cap_wrvus,
    py.cap_wrvu_split as cap_wrvus_split_percentage, 
    py.under_wrvus as total_under_wrvus,
    py.under_wrvu_split as under_wrvus_split_percentage,
    py.ffs_wrvus as total_ffs_wrvus,
    py.ffs_wrvu_split as ffs_wrvus_split_percentage
from budget.revenue_prior_actuals_by_div py
GO

/**
 * VIEW v_revenue_prior_actual_split_perc_by_category_and_prov
 * Displays PROVIDER level wrvu split information
 */
create or replace view budget.v_revenue_prior_actual_split_perc_by_category_and_prov as
select 
    py.division_id, rwd.employee_fullname, rwd.employee_ucsd_id, py.sos,
    py.total_wrvus as total_prov_wrvus, 
    round((py.cap_wrvu_split * py.total_wrvus),2) as total_cap_wrvus, py.cap_wrvu_split as perc_cap_wrvus,
    round((py.under_wrvu_split * py.total_wrvus),2) as total_under_wrvus, py.under_wrvu_split as perc_under_wrvus,
    round((py.ffs_wrvu_split * py.total_wrvus),2) as total_ffs_wrvus, py.ffs_wrvu_split as perc_ffs_wrvus, 
    py.py_actual_id as id
from budget.revenue_prior_actuals_by_prov py
JOIN budget.v_revenue_worksheet_desc rwd ON rwd.worksheet_desc_id = py.worksheet_desc_id
GO

CREATE OR REPLACE VIEW budget.v_revenue_prior_actuals_by_prov AS
	SELECT
		rwd.employee_ucsd_id,
		rwd.employee_fullname,
		py.*
	FROM
		budget.revenue_prior_actuals_by_prov py
	JOIN budget.v_revenue_worksheet_desc rwd ON rwd.worksheet_desc_id = py.worksheet_desc_id
GO

CREATE OR REPLACE VIEW budget.v_division_with_worksheet AS (
    SELECT
        rw.division_id, rw.sos
    FROM
		budget.medgrp_revenue_worksheet rw
    GROUP BY
        rw.division_id, rw.sos
)
GO

CREATE OR REPLACE VIEW budget.v_provider_in_worksheet AS (
    SELECT
        aev.*, rw.division_id, rw.sos
    FROM
        budget.medgrp_revenue_worksheet rw
    JOIN
        budget.medgrp_revenue_worksheet_desc wd ON
            wd.worksheet_desc_id = rw.worksheet_desc_id
    JOIN
        dw.all_employee_view aev ON
            aev.all_employee_id = wd.all_employee_id
    GROUP BY
        aev.all_employee_id, rw.division_id, rw.sos
    ORDER BY
        aev.all_employee_id
)
GO