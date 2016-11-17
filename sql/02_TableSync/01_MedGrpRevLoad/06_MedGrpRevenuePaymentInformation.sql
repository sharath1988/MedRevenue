drop procedure if exists budget.loadMedGrpPaymentInformation
GO

/*
 * This must be called after the call to loadPriorYearActuals
 * 
 * Do not drop revenue_prior_actual_cctw_totals_by_sos
 * 
 */
create procedure budget.loadMedGrpPaymentInformation()
BEGIN

    drop table if exists budget.medgrp_revenue_payment_information;

    create table budget.medgrp_revenue_payment_information (
        id int primary key auto_increment,
        division_id int,
        sos varchar(32),
        proj_cap_collections_to_trvus_perc_chg decimal(20,4),
        proj_under_collections_to_trvus_perc_chg decimal(20,4),
        proj_ffs_collections_to_trvus_perc_chg decimal(20,4),
        proj_copay decimal(20,4),
        py_actual_id int(11)
    ) ENGINE=InnoDB;

    insert into budget.medgrp_revenue_payment_information (division_id, sos,
        proj_cap_collections_to_trvus_perc_chg, proj_under_collections_to_trvus_perc_chg, proj_ffs_collections_to_trvus_perc_chg, proj_copay,
        py_actual_id)
	select py.division_id, py.sos, 0.00, 0.00, 0.00, py.copay as proj_copay, py.py_actual_id
	from revenue_prior_actuals_by_div py;

    alter table budget.medgrp_revenue_payment_information add index division_id (division_id);
    alter table budget.medgrp_revenue_payment_information add index sos (sos);
    alter table budget.medgrp_revenue_payment_information add index py_actual_id (py_actual_id);
    
    alter table budget.medgrp_revenue_payment_information add constraint fk_py_actual_id_div foreign key (py_actual_id) references budget.revenue_prior_actuals_by_div(py_actual_id);	    
        
    /**
     * Create INPATIENT PERCENTAGE table
     */
    drop table if exists budget.medgrp_revenue_inpatient_perc;
    
    
    create table budget.medgrp_revenue_inpatient_perc (
		 division_id                 int(11) PRIMARY KEY,               
		 total_inpatient_trvus       decimal(65,4),               
		 total_div_trvus             decimal(65,4),               
		 prior_inpatient_percentage  decimal(62,4),               
		 proj_inpatient_percentage   decimal(62,4)              
    ) ENGINE=InnoDB;
    
    insert into budget.medgrp_revenue_inpatient_perc (division_id, total_inpatient_trvus, total_div_trvus, prior_inpatient_percentage, proj_inpatient_percentage)
	select
	    divTotals.somDiv as division_id,
	    ifnull(allInpatients.trvus,0) as total_inpatient_trvus, 
	    divTotals.total_trvus total_div_trvus,
	    ifnull(round((allInpatients.trvus / divTotals.total_trvus),4),0) as prior_inpatient_percentage,
        ifnull(round((allInpatients.trvus / divTotals.total_trvus),4),0) as proj_inpatient_percentage
	from budget.revenue_prior_actual_cctw_totals_by_div divTotals 
	left outer join (
	    select somDiv, sos, sum(trvus) as trvus from revenue_prior_actual_cctw_totals_by_sos where sos='INPATIENT' group by somDiv, sos
	) allInpatients on allInpatients.somDiv = divTotals.somDiv
	order by divTotals.somDiv;  
	
	alter table budget.medgrp_revenue_inpatient_perc add index division_id (division_id);
    
END
GO

call budget.loadMedGrpPaymentInformation()
GO

drop procedure if exists budget.loadMedGrpPaymentInformation
GO