/*
VALIDATIONS
*/

/*
Expected result:  0 records
*/
select * from v_medgrp_revenue_payment_information bu 
where bu.proj_cap_collections_to_trvus_perc_chg <> 0 or bu.proj_under_collections_to_trvus_perc_chg <> 0 or bu.proj_ffs_collections_to_trvus_perc_chg <> 0
GO

/*
Expected result: 4 records
*/
select * from v_medgrp_revenue_payment_information bu 
where (bu.proj_copay <> bu.prior_copay)
GO

/*
Expected result:  0 records
*/
select * from budget.medgrp_revenue_other_income
GO

/*
Expected result:  2 records
*/
select * from budget.medgrp_revenue_inpatient_perc where prior_inpatient_percentage <> proj_inpatient_percentage
GO

/*
Expected result: (19 rows)
*/
select mgv.division_id, mgv.employee_fullname, mgv.employee_ucsd_id, mgv.proj_wrvus, bu.proj_wrvus, bu.employee_fullname, bu.employee_ucsd_id
from budget.v_medgrp_revenue_worksheet mgv 
right outer join BackupArea.v_medgrp_revenue_worksheet_activity bu on bu.employee_fullname = mgv.employee_fullname and bu.division_id = mgv.division_id and bu.employee_ucsd_id=mgv.employee_ucsd_id and bu.sos=mgv.sos
where bu.removable_flag='Y' and mgv.division_id is not null
GO

--- additions for Rose
select distinct sr.deptId, sr.deptName, sr.divId, sr.divName, bu.proj_wrvus, bu.employee_fullname, bu.employee_ucsd_id
from budget.v_medgrp_revenue_worksheet mgv 
right outer join BackupArea.v_medgrp_revenue_worksheet_activity bu on bu.employee_fullname = mgv.employee_fullname and bu.division_id = mgv.division_id and bu.employee_ucsd_id=mgv.employee_ucsd_id and bu.sos=mgv.sos
join som_portal.som_rollup sr on sr.divId = bu.division_id
where bu.removable_flag='Y' and mgv.division_id is null
GO