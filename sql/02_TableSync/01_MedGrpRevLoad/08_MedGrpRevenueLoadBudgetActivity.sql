use budget
GO

drop procedure if exists budget.loadBudgetActivity
GO

/*
 * This proc loads any existing budget activity
 * 
 */
create procedure budget.loadBudgetActivity()
BEGIN
	
	-- payment info, (0 records)
	update budget.medgrp_revenue_payment_information mg
	join BackupArea.v_medgrp_revenue_payment_information_activity bu on bu.division_id=mg.division_id
	set mg.proj_cap_collections_to_trvus_perc_chg = bu.proj_cap_collections_to_trvus_perc_chg,
	    mg.proj_under_collections_to_trvus_perc_chg = bu.proj_under_collections_to_trvus_perc_chg,
	    mg.proj_ffs_collections_to_trvus_perc_chg = bu.proj_ffs_collections_to_trvus_perc_chg
	where bu.proj_cap_collections_to_trvus_perc_chg <> 0 or bu.proj_under_collections_to_trvus_perc_chg <> 0 or bu.proj_ffs_collections_to_trvus_perc_chg <> 0;
	
	-- copay (4 records)
	update budget.medgrp_revenue_payment_information mg
	join BackupArea.v_medgrp_revenue_payment_information_activity bu on bu.division_id = mg.division_id
	set mg.proj_copay = bu.proj_copay
	where (bu.proj_copay <> bu.prior_copay);
	
	-- Other income (zero records)
	insert into budget.medgrp_revenue_other_income select * from BackupArea.medgrp_revenue_other_income_activity;
	
	-- inpatient perc (2 records)
	update budget.medgrp_revenue_inpatient_perc mg
	join BackupArea.medgrp_revenue_inpatient_perc_activity bu on bu.division_id=mg.division_id
	set mg.proj_inpatient_percentage = bu.proj_inpatient_percentage
	where bu.prior_inpatient_percentage <> bu.proj_inpatient_percentage;
	
	-- Proj wrvus (285 records)
	update budget.medgrp_revenue_worksheet mg
	join budget.v_medgrp_revenue_worksheet mgv on mgv.id = mg.id
	join BackupArea.v_medgrp_revenue_worksheet_activity bu on bu.employee_fullname = mgv.employee_fullname and bu.division_id = mgv.division_id and bu.employee_ucsd_id=mgv.employee_ucsd_id and bu.sos=mgv.sos
	set mg.proj_wrvus = bu.proj_wrvus
	where bu.proj_wrvus <> bu.py_wrvus;
	
	-- Proj wrvus for providers added in
	update budget.medgrp_revenue_worksheet mg
	join budget.v_medgrp_revenue_worksheet mgv on mgv.id = mg.id
	right outer join BackupArea.v_medgrp_revenue_worksheet_activity bu on bu.employee_fullname = mgv.employee_fullname and bu.division_id = mgv.division_id and bu.employee_ucsd_id=mgv.employee_ucsd_id and bu.sos=mgv.sos
	set mg.proj_wrvus = bu.proj_wrvus
	where bu.removable_flag='Y' and mgv.division_id is not null;	
END
GO

call budget.loadBudgetActivity()
GO

/*
    select mg.worksheet_desc_id, mg.sos, mgv.employee_fullname, mgv.employee_ucsd_id, mg.division_id, mg.proj_wrvus, bu.proj_wrvus, bu.py_wrvus from budget.medgrp_revenue_worksheet mg
	join budget.v_medgrp_revenue_worksheet mgv on mgv.id = mg.id
	join BackupArea.v_medgrp_revenue_worksheet_activity bu on bu.employee_fullname = mgv.employee_fullname and bu.division_id = mgv.division_id and bu.employee_ucsd_id=mgv.employee_ucsd_id and bu.sos=mgv.sos
	where bu.proj_wrvus <> bu.py_wrvus and bu.proj_wrvus <> 0
    order by mg.division_id, mgv.employee_fullname
*/