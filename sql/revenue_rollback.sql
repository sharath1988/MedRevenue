-- TODO Test this
drop procedure if exists budget.revenueRollback
/


create procedure budget.revenueRollback()
BEGIN
	
	-- DROP TABLES
	------------------------------------------------------
 	drop table budget.medgrp_revenue_inpatient_perc;      
 	drop table budget.medgrp_revenue_other_income;        
 	drop table budget.medgrp_revenue_payment_information; 
 	drop table budget.medgrp_revenue_worksheet;	
	drop table budget.budget_developer_impersonator;
	drop table budget.budgetMetadata;
 	drop table budget.mv_revenue_12mo_actual_cctw_totals;                
 	drop table budget.mv_revenue_12mo_actual_cctw_totals_by_div;         
 	drop table budget.mv_revenue_12mo_actual_cctw_totals_by_div_and_sos; 
 	drop table budget.mv_revenue_prior_actuals_cctw_by_category_and_sos;
 	drop table budget.revenue_prior_actuals_by_div;          
 	drop table budget.revenue_prior_actuals_by_prov;
 	drop table budget.lu_vchs_revenue_provider;
	
	-- DROP VIEWS
	------------------------------------------------------
	drop view v_collections_to_trvus_rates;                           
 	drop view v_collections_to_trvus_rates_by_prov;                               
	drop view v_employee_division;                                    
	drop view v_medgrp_revenue_dept;                                  
	drop view v_medgrp_revenue_inpatient_perc_psych;                  
	drop view v_medgrp_revenue_payment_information;                   
	drop view v_medgrp_revenue_payment_information_psych;             
	drop view v_medgrp_revenue_worksheet;                             
	drop view v_medgrp_revenue_worksheet_12mo_only;                   
	drop view v_medgrp_revenue_worksheet_base;                        
	drop view v_medgrp_revenue_worksheet_totals;                      
	drop view v_medgrp_revenue_worksheet_totals_psych;               
	drop view v_proj_trvus_by_prov;                                   
	drop view v_revenue_budget_exceptions;                            
	drop view v_revenue_prior_actual_split_perc_by_category_and_div;
	drop view v_revenue_prior_actual_split_perc_by_category_and_prov; 
	drop view v_som_division;
	
	-- DROP FUNCTIONS
	------------------------------------------------------
	drop function budget.calculateProjectedTrvus;
	drop function budget.calculateTrvusByCategory;
	drop function budget.calculateProjectedCharges;
	drop function budget.calculateProjectedCollections;
	drop function budget.calculateCollectionsByCategory;

END
/

call budget.revenueRollback()
/

drop procedure if exists budget.revenueRollback
/