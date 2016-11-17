DROP PROCEDURE IF EXISTS spGetRevenueWorksheetByDivisionId
GO

CREATE PROCEDURE spGetRevenueWorksheetByDivisionId(IN pDivisionId int)
BEGIN
    select * from budget.v_medgrp_revenue_worksheet_base rev where division_id = pDivisionId
    union all
    select * from budget.v_medgrp_revenue_worksheet_12mo_only where division_id = pDivisionId
    order by division_id, employee_fullname, employee_ucsd_id, sos;
END
GO

DROP PROCEDURE IF EXISTS spGetRevenueWorksheetByDivisionIdAndSos
GO

CREATE PROCEDURE spGetRevenueWorksheetByDivisionIdAndSos(IN pDivisionId int, IN pSos varchar(255))
BEGIN
    select * from budget.v_medgrp_revenue_worksheet_base rev where division_id = pDivisionId and sos = pSos
    union all
    select * from budget.v_medgrp_revenue_worksheet_12mo_only where division_id = pDivisionId and sos = pSos
    order by division_id, employee_fullname, employee_ucsd_id, sos;
END
GO


DROP PROCEDURE IF EXISTS spGetRevenueAdjustmentsByDivision
GO

CREATE PROCEDURE spGetRevenueAdjustmentsByDivision(IN pDivisionId int)
BEGIN
	SELECT
	    concat(c.division_id,'-',p.PL_CATEGORY) as id, c.division_id, c.total_proj_charges, p.PL_CATEGORY, 
	    round(c.total_proj_charges * p.PL_CHARGE_PERC,2) as total_proj_pl_charges,
	    p.PL_CHARGE_PERC,
	    p.BAD_DEBT_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC) as BAD_DEBT_ADJ,
	    p.CAPITATION_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) as CAP_ADJ, 
	    p.OTHER_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC) as OTHER_ADJ, 
	
	    (round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC)  +
	    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) +
	    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC)) as TOTAL_ADJ,
	    
	    (round(c.total_proj_charges * p.PL_CHARGE_PERC,2) + 
	
	    (round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC)  +
	    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) +
	    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC))) as TOTAL_ADJ_CHARGES
	
	FROM (
		select rev.division_id as id, rev.division_id, _latin1'INP/OUTP' as sos, 
		    ifnull(sum(rev.proj_charges),0.00) as total_proj_charges
		from budget.v_medgrp_revenue_worksheet_base rev
		left outer join budget.revenue_prior_actuals_by_div py on py.division_id=rev.division_id and rev.sos = py.sos and py.division_id = pDivisionId
		left outer join budget.mv_revenue_12mo_actual_cctw_totals_by_div roll on roll.somDiv = rev.division_id and roll.somDiv = pDivisionId
	    where rev.division_id = pDivisionId
		group by rev.division_id
		
        UNION ALL

        SELECT e.division_id as id, e.division_id, _latin1'INP/OUTP' as sos, 
        sum(ifnull(e.proj_cap_charges, 0) + ifnull(e.proj_under_charges,0) + ifnull(e.proj_ffs_charges, 0)) as total_proj_charges
        FROM budget.v_revenue_budget_exceptions e
        WHERE e.division_id = pDivisionId     		
	) c
	JOIN budget.MV_ADJ_PERC_BY_DIV p on c.division_id = p.SOM_DIVISION_ID AND p.SOM_DIVISION_ID = pDivisionId
	;
END
GO


DROP PROCEDURE IF EXISTS spGetRevenueAdjustmentsTotalByDivision
GO

CREATE PROCEDURE spGetRevenueAdjustmentsTotalByDivision(IN pDivisionId int)
BEGIN
	SELECT 
	    pl.division_id as DIVISION_ID, 
	    cast(max(total_proj_charges) as decimal(24,4)) as TOTAL_PROJ_CHARGES, 
	    cast(sum(BAD_DEBT_ADJ) as decimal(24,4)) as TOTAL_BAD_DEBT_ADJ, 
        cast((sum(BAD_DEBT_ADJ) / max(total_proj_charges)) as decimal(24,4)) as TOTAL_BAD_DEBT_ADJ_PERC,
	    cast(sum(CAP_ADJ) as decimal(24,4)) as TOTAL_CAP_ADJ, 
        cast((sum(CAP_ADJ) / max(total_proj_charges)) as decimal(24,4)) as TOTAL_CAP_ADJ_PERC,
	    cast(sum(OTHER_ADJ) as decimal(24,4)) as TOTAL_OTHER_ADJ, 
        cast((sum(OTHER_ADJ) / max(total_proj_charges)) as decimal(24,4)) as TOTAL_OTHER_ADJ_PERC,
	    cast(sum(TOTAL_ADJ) as decimal(24,4)) as TOTAL_ADJ, 
        cast((sum(TOTAL_ADJ) / max(total_proj_charges)) as decimal(24,4)) as TOTAL_ADJ_PERC,
	    cast(sum(TOTAL_ADJ_CHARGES) as decimal(24,4))as TOTAL_ADJ_CHARGES,
	    cast(IFNULL(max(cap.amount), 0) as decimal(24,4)) as TOTAL_CAP_DISTRIBUTION, 
	    cast( max(total_proj_charges) + sum(TOTAL_ADJ) + IFNULL(max(cap.amount), 0) as decimal(24,4)) as TOTAL_EXPECTED_REVENUE
	FROM (
	    select 
	        concat(c.division_id,'-',p.PL_CATEGORY) as id, c.division_id, c.total_proj_charges, p.PL_CATEGORY, 
	        round(c.total_proj_charges * p.PL_CHARGE_PERC,2) as total_proj_pl_charges,
	        p.PL_CHARGE_PERC,
	        p.BAD_DEBT_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC) as BAD_DEBT_ADJ,
	        p.CAPITATION_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) as CAP_ADJ, 
	        p.OTHER_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC) as OTHER_ADJ, 
	
	        (round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC)  +
	        round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) +
	        round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC)) as TOTAL_ADJ,
	
	        (round(c.total_proj_charges * p.PL_CHARGE_PERC,2) + 
	
	        (round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC)  +
	        round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) +
	        round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC))) as TOTAL_ADJ_CHARGES
	
	    FROM (
	        select rev.division_id as id, rev.division_id, _latin1'INP/OUTP' as sos, 
	            ifnull(sum(rev.proj_charges),0.00) as total_proj_charges
	        from budget.v_medgrp_revenue_worksheet_base rev
	        left outer join budget.revenue_prior_actuals_by_div py on py.division_id=rev.division_id and rev.sos = py.sos and py.division_id = pDivisionId
	        left outer join budget.mv_revenue_12mo_actual_cctw_totals_by_div roll on roll.somDiv = rev.division_id and roll.somDiv = pDivisionId
	        where rev.division_id = pDivisionId
	        group by rev.division_id
	        
            UNION ALL

            SELECT e.division_id as id, e.division_id, _latin1'INP/OUTP' as sos, 
            sum(ifnull(e.proj_cap_charges, 0) + ifnull(e.proj_under_charges,0) + ifnull(e.proj_ffs_charges, 0)) as total_proj_charges
            FROM budget.v_revenue_budget_exceptions e
            WHERE e.division_id = pDivisionId     
	    ) c
	    JOIN budget.MV_ADJ_PERC_BY_DIV p on c.division_id = p.SOM_DIVISION_ID AND p.SOM_DIVISION_ID = pDivisionId
	) pl
	LEFT JOIN budget.medgrp_revenue_cap_distribution cap on cap.division_id = pl.division_id and cap.division_id = pDivisionId
	GROUP BY division_id
;
END
GO

/* Permissions view is slow */
create or replace view budget.v_medgrp_revenue_division as
select distinct division_id from budget.medgrp_revenue_worksheet
union all
select 1 from dual
GO

DROP PROCEDURE IF EXISTS spGetPaymentInformationByDivision
GO

CREATE PROCEDURE spGetPaymentInformationByDivision(IN pDivisionId int)
BEGIN
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
    join (
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
		left outer join budget.revenue_prior_actuals_by_div py on py.division_id=rev.division_id and rev.sos = py.sos and py.division_id = pDivisionId
		left outer join budget.mv_revenue_12mo_actual_cctw_totals_by_div roll on roll.somDiv = rev.division_id and roll.somDiv = pDivisionId
	    where rev.division_id = pDivisionId
		group by rev.division_id
    ) proj_totals on proj_totals.division_id = rev.division_id and proj_totals.sos = rev.sos
    where rev.division_id = pDivisionId
    ;
END
GO