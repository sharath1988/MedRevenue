/**
 * Rewrite of the revenue_output view.  This view is a union of 3 queries that are grouped 
 * by SOS for salpro.
 * 
 * Query 1:  Pulls all data for Psychiatry (which is already grouped by sos)
 * Query 2:  Pulls all divisions that are not Psych and calculates INPATIENT amounts 
 * 	by multiplying by the projected inpatient percentage.
 * Query 3:  Pulls all divisions that are not Psych and calculates OUTPATIENT amounts
 * 	by multiplying by (1 - the projected inpatient percentage).
 */
create or replace view revenue_output as 
select 
    rev.id, wd.all_employee_id, rev.employee_ucsd_id, rev.employee_fullname as provider, sdiv.department_id, rev.division_id,
    py.cap_charges, py.cap_collections, py.cap_trvus, py.cap_wrvus,
    py.under_charges, py.under_collections, py.under_trvus, py.under_wrvus,
    py.ffs_charges, py.ffs_collections, py.ffs_trvus, py.ffs_wrvus,
    (py.cap_charges + py.under_charges + py.ffs_charges) as total_charges, 
    (py.cap_collections + py.under_collections + py.ffs_collections) as total_collections, 
    (py.cap_trvus + py.under_trvus + py.ffs_trvus) as total_trvus, 
    (py.cap_wrvus + py.under_wrvus + py.ffs_wrvus) as total_wrvus,

    rev.proj_cap_charges,
    rev.proj_cap_collections,
    rev.proj_cap_trvus,
    rev.proj_cap_wrvus,

    rev.proj_under_charges,
    rev.proj_under_collections,
    rev.proj_under_trvus,
    rev.proj_under_wrvus,

    rev.proj_ffs_charges,
    rev.proj_ffs_collections,
    rev.proj_ffs_trvus,
    rev.proj_ffs_wrvus,

    rev.proj_charges as proj_total_charges, rev.proj_collections as proj_total_collections, 
    rev.proj_trvus as proj_total_trvus, rev.proj_wrvus as proj_total_wrvus,
    (case when rev.wrvu_percent_change = 'N/A' then 0.00 else rev.wrvu_percent_change * 1.00 end) as proj_total_change,
    (meta.budget_current_year * 1) as budget_year,
    rev.sos,
    if(isnull(outside.division_id), 'N', 'Y') as OutsideBilled,
    'Y' as SalproLoad
from budget.v_medgrp_revenue_worksheet rev
join budget.medgrp_revenue_worksheet_desc wd ON rev.worksheet_desc_id = wd.worksheet_desc_id
join budget.budgetMetadata meta
join budget.v_som_division sdiv on sdiv.division_id = rev.division_id
left outer join budget.mv_revenue_prior_actuals_cctw_by_category_and_sos py on py.worksheet_desc_id = rev.worksheet_desc_id and rev.sos = py.sos and py.somDiv = rev.division_id
left outer join budget.revenue_budget_outside outside on outside.division_id = rev.division_id and outside.exception_flag = 'N'
where rev.division_id=22 and rev.12mo_only_flag = 'N' and rev.exception_flag = 'N'

union all

select --ip.proj_inpatient_percentage, ip.prior_inpatient_percentage,
    rev.id, wd.all_employee_id, rev.employee_ucsd_id, rev.employee_fullname as provider, sdiv.department_id, rev.division_id,

    ifnull(py.cap_charges,0) as cap_charges, 
    ifnull(py.cap_collections,0) as cap_collections, 
    ifnull(py.cap_trvus,0) as cap_trvus, 
    ifnull(py.cap_wrvus,0) as cap_wrvus,

    ifnull(py.under_charges,0) as under_charges, 
    ifnull(py.under_collections,0) as under_collections, 
    ifnull(py.under_trvus,0) as under_trvus, 
    ifnull(py.under_wrvus,0) as under_wrvus,

    ifnull(py.ffs_charges,0) as ffs_charges, 
    ifnull(py.ffs_collections,0) as ffs_collections, 
    ifnull(py.ffs_trvus,0) as ffs_trvus, 
    ifnull(py.ffs_wrvus,0) as ffs_wrvus,

    ifnull((py.cap_charges + py.under_charges + py.ffs_charges),0) as total_charges, 
    ifnull((py.cap_collections + py.under_collections + py.ffs_collections),0) as total_collections, 
    ifnull((py.cap_trvus + py.under_trvus + py.ffs_trvus),0) as total_trvus, 
    ifnull((py.cap_wrvus + py.under_wrvus + py.ffs_wrvus),0) as total_wrvus,

    ifnull(round(rev.proj_cap_charges * ip.proj_inpatient_percentage,2),0) as proj_cap_charges,
    ifnull(round(rev.proj_cap_collections * ip.proj_inpatient_percentage,2),0) as proj_cap_collections,
    ifnull(round(rev.proj_cap_trvus * ip.proj_inpatient_percentage,2),0) as proj_cap_trvus,
    ifnull(round(rev.proj_cap_wrvus * ip.proj_inpatient_percentage,2),0) as proj_cap_wrvus,

    round(rev.proj_under_charges * ip.proj_inpatient_percentage,2) as proj_under_charges,
    round(rev.proj_under_collections * ip.proj_inpatient_percentage,2) as proj_under_collections,
    round(rev.proj_under_trvus * ip.proj_inpatient_percentage,2) as proj_under_trvus,
    round(rev.proj_under_wrvus * ip.proj_inpatient_percentage,2) as proj_under_wrvus,

    round(rev.proj_ffs_charges * ip.proj_inpatient_percentage,2) as proj_ffs_charges,
    round(rev.proj_ffs_collections * ip.proj_inpatient_percentage,2) as proj_ffs_collections,
    round(rev.proj_ffs_trvus * ip.proj_inpatient_percentage,2) as proj_ffs_trvus,
    round(rev.proj_ffs_wrvus * ip.proj_inpatient_percentage,2) as proj_ffs_wrvus,

    round(rev.proj_charges * ip.proj_inpatient_percentage,2) as proj_total_charges, 
    round(rev.proj_collections * ip.proj_inpatient_percentage,2) as proj_total_collections, 
    round(rev.proj_trvus * ip.proj_inpatient_percentage,2) as proj_total_trvus, 
    round(rev.proj_wrvus * ip.proj_inpatient_percentage,2) as proj_total_wrvus,

    (case when rev.wrvu_percent_change = 'N/A' then 0.00 else rev.wrvu_percent_change * 1.00 end) as proj_total_change,
    (meta.budget_current_year * 1) as budget_year,
    'INPATIENT',
    if(isnull(outside.division_id), 'N', 'Y') as OutsideBilled,
    'Y' as SalproLoad
from budget.v_medgrp_revenue_worksheet rev
join budget.medgrp_revenue_worksheet_desc wd ON wd.worksheet_desc_id = rev.worksheet_desc_id
join budget.budgetMetadata meta
join budget.v_som_division sdiv on sdiv.division_id = rev.division_id
join budget.medgrp_revenue_inpatient_perc ip on ip.division_id = rev.division_id
left outer join budget.mv_revenue_prior_actuals_cctw_by_category_and_sos py on py.worksheet_desc_id = rev.worksheet_desc_id and py.sos = 'INPATIENT' and py.somDiv = rev.division_id
left outer join budget.revenue_budget_outside outside on outside.division_id = rev.division_id and outside.exception_flag = 'N'
where rev.division_id<>22 and rev.12mo_only_flag = 'N' and rev.exception_flag = 'N'
--where rev.division_id = 5

union all

select --ip.proj_inpatient_percentage, ip.prior_inpatient_percentage,
    rev.id, wd.all_employee_id, rev.employee_ucsd_id, rev.employee_fullname as provider, sdiv.department_id, rev.division_id,

    ifnull(py.cap_charges,0) as cap_charges, 
    ifnull(py.cap_collections,0) as cap_collections, 
    ifnull(py.cap_trvus,0) as cap_trvus, 
    ifnull(py.cap_wrvus,0) as cap_wrvus,

    ifnull(py.under_charges,0) as under_charges, 
    ifnull(py.under_collections,0) as under_collections, 
    ifnull(py.under_trvus,0) as under_trvus, 
    ifnull(py.under_wrvus,0) as under_wrvus,

    ifnull(py.ffs_charges,0) as ffs_charges, 
    ifnull(py.ffs_collections,0) as ffs_collections, 
    ifnull(py.ffs_trvus,0) as ffs_trvus, 
    ifnull(py.ffs_wrvus,0) as ffs_wrvus,

    ifnull((py.cap_charges + py.under_charges + py.ffs_charges),0) as total_charges, 
    ifnull((py.cap_collections + py.under_collections + py.ffs_collections),0) as total_collections, 
    ifnull((py.cap_trvus + py.under_trvus + py.ffs_trvus),0) as total_trvus, 
    ifnull((py.cap_wrvus + py.under_wrvus + py.ffs_wrvus),0) as total_wrvus,

    ifnull(round(rev.proj_cap_charges * (1 - ip.proj_inpatient_percentage),2),0) as proj_cap_charges,
    ifnull(round(rev.proj_cap_collections * (1 - ip.proj_inpatient_percentage),2),0) as proj_cap_collections,
    ifnull(round(rev.proj_cap_trvus * (1 - ip.proj_inpatient_percentage),2),0) as proj_cap_trvus,
    ifnull(round(rev.proj_cap_wrvus * (1 - ip.proj_inpatient_percentage),2),0) as proj_cap_wrvus,

    round(rev.proj_under_charges * (1 - ip.proj_inpatient_percentage),2) as proj_under_charges,
    round(rev.proj_under_collections * (1 - ip.proj_inpatient_percentage),2) as proj_under_collections,
    round(rev.proj_under_trvus * (1 - ip.proj_inpatient_percentage),2) as proj_under_trvus,
    round(rev.proj_under_wrvus * (1 - ip.proj_inpatient_percentage),2) as proj_under_wrvus,

    round(rev.proj_ffs_charges * (1 - ip.proj_inpatient_percentage),2) as proj_ffs_charges,
    round(rev.proj_ffs_collections * (1 - ip.proj_inpatient_percentage),2) as proj_ffs_collections,
    round(rev.proj_ffs_trvus * (1 - ip.proj_inpatient_percentage),2) as proj_ffs_trvus,
    round(rev.proj_ffs_wrvus * (1 - ip.proj_inpatient_percentage),2) as proj_ffs_wrvus,

    round(rev.proj_charges * (1 - ip.proj_inpatient_percentage),2) as proj_total_charges, 
    round(rev.proj_collections * (1 - ip.proj_inpatient_percentage),2) as proj_total_collections, 
    round(rev.proj_trvus * (1 - ip.proj_inpatient_percentage),2) as proj_total_trvus, 
    round(rev.proj_wrvus * (1 - ip.proj_inpatient_percentage),2) as proj_total_wrvus,

    (case when rev.wrvu_percent_change = 'N/A' then 0.00 else rev.wrvu_percent_change * 1.00 end) as proj_total_change,
    (meta.budget_current_year * 1) as budget_year,
    'OUTPATIENT',
    if(isnull(outside.division_id), 'N', 'Y') as OutsideBilled,
    'Y' as SalproLoad
from budget.v_medgrp_revenue_worksheet rev
join budget.medgrp_revenue_worksheet_desc wd ON wd.worksheet_desc_id = rev.worksheet_desc_id
join budget.budgetMetadata meta
join budget.v_som_division sdiv on sdiv.division_id = rev.division_id
join budget.medgrp_revenue_inpatient_perc ip on ip.division_id = rev.division_id
left outer join budget.mv_revenue_prior_actuals_cctw_by_category_and_sos py on py.worksheet_desc_id = rev.worksheet_desc_id and py.sos = 'OUTPATIENT' and py.somDiv = rev.division_id
left outer join budget.revenue_budget_outside outside on outside.division_id = rev.division_id and outside.exception_flag = 'N'
where rev.division_id<>22 and rev.12mo_only_flag = 'N' and rev.exception_flag = 'N';
--where rev.division_id = 5
GO

-- validation for non division 22
select * from (
select ro.provider, ro.employee_ucsd_id, ro.division_id, ro.id,
    sum(ro.proj_total_charges) as proj_total_charges, rev.proj_charges,
    sum(ro.proj_total_collections) as proj_total_collections, rev.proj_collections, 
    sum(ro.proj_total_trvus) as proj_total_trvus, rev.proj_trvus, 
    sum(ro.proj_total_wrvus) as proj_total_wrvus, rev.proj_wrvus
from revenue_output ro
join v_medgrp_revenue_worksheet rev on rev.id = ro.id
--where ro.division_id=5 --and ro.sos = 'INPATIENT'
group by ro.provider, ro.employee_ucsd_id, ro.division_id
) q where division_id <> 22 and (round(proj_total_charges) <> round(proj_charges) or round(proj_total_collections) <> round(proj_collections) 
    or round(proj_total_trvus) <> round(proj_trvus) or round(proj_total_wrvus) <> round(proj_wrvus))
GO

-- validation for division 22
