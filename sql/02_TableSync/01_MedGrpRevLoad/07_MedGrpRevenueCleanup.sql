drop procedure if exists budget.medGrpRevenueCleanup
GO

create procedure budget.medGrpRevenueCleanup()
BEGIN

	/* Cleanup prior year tables */
	drop table if exists budget.mv_revenue_prior_actual_cctw_totals;
	drop table if exists budget.revenue_prior_actual_collections_per_trvus_by_prov;
	drop table if exists budget.revenue_prior_actual_split_perc_by_category_and_prov;
	drop table if exists budget.revenue_prior_actual_cctw_by_category_and_div;
	drop table if exists budget.revenue_prior_actual_split_perc_by_category_and_div;
	drop table if exists budget.revenue_prior_actual_collections_per_trvus;
	drop table if exists budget.tmp_revenue_prior_actual_cctw_by_category_and_sos;
	drop table if exists budget.revenue_prior_actual_cctw_by_category;
	drop table if exists budget.revenue_prior_actual_cctw_totals;
	drop table if exists budget.revenue_prior_actual_cctw_totals_by_div;
	drop table if exists budget.revenue_prior_actual_cctw_totals_by_div_and_sos;
	drop table if exists budget.revenue_prior_actual_cctw_totals_by_sos;
	
END
GO


call budget.medGrpRevenueCleanup()
GO

drop procedure if exists budget.medGrpRevenueCleanup
GO

/* 
 * reload already budgeted data
 */
