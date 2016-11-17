/**
 * Load revenue budget exceptions from PROVIDER worksheet
 * 
 * This staging table will be used to populate the following tables:
 * 	lu_vchs_revenue_provider
 * 	revenue_prior_actuals_by_prov
 *  medgrp_revenue_worksheet
 */
CREATE TABLE stg_revenue_budget_exceptions_prov_load (
  provider_name varchar(128) DEFAULT NULL,
  employee_ucsd_id int(11) DEFAULT NULL,
  som_division_id int(11) DEFAULT NULL,
  projected_wrvus varchar(150) DEFAULT NULL,
  cap_charges decimal(65,4) DEFAULT NULL,
  under_charges decimal(65,4) DEFAULT NULL,
  ffs_charges decimal(65,4) DEFAULT NULL,
  total_charges decimal(65,4) DEFAULT NULL,
  cap_collections decimal(65,4) DEFAULT NULL,
  under_collections decimal(65,4) DEFAULT NULL,
  ffs_collections decimal(65,4) DEFAULT NULL,
  total_collections decimal(65,4) DEFAULT NULL,
  cap_trvus decimal(65,4) DEFAULT NULL,
  under_trvus decimal(65,4) DEFAULT NULL,
  ffs_trvus decimal(65,4) DEFAULT NULL,
  total_trvus decimal(65,4) DEFAULT NULL,
  cap_wrvus decimal(65,4) DEFAULT NULL,
  under_wrvus decimal(65,4) DEFAULT NULL,
  ffs_wrvus decimal(65,4) DEFAULT NULL,
  total_wrvus decimal(65,4) DEFAULT NULL,
  copay decimal(20,4) DEFAULT NULL,
  cap_wrvu_split decimal(30,8) DEFAULT NULL,
  under_wrvu_split decimal(30,8) DEFAULT NULL,
  ffs_wrvu_split decimal(30,8) DEFAULT NULL,
  cap_collection_rate decimal(30,8) DEFAULT NULL,
  under_collection_rate decimal(30,8) DEFAULT NULL,
  ffs_collection_rate decimal(30,8) DEFAULT NULL
) ENGINE=InnoDB
/

/**
 * Load revenue budget exceptions from DIVISION worksheet
 * 
 * This staging table will be used to populate the following tables:
 * 	revenue_prior_actuals_by_div
 * 	medgrp_revenue_payment_information
 * 	medgrp_revenue_inpatient_perc
 *  medgrp_revenue_other_income
 */
CREATE TABLE stg_revenue_budget_exceptions_div_load (
  division_id int(11) DEFAULT NULL,
  inpatient_percentage decimal(62,4) DEFAULT NULL,
  projected_inpatient_percentage decimal(62,4) DEFAULT NULL,
  projected_copay decimal(20,4) DEFAULT NULL,
  proj_cap_rate_percent_change decimal(20,4) DEFAULT NULL,
  proj_under_rate_percent_change decimal(20,4) DEFAULT NULL,
  proj_ffs_rate_percent_change decimal(20,4) DEFAULT NULL,
  other_income_co_payment int(11) DEFAULT NULL,
  other_income_incentive int(11) DEFAULT NULL,
  other_income_membership int(11) DEFAULT NULL,
  other_income_stip int(11) DEFAULT NULL,
  other_income_other int(11) DEFAULT NULL,
  cap_charges decimal(30,8) DEFAULT NULL,
  under_charges decimal(30,8) DEFAULT NULL,
  ffs_charges decimal(30,8) DEFAULT NULL,
  total_charges decimal(30,8) DEFAULT NULL,
  cap_collections decimal(30,8) DEFAULT NULL,
  under_collections decimal(30,8) DEFAULT NULL,
  ffs_collections decimal(30,8) DEFAULT NULL,
  total_collections decimal(30,8) DEFAULT NULL,
  cap_trvus decimal(30,8) DEFAULT NULL,
  under_trvus decimal(30,8) DEFAULT NULL,
  ffs_trvus decimal(30,8) DEFAULT NULL,
  total_trvus decimal(30,8) DEFAULT NULL,
  cap_wrvus decimal(30,8) DEFAULT NULL,
  under_wrvus decimal(30,8) DEFAULT NULL,
  ffs_wrvus decimal(30,8) DEFAULT NULL,
  total_wrvus decimal(30,8) DEFAULT NULL,
  copay decimal(30,8) DEFAULT NULL,
  cap_wrvu_split decimal(30,8) DEFAULT NULL,
  under_wrvu_split decimal(30,8) DEFAULT NULL,
  ffs_wrvu_split decimal(30,8) DEFAULT NULL,
  cap_collection_rate decimal(30,8) DEFAULT NULL,
  under_collection_rate decimal(30,8) DEFAULT NULL,
  ffs_collection_rate decimal(30,8) DEFAULT NULL
) ENGINE=InnoDB
/

/**
 * Audit tables for budget records that do not have a valid som_division
 */
CREATE TABLE stg_revenue_budget_exceptions_prov_load_rejects (
  division_id int(11) DEFAULT NULL
) ENGINE=InnoDB
/


CREATE TABLE stg_revenue_budget_exceptions_div_load_rejects (
  division_id int(11) DEFAULT NULL
) ENGINE=InnoDB
/