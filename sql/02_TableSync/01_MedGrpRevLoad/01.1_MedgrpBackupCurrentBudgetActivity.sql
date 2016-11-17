use budget
GO

/**
 * Create a backup of existing tables
 */
drop procedure if exists budget.backupCurrentBudgetActivity
GO

create procedure budget.backupCurrentBudgetActivity()
BEGIN
	DROP TABLE IF EXISTS BackupArea.medgrp_revenue_worksheet_desc_activity;
	create table BackupArea.medgrp_revenue_worksheet_desc_activity as select * from budget.medgrp_revenue_worksheet_desc;
	
	DROP TABLE IF EXISTS BackupArea.v_medgrp_revenue_worksheet_activity;
	create table IF NOT EXISTS BackupArea.v_medgrp_revenue_worksheet_activity as select * from budget.v_medgrp_revenue_worksheet;
	
	DROP TABLE IF EXISTS BackupArea.v_medgrp_revenue_payment_information_activity;
	create table IF NOT EXISTS BackupArea.v_medgrp_revenue_payment_information_activity as select * from budget.v_medgrp_revenue_payment_information;
	
	DROP TABLE IF EXISTS BackupArea.medgrp_revenue_other_income_activity;
	create table IF NOT EXISTS BackupArea.medgrp_revenue_other_income_activity as select * from budget.medgrp_revenue_other_income;
	
	DROP TABLE IF EXISTS BackupArea.medgrp_revenue_inpatient_perc_activity;
	create table IF NOT EXISTS BackupArea.medgrp_revenue_inpatient_perc_activity as select * from budget.medgrp_revenue_inpatient_perc;
END
GO

call budget.backupCurrentBudgetActivity()
GO