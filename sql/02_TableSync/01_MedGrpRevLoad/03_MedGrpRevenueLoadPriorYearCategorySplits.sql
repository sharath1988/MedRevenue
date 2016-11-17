/**
 * This procedure is responsible for loading revenue category 
 * split percentages from the prior year budget database
 * 
 */
drop procedure if exists budget.loadPriorYearRevenueCategorySplits
GO

create procedure budget.loadPriorYearRevenueCategorySplits()
BEGIN

    /*
    * division wRVU totals for prior year.
    */
    drop table if exists budget.tmp_revenue_prior_actual_totals_by_category_and_div;

    create table budget.tmp_revenue_prior_actual_totals_by_category_and_div as
	select division_id, total_cap_wrvus, total_under_wrvus, total_ffs_wrvus, sos,
	    (total_cap_wrvus + total_under_wrvus + total_ffs_wrvus) as total_wrvus_amount
	from (
	    select somDiv as division_id, 'INP/OUTP' as sos,
	        round(sum(cap_wrvus),2) as total_cap_wrvus,
	        round(sum(under_wrvus),2) as total_under_wrvus,
	        round(sum(ffs_wrvus),2) as total_ffs_wrvus
	    from budget.revenue_prior_actual_cctw_by_category py
	    group by somDiv
        union all
	    select somDiv as division_id, sos,
	        round(sum(cap_wrvus),2) as total_cap_wrvus,
	        round(sum(under_wrvus),2) as total_under_wrvus,
	        round(sum(ffs_wrvus),2) as total_ffs_wrvus
	    from budget.mv_revenue_prior_actuals_cctw_by_category_and_sos py
        where somDiv = 22
	    group by somDiv, sos
	    order by division_id
	) q;
        

    /*
    * Total revenue percentage splits by division
    */
    drop table if exists budget.revenue_prior_actual_split_perc_by_category_and_div;

    create table budget.revenue_prior_actual_split_perc_by_category_and_div as
    select dt.division_id, dt.sos,
        round((dt.total_wrvus_amount), 4) as total_wrvus_amount,
        dt.total_cap_wrvus,
        ifnull (round((dt.total_cap_wrvus / (dt.total_wrvus_amount)), 8), 0) as cap_wrvus_split_percentage,
        dt.total_under_wrvus,
        ifnull (round((dt.total_under_wrvus / (dt.total_wrvus_amount)), 8), 0) as under_wrvus_split_percentage,
        dt.total_ffs_wrvus,
        ifnull (round((dt.total_ffs_wrvus / (dt.total_wrvus_amount)), 8), 0) as ffs_wrvus_split_percentage
    from budget.tmp_revenue_prior_actual_totals_by_category_and_div dt;
    
    alter table budget.revenue_prior_actual_split_perc_by_category_and_div add index division_id (division_id);
    alter table budget.revenue_prior_actual_split_perc_by_category_and_div add index sos (sos);
    
    /*
    * provider totals for prior year
    */
    drop table if exists budget.tmp_revenue_prior_actual_totals_by_category_and_prov;

    create table budget.tmp_revenue_prior_actual_totals_by_category_and_prov as
    select worksheet_desc_id, somDiv as division_id, 'INP/OUTP' as sos,
        round((cap_wrvus),2) as total_cap_wrvus,
        round((under_wrvus),2) as total_under_wrvus,
        round((ffs_wrvus),2) as total_ffs_wrvus,
        round((cap_wrvus + under_wrvus + ffs_wrvus),2) as total_wrvus
    from budget.revenue_prior_actual_cctw_by_category py
    where py.somDiv <> 22
    union all 
    select worksheet_desc_id, somDiv as division_id, sos,
        round((cap_wrvus),2) as total_cap_wrvus,
        round((under_wrvus),2) as total_under_wrvus,
        round((ffs_wrvus),2) as total_ffs_wrvus,
        round((cap_wrvus + under_wrvus + ffs_wrvus),2) as total_wrvus
    from budget.mv_revenue_prior_actuals_cctw_by_category_and_sos py
    where py.somDiv = 22
    order by division_id;
    
    drop table if exists budget.tmp_revenue_prior_actual_totals_by_category_and_prov_sums;

    /*
     * Table that Groups by division and employee
     */
    create table tmp_revenue_prior_actual_totals_by_category_and_prov_sums as
    select worksheet_desc_id, division_id, sos,
        sum(total_cap_wrvus) as total_cap_wrvus,
        sum(total_under_wrvus) as total_under_wrvus,
        sum(total_ffs_wrvus) as total_ffs_wrvus,
        sum(total_wrvus) as total_wrvus
    from tmp_revenue_prior_actual_totals_by_category_and_prov
    group by worksheet_desc_id, division_id, sos;
    
    /*
    * provider-level category percentage splits 
    */
    drop table if exists budget.tmp_revenue_prior_actual_split_perc_by_category_and_prov;

    create table budget.tmp_revenue_prior_actual_split_perc_by_category_and_prov as
    select 
        pt.division_id, pt.worksheet_desc_id, pt.sos, dt.total_wrvus_amount, pt.total_wrvus,
        pt.total_cap_wrvus, round((pt.total_cap_wrvus / pt.total_wrvus), 8) as total_perc_cap_wrvus,
        pt.total_under_wrvus, round((pt.total_under_wrvus / pt.total_wrvus), 8) as total_perc_under_wrvus,
        pt.total_ffs_wrvus, round((pt.total_ffs_wrvus / pt.total_wrvus), 8) as total_perc_ffs_wrvus,
        dt.cap_wrvus_split_percentage, dt.under_wrvus_split_percentage, dt.ffs_wrvus_split_percentage
    from budget.tmp_revenue_prior_actual_totals_by_category_and_prov_sums pt
    join budget.revenue_prior_actual_split_perc_by_category_and_div dt on dt.division_id = pt.division_id and pt.sos = dt.sos;
                

    drop table if exists budget.revenue_prior_actual_split_perc_by_category_and_prov;
    
	create table budget.revenue_prior_actual_split_perc_by_category_and_prov as
	select division_id, worksheet_desc_id, sos, total_wrvus as total_prov_wrvus,
	    ifnull(total_cap_wrvus,0) as total_cap_wrvus,
	    ifnull(total_perc_cap_wrvus,0) as perc_cap_wrvus,
	    ifnull(total_under_wrvus,0) as total_under_wrvus,
	    ifnull(total_perc_under_wrvus,0) as perc_under_wrvus,
	    ifnull(total_ffs_wrvus,0) as total_ffs_wrvus,
	    ifnull(total_perc_ffs_wrvus,0) as perc_ffs_wrvus
	from budget.tmp_revenue_prior_actual_split_perc_by_category_and_prov;
	
    alter table budget.revenue_prior_actual_split_perc_by_category_and_prov add index division_id (division_id);
    alter table budget.revenue_prior_actual_split_perc_by_category_and_prov add index worksheet_desc_id (worksheet_desc_id);
    alter table budget.revenue_prior_actual_split_perc_by_category_and_prov add index sos (sos);
    alter table budget.revenue_prior_actual_split_perc_by_category_and_prov add column id int auto_increment primary key;	
	
    drop table if exists budget.tmp_revenue_prior_actual_totals_by_category_and_prov;
    drop table if exists budget.tmp_revenue_prior_actual_totals_by_category_and_div;
    drop table if exists budget.tmp_revenue_prior_actual_split_perc_by_category_and_prov;	
    drop table if exists budget.tmp_revenue_prior_actual_totals_by_category_and_prov_sums;
      
END
GO

call budget.loadPriorYearRevenueCategorySplits()
GO

drop procedure if exists budget.loadPriorYearRevenueCategorySplits
GO