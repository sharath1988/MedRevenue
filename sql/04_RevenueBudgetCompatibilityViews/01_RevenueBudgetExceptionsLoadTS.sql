/* 
   Job Name:        RevenueBudgetExceptionsLoad
   

   Purpose:         This job loads from the Revenue Budget Exception staging tables (that must be populated  
                    manually using the Excel template) into the VCHS Revenue Budget application tables.

   Source Data:     budget.stg_revenue_budget_exceptions_prov_load
                    budget.stg_revenue_budget_exceptions_div_load


   Operation:       

   Scheduled Job    Runs on demand after a dbo has sent us a populated revenue budget exception template
   					and we have manually loaded it into the staging tables (see Source data)


   Written By:      Mike Yebba
   Date Written:    01/13/2015

   Modification History:
     Modifier's Initials   Date    Comment
     ---------------------------------------------------------------------------

*/
GO
-- 
--    Tablesync fields:
-- =========================
-- Column Name 	Description
-- Name 	    The name of the job
-- Type 	    They type of work we want to perform.
-- ReturnTable 	Where we will put the data.
-- Command 	    What we wish to execute
-- Ordinal 	    The order we will perform the Elements in the Job.
-- ConnAlias    The connection Alias we want to use for the remote data.
-- Enabled      Is the Job element enabled. A bit value of 0/1. A second security measure.
-- Comment 	    A short description of the Job element, displayed during a non-verbose Job run. 
-- 
-- 
--    Tablesync commands:
-- ========================
-- Use Type = Command to run a standard SQL command on the server pointed to by the ConnAlias 
--
-- Use Type = Create  to create a new table structure, drop any existing table is included.
--
-- Use Type = Upload  to move a new table/data from the ConnAlias (source) to the target table
--                      identified in the Command field.
-- [ PayPeriod  Upload   s_pp      SELECT* from $op-Award   10     Coeus       false  "No Comment" ]
-- 
-- Use Type = Swap    to replace an existing table with the exact same name and stucture from
--                       the source you specify in the Command field to the som_portal database
--                       on the server specified by the ConnAlias.
-- [ OverDraft  Swap overDraft   select * from overDraft  50  SomProd  true  "swap overDraft onto SomProd"]
--
--
--
-- Use Type = Push    to move table from the Command (source) to the ConnAlias (target), a table 
--                      with the same name can already exist on the target and it will overwrite it.
-- [ SomDept Push  ucsd_department    5000   SomDev2V5     true        "push ucsd_department to Dev2" ]
-- 
-- Use CreateFunc     to create a function
--  [StipFix  CreateFunc  periodStartDate  create function periodStartDate(period char(8)) RETURNS date...
--      135         JobServer     true        "create function periodStartDate"] 
-- 
-- Use CreateProc     to create a procedure
-- 
-- Use Type = Execute to execute a stored procedure or function
--  [SomDept   Execute  dw_org_status { call org_list(?) }  520 DW_DB2 true "upload org status list (DW_DB2)"]
--
-- Use Type = Drop    to drop a table from the ConnAlias database (target)
--  [PayPeriod  Drop     s_pp            (null)      1000        JobServer     true        "drop s_pp (JobServer)"] 
-- 
-- Use type = InsPdox  to move a paradox table to another paradox table
-- [ Index    InsPdox  IndxFyl   select * from ifoapal  410   PdoxLcl  false  "swap ifoapal to IndxFyl.db (test)" ]
-- 
-- Use Type = Procedure  to run a procedure
--  [ SomRollup	Procedure	(null)	{ call som_portal.createSomRollup() }	150	SomDev2V5	true	run createSomRollup Dev2 ]
-- 
-- 
-- 
-- 
--    Tablesync ConnAlias
--  ============================
-- 
--  Coeus      
--  CoeusT4      
--  Darwin       
--  DW_DB2       
--  FacLink     (server  
--  JobServer   (Server no longer exists) 
--  MdbHxtt      
--  MdbLcl       
--  MdbMed       
--  MdbMsp       
--  PdoxAlloc    
--  PdoxAsa      
--  PdoxCedfHca  
--  PdoxFsA      
--  PdoxGrant    
--  PdoxLcl      
--  PdoxTest     
--  pdx          
--  SomDev    (Server no longer exists)
--  SomDev2V4 (Server no longer exists)
--  SomDev2V5 (Server no longer exists)
--  SomPortal (server = som-prod2.ucsd.edu)
--  SomProd   (Server no longer exists)  
--  SomProd2  (server = som-prod2.ucsd.edu)
--  SomQA2V2  (Server no longer exists)
--  SomQA2V5  (server = som-qa2.ucsd.edu)

-- 
-- 
--
GO


/**
 * This job loads from the Revenue Budget Exception staging tables (that must be populated
 * manually using the Excel template) into the VCHS Revenue Budget application tables.
 */
drop procedure if exists budget.revenueBudgetExceptionsLoad
GO

create procedure budget.revenueBudgetExceptionsLoad()
BEGIN	
	ALTER TABLE budget.stg_revenue_budget_exceptions_prov_load ADD COLUMN id INT PRIMARY KEY AUTO_INCREMENT;
	
	DROP TABLE IF EXISTS budget.tmp_exception_worksheet_desc;
	
	CREATE TABLE budget.tmp_exception_worksheet_desc AS 
		SELECT
            stg.id,
			stg.provider_name,
			COALESCE(aev.employee_fullname,UPPER(REPLACE(stg.provider_name,'"','')),'*UNSPECIFIED PROVIDER') AS description,
			stg.employee_ucsd_id,
			aev.all_employee_id
		FROM
			budget.stg_revenue_budget_exceptions_prov_load stg
		LEFT OUTER JOIN
			dw.all_employee_view aev ON
				aev.employee_ucsd_id = stg.employee_ucsd_id
        ORDER BY
            stg.id;
				
	ALTER TABLE budget.tmp_exception_worksheet_desc ADD PRIMARY KEY (id);
	ALTER TABLE budget.tmp_exception_worksheet_desc ADD INDEX all_employee_id (all_employee_id);
	ALTER TABLE budget.tmp_exception_worksheet_desc ADD INDEX employee_ucsd_id (employee_ucsd_id);
	ALTER TABLE budget.tmp_exception_worksheet_desc ADD INDEX description (description);
	
	ALTER TABLE budget.tmp_exception_worksheet_desc ADD COLUMN worksheet_desc_id INT;
	
	UPDATE
		budget.tmp_exception_worksheet_desc ed
	JOIN
		budget.medgrp_revenue_worksheet_desc rwd ON
			rwd.all_employee_id = ed.all_employee_id OR
			((rwd.all_employee_id IS NULL AND ed.all_employee_id IS NULL) AND
				(
					(rwd.reference_number = ed.employee_ucsd_id OR
						(rwd.reference_number IS NULL AND ed.employee_ucsd_id IS NULL)) AND
					(rwd.description = ed.description)
				)
			)
	SET
		ed.worksheet_desc_id = rwd.worksheet_desc_id;
		
	INSERT INTO budget.medgrp_revenue_worksheet_desc (description, all_employee_id, reference_number)
		SELECT
			description, all_employee_id, employee_ucsd_id AS reference_number
		FROM
			budget.tmp_exception_worksheet_desc
		WHERE
			worksheet_desc_id IS NULL;
			
	UPDATE
		budget.tmp_exception_worksheet_desc ed
	JOIN
		budget.medgrp_revenue_worksheet_desc rwd ON
			rwd.all_employee_id = ed.all_employee_id OR
			((rwd.all_employee_id IS NULL AND ed.all_employee_id IS NULL) AND
				(
					(rwd.reference_number = ed.employee_ucsd_id OR
						(rwd.reference_number IS NULL AND ed.employee_ucsd_id IS NULL)) AND
					(rwd.description = ed.description)
				)
			)
	SET
		ed.worksheet_desc_id = rwd.worksheet_desc_id
	WHERE
		ed.worksheet_desc_id IS NULL;
		
	ALTER TABLE budget.tmp_exception_worksheet_desc MODIFY worksheet_desc_id INT NOT NULL;
	
	ALTER TABLE budget.tmp_exception_worksheet_desc DROP COLUMN provider_name;
	ALTER TABLE budget.tmp_exception_worksheet_desc DROP COLUMN description;
	ALTER TABLE budget.tmp_exception_worksheet_desc DROP COLUMN employee_ucsd_id;
	ALTER TABLE budget.tmp_exception_worksheet_desc DROP COLUMN all_employee_id;
	
	DROP TABLE IF EXISTS budget.tmp_prior_exception_worksheet;
	
	/**
	 * Load prior year actuals for budget exceptions by provider and division.
	 * Do not load if the division does not exist in som_portal.som_division.
	 */
	 
	insert into budget.revenue_prior_actuals_by_prov (
	    worksheet_desc_id, division_id,
	    cap_charges, under_charges, ffs_charges, total_charges,
	    cap_collections, under_collections, ffs_collections, total_collections,
	    cap_trvus, under_trvus, ffs_trvus, total_trvus,
	    cap_wrvus, under_wrvus, ffs_wrvus, total_wrvus,
	    copay,
	    sos,
	    cap_wrvu_split, under_wrvu_split, ffs_wrvu_split,
	    cap_collection_rate, under_collection_rate, ffs_collection_rate,
		external_src_ref
	)
	select 
	    ed.worksheet_desc_id, som_division_id,
	    cap_charges, under_charges, ffs_charges, total_charges,
	    cap_collections, under_collections, ffs_collections, total_collections,
	    cap_trvus, under_trvus, ffs_trvus, total_trvus,
	    cap_wrvus, under_wrvus, ffs_wrvus, total_wrvus,
	    copay,
	    'INP/OUTP' as sos,
	    cap_wrvu_split, under_wrvu_split, ffs_wrvu_split,
	    cap_collection_rate, under_collection_rate, ffs_collection_rate,
		stg.id AS external_src_ref
	from budget.stg_revenue_budget_exceptions_prov_load stg
	join som_portal.som_division sdiv on sdiv.division_id = stg.som_division_id
	join budget.tmp_exception_worksheet_desc ed ON ed.id = stg.id;
	
	/**
	 * Load provider level exceptions into medgrp_revenue_worksheet table.
	 */
	insert into budget.medgrp_revenue_worksheet (
	    py_actual_id, worksheet_desc_id, sos, division_id, proj_wrvus, exception_flag
	)
	select 
	    py.py_actual_id, ed.worksheet_desc_id, py.sos, py.division_id, stg.projected_wrvus, 'Y'
	from budget.stg_revenue_budget_exceptions_prov_load stg
	join budget.tmp_exception_worksheet_desc ed ON ed.id = stg.id
	join budget.revenue_prior_actuals_by_prov py on py.external_src_ref = stg.id and py.division_id = stg.som_division_id;
	
	ALTER TABLE stg_revenue_budget_exceptions_prov_load DROP COLUMN id;
	
	/**
	 * Load prior year actuals for the division level amounts.
	 * Do not load if the division does not exist in som_portal.som_division.
	 */
	insert into budget.revenue_prior_actuals_by_div (
	    division_id, 
	    cap_charges, under_charges, ffs_charges, total_charges,
	    cap_collections, under_collections, ffs_collections, total_collections,
	    cap_trvus, under_trvus, ffs_trvus, total_trvus,
	    cap_wrvus, under_wrvus, ffs_wrvus, total_wrvus,
	    copay,
	    sos,
	    cap_wrvu_split, under_wrvu_split, ffs_wrvu_split,
	    cap_collection_rate, under_collection_rate, ffs_collection_rate    
	)
	select 
	    stg.division_id, 
	    cap_charges, under_charges, ffs_charges, total_charges,
	    cap_collections, under_collections, ffs_collections, total_collections,
	    cap_trvus, under_trvus, ffs_trvus, total_trvus,
	    cap_wrvus, under_wrvus, ffs_wrvus, total_wrvus,
	    copay,
	    'INP/OUTP' as sos,
	    cap_wrvu_split, under_wrvu_split, ffs_wrvu_split,
	    cap_collection_rate, under_collection_rate, ffs_collection_rate    
	from budget.stg_revenue_budget_exceptions_div_load stg
	join som_portal.som_division sdiv on sdiv.division_id = stg.division_id;
	
	
	/**
	 * Insert division level projections into medgrp_revenue_payment_information
	 */
	insert into budget.medgrp_revenue_payment_information (
	    division_id, sos, 
	    proj_cap_collections_to_trvus_perc_chg, 
	    proj_under_collections_to_trvus_perc_chg,
	    proj_ffs_collections_to_trvus_perc_chg,
	    proj_copay,
	    py_actual_id
	)
	select 
	    stg.division_id, py.sos, 
	    stg.proj_cap_rate_percent_change,
	    stg.proj_under_rate_percent_change,
	    stg.proj_ffs_rate_percent_change,
	    stg.projected_copay,
	    py.py_actual_id
	from budget.stg_revenue_budget_exceptions_div_load stg
	join budget.revenue_prior_actuals_by_div py on py.division_id = stg.division_id;
	
	/**
	 * Insert division level projections into medgrp_revenue_inpatient_perc
	 */
	insert into budget.medgrp_revenue_inpatient_perc (
	    division_id, 
	    total_inpatient_trvus,
	    total_div_trvus,
	    prior_inpatient_percentage,
	    proj_inpatient_percentage
	)
	select 
	    division_id,
	    round((total_trvus * inpatient_percentage),4),
	    total_trvus,
	    inpatient_percentage,
	    projected_inpatient_percentage
	from budget.stg_revenue_budget_exceptions_div_load stg;
	
	/**
	 * Insert division level other income amounts
	 */
	insert into budget.medgrp_revenue_other_income (
	    division_id, co_payment, incentive, membership, stip, other
	)
	select 
	    division_id,
	    stg.other_income_co_payment,
	    stg.other_income_incentive,
	    stg.other_income_membership,
	    stg.other_income_stip,
	    stg.other_income_other
	from budget.stg_revenue_budget_exceptions_div_load stg;
	
	/**
	 * Audit tables for rejects
	 */
	truncate table budget.stg_revenue_budget_exceptions_prov_load_rejects;
	truncate table budget.stg_revenue_budget_exceptions_div_load_rejects;
	
	insert into budget.stg_revenue_budget_exceptions_prov_load_rejects (division_id) 
	select 
	    stg.som_division_id
	from budget.stg_revenue_budget_exceptions_prov_load stg
	left outer join som_portal.som_division sdiv on sdiv.division_id = stg.som_division_id
    where sdiv.division_id is null;
    
	insert into budget.stg_revenue_budget_exceptions_div_load_rejects (division_id)
	select 
	    stg.division_id
	from budget.stg_revenue_budget_exceptions_div_load stg
	left outer join som_portal.som_division sdiv on sdiv.division_id = stg.division_id
    where sdiv.division_id is null;

	DELETE FROM stg_revenue_budget_exceptions_prov_load;	
END
GO

call budget.revenueBudgetExceptionsLoad()
GO

drop procedure if exists budget.revenueBudgetExceptionsLoad
GO