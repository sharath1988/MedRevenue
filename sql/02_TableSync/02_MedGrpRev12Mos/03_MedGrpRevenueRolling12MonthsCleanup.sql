drop procedure if exists budget.medGrp12MosRevenueCleanup
GO

create procedure budget.medGrp12MosRevenueCleanup()
BEGIN

	/* Cleanup rolling 12mo tables */
	drop table if exists budget.revenue_12mo_actual_cctw_by_category;
	drop table if exists budget.revenue_12mo_actual_cctw_by_category_and_div;
	drop table if exists budget.revenue_12mo_actual_cctw_by_category_and_sos;
	drop table if exists budget.revenue_12mo_actual_cctw_totals;
	drop table if exists budget.revenue_12mo_actual_cctw_totals_by_sos;

END
GO

call budget.medGrp12MosRevenueCleanup()
GO

drop procedure if exists budget.medGrp12MosRevenueCleanup
GO