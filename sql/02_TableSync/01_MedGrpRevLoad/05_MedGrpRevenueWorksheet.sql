/**
 * Creates the revenue worksheet table for projections
 */
drop procedure if exists budget.loadRevenueWorksheet
GO

create procedure budget.loadRevenueWorksheet()
BEGIN    
	
	/*
    *  CREATE budget (current fiscal year revenue worksheet)
    */
    drop table if exists budget.medgrp_revenue_worksheet;

	CREATE TABLE budget.medgrp_revenue_worksheet (
        id int(11) primary key not null auto_increment,
        py_actual_id int(11),
        worksheet_desc_id INT NOT NULL,
   		sos varchar(40),
        division_id int(11) NOT NULL,
        proj_wrvus decimal(65,4),
        exception_flag char(1) NOT NULL DEFAULT 'N',
        care_payment_rate decimal(24,4) NULL,
        FOREIGN KEY (worksheet_desc_id) REFERENCES budget.medgrp_revenue_worksheet_desc(worksheet_desc_id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

    alter table budget.medgrp_revenue_worksheet add index division_id (division_id);
    alter table budget.medgrp_revenue_worksheet add index sos (sos);
    alter table budget.medgrp_revenue_worksheet add index py_actual_id (py_actual_id);
    
    drop table if exists mv_revenue_prior_actuals_by_prov_sum
    ;
    
    create table mv_revenue_prior_actuals_by_prov_sum as
    select 
        min(py_actual_id) as py_actual_id, 
        worksheet_desc_id, division_id, 
        sum(cap_charges) as cap_charges, sum(under_charges) as under_charges, sum(ffs_charges) as ffs_charges, sum(total_charges) as total_charges,
        sum(cap_collections) as cap_collections, sum(under_collections) as under_collections, sum(ffs_collections) as ffs_collections, sum(total_collections) as total_collections,
        sum(cap_trvus) as cap_trvus, sum(under_trvus) as under_trvus, sum(ffs_trvus) as ffs_trvus, sum(total_trvus) as total_trvus,
        sum(cap_wrvus) as cap_wrvus, sum(under_wrvus) as under_wrvus, sum(ffs_wrvus) as ffs_wrvus, sum(total_wrvus) as total_wrvus,
        sum(copay) as copay, 
        'INP/OUTP' as sos,
        sum(cap_wrvus) / sum(total_wrvus) as cap_wrvu_split,
        sum(under_wrvus) / sum(total_wrvus) as under_wrvu_split,
        sum(ffs_wrvus) / sum(total_wrvus) as ffs_wrvu_split,
        max(cap_collection_rate),
        sum(under_collections) / sum(under_trvus) as under_collection_rate,
        sum(ffs_collections) / sum(ffs_trvus) as ffs_collection_rate
	from revenue_prior_actuals_by_prov 
	GROUP BY worksheet_desc_id, division_id
	;
	
	alter table mv_revenue_prior_actuals_by_prov_sum add index (py_actual_id)
	;
	
	alter table mv_revenue_prior_actuals_by_prov_sum add index (worksheet_desc_id, division_id)
	;
    
    insert into budget.medgrp_revenue_worksheet (py_actual_id, worksheet_desc_id, sos, division_id, proj_wrvus)
	select 
        py.py_actual_id, py.worksheet_desc_id, py.sos, py.division_id,
        (case when py.total_wrvus < 0 then 0 else py.total_wrvus end) as wrvus
    from mv_revenue_prior_actuals_by_prov_sum py;
    
	alter table budget.medgrp_revenue_worksheet add constraint fk_py_actual_id foreign key (py_actual_id) references budget.revenue_prior_actuals_by_prov(py_actual_id);    
    
	-- Clear out medgrp_revenue_other_income table
	DELETE FROM medgrp_revenue_other_income;
END
GO

call budget.loadRevenueWorksheet()
GO

drop procedure if exists budget.loadRevenueWorksheet
GO