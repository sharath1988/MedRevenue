use budget
GO

DROP PROCEDURE IF EXISTS budget.sp_REFRESH_PROVIDER_LOOKUP_INFO
GO

CREATE PROCEDURE budget.sp_REFRESH_PROVIDER_LOOKUP_INFO()
BEGIN
	/*load provider lookups*/
	
	/*Join on EIN*/
    drop table if exists budget.tmpProviderLookupInfoEIN
    ;
	create table budget.tmpProviderLookupInfoEIN AS
	select distinct mgbsPvdEmpId, ucsdEIN, ucsdName 
	from mgfs.providerLookupInfo
	where mgbsPvdEmpId is not null and mgbsPvdEmpId <> 999999999
	order by mgbsPvdEmpId
	;
	alter table budget.tmpProviderLookupInfoEIN add index (mgbsPvdEmpId)
	;
	
	/*Join on EIN and Name*/ 
	drop table if exists budget.tmpProviderLookupInfoEINName
    ;
    create table budget.tmpProviderLookupInfoEINName AS
	select distinct mgbsPvdEmpId, mgbsPvdEmpName, ucsdEIN, ucsdName 
	from mgfs.providerLookupInfo
	where mgbsPvdEmpId is not null and mgbsPvdEmpId = 999999999 and ucsdEIN is not null
	order by mgbsPvdEmpId
	;
	
	/*Join on Name and Dept*/
	drop table if exists budget.tmpProviderLookupInfoEmpNameDept
	;
	create table budget.tmpProviderLookupInfoEmpNameDept AS
	select distinct mgbsPvdEmpName, mgbsPvdEmpSomDept, ucsdEIN, ucsdName, SomDept
	from mgfs.providerLookupInfo
	where ucsdEIN is not null	
	;
	alter table budget.tmpProviderLookupInfoEmpNameDept add index (mgbsPvdEmpName, mgbsPvdEmpSomDept)
	;		
	
	/*Join on Name*/
	drop table if exists budget.tmpProviderLookupInfoEmpName
	;
	create table budget.tmpProviderLookupInfoEmpName AS
	select distinct mgbsPvdEmpName, ucsdEIN, ucsdName 
	from mgfs.providerLookupInfo
	where ucsdEIN is not null
	;
	alter table budget.tmpProviderLookupInfoEmpName add index (mgbsPvdEmpName)
	;   	
END
GO

DROP PROCEDURE IF EXISTS budget.sp_REFRESH_PROVIDER_LOOKUP_INFO_CLEANUP
GO

CREATE PROCEDURE budget.sp_REFRESH_PROVIDER_LOOKUP_INFO_CLEANUP()
BEGIN
	drop table if exists budget.tmpProviderLookupInfoEIN;
	
	drop table if exists budget.tmpProviderLookupInfoEINName;
	
	drop table if exists budget.tmpProviderLookupInfoEmpNameDept;
	
	drop table if exists budget.tmpProviderLookupInfoEmpName;
END
GO

drop procedure if exists budget.loadRevenuePriorYearActuals
GO

/*
 * Proc that creates the Tables holding revenue actuals by division.  
 * These are base tables behind a view that retrieves ext employee info.
 * 
 */
create procedure budget.loadRevenuePriorYearActuals()
BEGIN
    drop table if exists mgfs.mgbs_composite;
    
    /* Update ucsdNames to null where the name is empty or contains only whitespace........ */
    UPDATE mgfs.providerLookupInfo
    	SET ucsdName = null
    	WHERE trim(ucsdName) = '';
    	
	call budget.sp_REFRESH_PROVIDER_LOOKUP_INFO()
	;
    	
	CREATE TABLE mgfs.mgbs_composite (
	  rowId int(10) NOT NULL AUTO_INCREMENT,
	  postPd date DEFAULT NULL,
	  postDt datetime DEFAULT NULL,
	  division varchar(50) DEFAULT NULL,
	  divisionNumber int(10) DEFAULT NULL,
	  billingArea varchar(50) DEFAULT NULL,
	  billingAreaNumber int(10) DEFAULT NULL,
	  location varchar(55) DEFAULT NULL,
	  locationNumber int(10) DEFAULT NULL,
	  pos_type varchar(50) DEFAULT NULL,
	  sos varchar(40) DEFAULT NULL,
	  provider varchar(50) DEFAULT NULL,
	  employeeId varchar(10) DEFAULT NULL,
	  fund varchar(5) DEFAULT NULL,
	  originalFscType varchar(45) DEFAULT NULL,
	  originalFscCategory varchar(50) DEFAULT NULL,
	  capType varchar(45) DEFAULT NULL,
	  cptType varchar(15) DEFAULT NULL,
	  payClass varchar(45) DEFAULT NULL,
	  charges decimal(15,2) DEFAULT NULL,
	  payment decimal(15,2) DEFAULT NULL,
	  paymentAdj decimal(15,2) DEFAULT NULL,
	  trvu decimal(15,3) DEFAULT NULL,
	  wrvu decimal(15,3) DEFAULT NULL,
	  volume int(11) DEFAULT NULL,
	  anesUnits decimal(15,2) DEFAULT NULL,
	  update_ts datetime DEFAULT NULL,
	  create_ts datetime DEFAULT NULL,
	  mrvu decimal(15,3) DEFAULT NULL,
	  PROVIDER_prov2 varchar(50) DEFAULT NULL,
	  EID_prov2 int(10) DEFAULT NULL,
	  tx_id int(11) DEFAULT NULL,
	  period int(11) DEFAULT NULL,
	  service_prov_name varchar(50) DEFAULT NULL,
	  service_prov_eid int(10) DEFAULT NULL,
	  fundz varchar(5) DEFAULT NULL,
	  pe_rvu decimal(15,3) DEFAULT NULL,
	  dSource varchar(5) DEFAULT NULL,
	  billing_type VARCHAR(32) DEFAULT NULL,
	  PRIMARY KEY (rowId),
	  KEY ind_pd_divisionNumber (postPd,divisionNumber),
	  KEY ind_pd_bAN (postPd,billingAreaNumber),
	  KEY ind_pd_empIdd (postPd,employeeId),
	  KEY ind_pd_fscCat (originalFscCategory),
	  KEY idx_prov_name (provider,employeeId)
	) AUTO_INCREMENT=6096332;

    INSERT INTO mgfs.mgbs_composite
	  (postPd,
	  postDt,
	  division,
	  divisionNumber,
	  billingArea,
	  billingAreaNumber,
	  location,
	  locationNumber,
	  pos_type,
	  sos,
	  provider,
	  employeeId,
	  fund,
	  originalFscType,
	  originalFscCategory,
	  capType,
	  cptType,
	  payClass,
	  charges,
	  payment,
	  paymentAdj,
	  trvu,
	  wrvu,
	  volume,
	  anesUnits,
	  update_ts,
	  create_ts,
	  mrvu,
	  PROVIDER_prov2,
	  EID_prov2,
	  period,
	  service_prov_name,
	  service_prov_eid,
	  fundz,
	  pe_rvu,
	  dSource,
	  billing_type)
    SELECT 
            postPd,
            postDt,
            division,
            divisionNumber,
            billingArea,
            billingAreaNumber,

            location,
            locationNumber,
            pos_type,
            sos,
            COALESCE(plini.ucsdName,pli.ucsdName,plin.ucsdName,provider) AS provider,
            COALESCE(plini.ucsdEIN,pli.ucsdEIN,plin.ucsdEIN,employeeId) AS employeeId,
            fund,
            originalFscType,
            originalFscCategory,

            capType,
            cptType,
            payClass,
            charges,
            payment,
            paymentAdj,

            trvu,
            wrvu,
            volume,
            anesUnits,
            update_ts,
            create_ts,

            mrvu,
            PROVIDER_prov2,
            EID_prov2,

            period,
            service_prov_name,
            service_prov_eid,
            fundz,
            pe_rvu,
            dSource,
            'INSIDE' as billing_type
        FROM mgfs.mgbs mb 
        join budget.budgetMetadata meta on mb.postPd >= meta.budget_start_date and mb.postPd <= meta.budget_end_date 
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEIN pli ON pli.mgbsPvdEmpId = mb.employeeId
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEINName plini ON plini.mgbsPvdEmpId = mb.employeeId AND plini.mgbsPvdEmpName = mb.provider
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEmpName plin ON plin.mgbsPvdEmpName = mb.provider
        where mb.billingAreaNumber is not null;
    
	INSERT INTO mgfs.mgbs_composite
	  (postPd,
	  postDt,
	  division,
	  divisionNumber,
	  billingArea,
	  billingAreaNumber,
	  location,
	  locationNumber,
	  pos_type,
	  sos,
	  provider,
	  employeeId,
	  fund,
	  originalFscType,
	  originalFscCategory,
	  capType,
	  cptType,
	  payClass,
	  charges,
	  payment,
	  paymentAdj,
	  trvu,
	  wrvu,
	  volume,
	  anesUnits,
	  update_ts,
	  create_ts,
	  mrvu,
	  PROVIDER_prov2,
	  EID_prov2,
	  period,
	  service_prov_name,
	  service_prov_eid,
	  fundz,
	  pe_rvu,
	  dSource,
	  billing_type)
    SELECT 
            postPd,
            postDt,
            division,
            divisionNumber,
            billingArea,
            billingAreaNumber,

            location,
            locationNumber,
            null as pos_type,
            sos,
            COALESCE(plini.ucsdName,pli.ucsdName,plin.ucsdName,provider) AS provider,
            COALESCE(plini.ucsdEIN,pli.ucsdEIN,plin.ucsdEIN,employeeId) AS employeeId,
            fund,
            originalFscType,
            originalFscCategory,

            capType,
            cptType,
            payClass,
            charges,
            payment,
            paymentAdj,

            trvu,
            wrvu,
            volume,
            anesUnits,
            update_ts,
            create_ts,

            mrvu,
            PROVIDER_prov2,
            EID_prov2,

            null as period,
            null as service_prov_name,
            null as service_prov_eid,
            null as fundz,
            null as pe_rvu,
            null as dSource,
            'OUTSIDE' as billing_type
        FROM mgfs.mgbs_outside_billers mb
        join budget.budgetMetadata meta on mb.postPd >= meta.budget_start_date and mb.postPd <= meta.budget_end_date
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEIN pli ON pli.mgbsPvdEmpId = mb.employeeId
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEINName plini ON plini.mgbsPvdEmpId = mb.employeeId AND plini.mgbsPvdEmpName = mb.provider
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEmpName plin ON plin.mgbsPvdEmpName = mb.provider
        where mb.billingAreaNumber is not null;
        
	call sp_REFRESH_PROVIDER_LOOKUP_INFO_CLEANUP()
	;  
	
	
		
	-- Create revenue provider description table with normalized data from mgbs_composite
	-- This will give us a key to join back to during rolling 12 month runs
	DROP TABLE IF EXISTS mgfs.mgbs_medgrp_rev_worksheet_desc;

	CREATE TABLE mgfs.mgbs_medgrp_rev_worksheet_desc AS
		SELECT
			m.rowId,
			m.provider,
			COALESCE(aev.employee_fullname,UPPER(REPLACE(provider,'"','')),'*UNSPECIFIED PROVIDER') AS description,
			m.employeeId,
			MIN(aev.all_employee_id) AS all_employee_id
		FROM
			mgfs.mgbs_composite m
		LEFT OUTER JOIN
			dw.all_employee_view aev ON
				aev.employee_ucsd_id = m.employeeId
		GROUP BY
			m.rowId,
			m.provider,
			description,
			m.employeeId;

	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc ADD PRIMARY KEY (rowId);
	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc ADD INDEX employeeId (employeeId);
	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc ADD INDEX provider (provider);
	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc ADD INDEX description (description);

	DROP TABLE IF EXISTS budget.medgrp_revenue_worksheet;
	DROP TABLE IF EXISTS budget.medgrp_revenue_worksheet_desc;

	CREATE TABLE budget.medgrp_revenue_worksheet_desc (
		worksheet_desc_id   INT PRIMARY KEY AUTO_INCREMENT,
		description         VARCHAR(512) NOT NULL,
		all_employee_id     INT,
		reference_number    INT,
		FOREIGN KEY (all_employee_id) REFERENCES dw.all_employee(all_employee_id) ON DELETE SET NULL,
		UNIQUE (all_employee_id),
		UNIQUE (description, reference_number, all_employee_id)
	);

	INSERT INTO budget.medgrp_revenue_worksheet_desc (description, all_employee_id, reference_number)
		SELECT
			description,
			all_employee_id,
			employeeId AS reference_number
		FROM
			mgfs.mgbs_medgrp_rev_worksheet_desc
		GROUP BY
			description,
			all_employee_id,
			employeeId;

	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc ADD COLUMN worksheet_desc_id INT;
	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc ADD 
		FOREIGN KEY (worksheet_desc_id) REFERENCES budget.medgrp_revenue_worksheet_desc(worksheet_desc_id);

	UPDATE
		mgfs.mgbs_medgrp_rev_worksheet_desc m
	JOIN
		budget.medgrp_revenue_worksheet_desc l ON
			l.description = m.description AND
			(l.reference_number = m.employeeId OR
			(l.reference_number IS NULL AND m.employeeId IS NULL)) AND
			(l.all_employee_id = m.all_employee_id OR
			(l.all_employee_id IS NULL AND m.all_employee_id IS NULL))
	SET
		m.worksheet_desc_id = l.worksheet_desc_id;

	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc MODIFY worksheet_desc_id INT NOT NULL;

	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc DROP COLUMN provider;
	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc DROP COLUMN description;
	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc DROP COLUMN employeeId;
	ALTER TABLE mgfs.mgbs_medgrp_rev_worksheet_desc DROP COLUMN all_employee_id;

    -- CREATE temp table to hold Charges, collections, trvus, and wrvus
    drop table if exists tmp_CCTW;
    
    create table tmp_CCTW as
    -- CAP
    SELECT
		wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type,
        replace(mg.sos, ' ', '') as sos,
        ROUND(SUM(mg.charges),4) AS cap_charges,
        ROUND(SUM(mg.trvu)*meta.cap_rate,4) AS cap_collections,
        ROUND(SUM(mg.trvu),4) AS cap_trvus,
        ROUND(SUM(mg.wrvu),4) AS cap_wrvus,
        null as under_charges, null as under_collections, null as under_trvus, null as under_wrvus,
        null as ffs_charges, null as ffs_collections, null as ffs_trvus, null as ffs_wrvus,
        null as copay
    FROM
            mgfs.mgBilling mb 
            JOIN mgfs.mgbs_composite mg                 	ON mg.billingAreaNumber = mb.NUMBER 
			JOIN mgfs.mgbs_medgrp_rev_worksheet_desc wd		ON wd.rowId = mg.rowId
            JOIN budget.budgetMetadata meta
    WHERE
        mg.originalFscType = 'CAP'
        AND payClass LIKE 'OTHER%'
        AND mb.somDiv <> 1
    GROUP BY
		wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type, 
        mg.sos

    union all

    -- UNDER
    SELECT
        wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type,
        replace(mg.sos, ' ', '') as sos,
        null, null, null, null,
        ROUND(SUM(mg.charges),4) AS under_charges,
        ROUND(SUM(mg.payment - mg.paymentAdj),4) AS under_collections,
        ROUND(SUM(mg.trvu),4) AS under_trvus,
        ROUND(SUM(mg.wrvu),4) AS under_wrvus,
        null, null, null, null,
        null
    FROM
		mgfs.mgBilling mb 
		JOIN mgfs.mgbs_composite mg                 	ON mg.billingAreaNumber = mb.NUMBER 
		JOIN mgfs.mgbs_medgrp_rev_worksheet_desc wd		ON wd.rowId = mg.rowId
		JOIN mgfs.tblMgbsPayorMixCategories mix     	ON mg.originalFscCategory=mix.orginalFscCategory 
    WHERE
        (mix.fscCategoryGroupId = 2 and mg.originalFscType <> 'CAP')
        AND payClass LIKE 'OTHER%'
        AND mb.somDiv <> 1
    GROUP BY 
		wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type, 
        mg.sos

    union all

    -- FFS
    SELECT
        wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type,
        replace(mg.sos, ' ', '') as sos,
        null, null, null, null,
        null, null, null, null,
        ROUND(SUM(mg.charges),4) AS ffs_charges,
        ROUND(SUM(mg.payment - mg.paymentAdj),4) AS ffs_collections,
        ROUND(SUM(mg.trvu),4) AS ffs_trvus,
        ROUND(SUM(mg.wrvu),4) AS ffs_wrvus,
        null
    FROM
            mgfs.mgBilling mb 
            JOIN mgfs.mgbs_composite mg                 	ON mg.billingAreaNumber = mb.NUMBER 
			JOIN mgfs.mgbs_medgrp_rev_worksheet_desc wd		ON wd.rowId = mg.rowId
            LEFT OUTER JOIN mgfs.tblMgbsPayorMixCategories mix     	ON mg.originalFscCategory=mix.orginalFscCategory  
    WHERE
        ((mix.fscCategoryGroupId is null or mix.fscCategoryGroupId >=3) and (mg.originalFscType <> 'CAP' or mg.originalFscType is null))
        AND payClass LIKE 'OTHER%'
        AND mb.somDiv <> 1
    GROUP BY 
		wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type, 
        mg.sos
        
    union all
    
    -- COPAY
    SELECT
        wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type,
        replace(mg.sos, ' ', '') as sos,
        null, null, null, null,
        null, null, null, null,
        null, null, null, null,
        sum(mg.payment) - sum(mg.paymentAdj) as copay
    FROM
            mgfs.mgBilling mb
            JOIN mgfs.mgbs_composite mg                 	ON mg.billingAreaNumber = mb.NUMBER 
			JOIN mgfs.mgbs_medgrp_rev_worksheet_desc wd		ON wd.rowId = mg.rowId
            JOIN mgfs.tblMgbsPayorMixCategories mix     	ON mg.originalFscCategory=mix.orginalFscCategory  
    WHERE
        payClass LIKE 'COPAY%' 
    GROUP BY 
		wd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type, 
        mg.sos;
    
	-- REMOVE Anes
	delete from budget.tmp_CCTW where somDiv = 1;
	
    drop table if exists budget.mv_revenue_prior_actuals_cctw_by_category_and_sos;
    
    CREATE TABLE budget.mv_revenue_prior_actuals_cctw_by_category_and_sos as
    select 
    	cctw.worksheet_desc_id, cctw.somDiv, cctw.sos,

        -- CAP
        ifnull(sum(cap_charges), 0) as cap_charges, 
        ifnull(sum(cap_collections), 0) as cap_collections, 
        ifnull(sum(cap_trvus), 0) as cap_trvus, 
        ifnull(sum(cap_wrvus), 0) as cap_wrvus,

        -- UNDER
        ifnull(sum(under_charges), 0) as under_charges, 
        ifnull(sum(under_collections), 0) as under_collections, 
        ifnull(sum(under_trvus), 0) as under_trvus, 
        ifnull(sum(under_wrvus), 0) as under_wrvus,

        -- FFS
        ifnull(sum(ffs_charges), 0) as ffs_charges, 
        ifnull(sum(ffs_collections), 0) as ffs_collections, 
        ifnull(sum(ffs_trvus), 0) as ffs_trvus, 
        ifnull(sum(ffs_wrvus), 0) as ffs_wrvus,
        
        -- COPAY
        ifnull(sum(copay), 0) as copay
    from tmp_CCTW cctw
    group by cctw.somDiv, cctw.worksheet_desc_id, cctw.sos
    order by somDiv, cctw.worksheet_desc_id;
    
 	-- Delete providers that are separated and have amounts equal to zero
	DELETE
		rev.*
	FROM
		budget.mv_revenue_prior_actuals_cctw_by_category_and_sos rev
		JOIN budget.medgrp_revenue_worksheet_desc wd ON
			wd.worksheet_desc_id = rev.worksheet_desc_id
		LEFT OUTER JOIN dw.all_employee_view aev ON
			aev.all_employee_id = wd.all_employee_id
		LEFT OUTER JOIN dw.employee_latest_refresh_view db2e ON
			db2e.emb_employee_number = aev.employee_ucsd_id
	WHERE
		(db2e.emp_employment_status_code = 'S' OR
		db2e.emb_employee_number IS NULL) AND
		(round(cap_collections + under_collections +  ffs_collections,2) = 0.00 AND round(cap_wrvus + under_wrvus + ffs_wrvus,2) = 0.00);
		/*(cap_charges + cap_collections + cap_trvus + cap_wrvus + under_charges + under_collections + under_trvus + under_wrvus + ffs_charges + ffs_collections + ffs_trvus + ffs_wrvus) = 0;*/
	
	/*Dont include Opthamology since they are not in EPIC*/
	/*DELETE FROM budget.mv_revenue_prior_actuals_cctw_by_category_and_sos WHERE somDiv = 18
	;*/
    
	alter table budget.mv_revenue_prior_actuals_cctw_by_category_and_sos add index worksheet_desc_id (worksheet_desc_id);
    alter table budget.mv_revenue_prior_actuals_cctw_by_category_and_sos add index somDiv (somDiv);

    drop table if exists budget.revenue_prior_actual_cctw_totals_by_sos;

    create table budget.revenue_prior_actual_cctw_totals_by_sos as
    select worksheet_desc_id, somDiv, sos,
        sum(cap_charges + under_charges + ffs_charges) as charges,
        sum(cap_collections + under_collections + ffs_collections) as collections,
        sum(cap_trvus + under_trvus + ffs_trvus) as trvus,
        sum(cap_wrvus + under_wrvus + ffs_wrvus) as wrvus,
        sum(copay) as copay
        from budget.mv_revenue_prior_actuals_cctw_by_category_and_sos cat
        group by worksheet_desc_id, sos, somDiv;
                
    alter table budget.revenue_prior_actual_cctw_totals_by_sos add index worksheet_desc_id (worksheet_desc_id);
    alter table budget.revenue_prior_actual_cctw_totals_by_sos add index sos (sos);
        
   	drop table if exists budget.revenue_prior_actual_cctw_totals_by_div;
        
   	create table budget.revenue_prior_actual_cctw_totals_by_div as
	select somDiv, 
	sum(charges) as total_charges,
	sum(collections) as total_collections,
	sum(trvus) as total_trvus,
	sum(wrvus) as total_wrvus,
	sum(copay) as total_copay
	from budget.revenue_prior_actual_cctw_totals_by_sos
	group by somDiv;
	
	alter table revenue_prior_actual_cctw_totals_by_div add index somDiv (somDiv);
	
   	drop table if exists budget.revenue_prior_actual_cctw_totals_by_div_and_sos;
        
   	create table budget.revenue_prior_actual_cctw_totals_by_div_and_sos as
	select somDiv, sos,
	sum(charges) as total_charges,
	sum(collections) as total_collections,
	sum(trvus) as total_trvus,
	sum(wrvus) as total_wrvus,
	sum(copay) as total_copay
	from budget.revenue_prior_actual_cctw_totals_by_sos
	group by somDiv, sos;
	
	alter table budget.revenue_prior_actual_cctw_totals_by_div_and_sos add index somDiv (somDiv);	
	alter table budget.revenue_prior_actual_cctw_totals_by_div_and_sos add index sos (sos);
     
    drop table if exists budget.revenue_prior_actual_cctw_by_category_and_div;
        
    create table budget.revenue_prior_actual_cctw_by_category_and_div as
    select somDiv, 'INP/OUTP' as sos,
    sum(cap_charges) as total_cap_charges, sum(cap_collections) as total_cap_collections, sum(cap_trvus) as total_cap_trvus, sum(cap_wrvus) as total_cap_wrvus,
    sum(under_charges) as total_under_charges, sum(under_collections) as total_under_collections, sum(under_trvus) as total_under_trvus, sum(under_wrvus) as total_under_wrvus,
    sum(ffs_charges) as total_ffs_charges, sum(ffs_collections) as total_ffs_collections, sum(ffs_trvus) as total_ffs_trvus, sum(ffs_wrvus) as total_ffs_wrvus,
    sum(copay) as total_copay
	from budget.mv_revenue_prior_actuals_cctw_by_category_and_sos
	group by somDiv
	
	union all
	
    select somDiv, sos,
    sum(cap_charges) as total_cap_charges, sum(cap_collections) as total_cap_collections, sum(cap_trvus) as total_cap_trvus, sum(cap_wrvus) as total_cap_wrvus,
    sum(under_charges) as total_under_charges, sum(under_collections) as total_under_collections, sum(under_trvus) as total_under_trvus, sum(under_wrvus) as total_under_wrvus,
    sum(ffs_charges) as total_ffs_charges, sum(ffs_collections) as total_ffs_collections, sum(ffs_trvus) as total_ffs_trvus, sum(ffs_wrvus) as total_ffs_wrvus,
    sum(copay) as total_copay
	from budget.mv_revenue_prior_actuals_cctw_by_category_and_sos
	where somDiv = 22
	group by somDiv, sos;
	
	alter table budget.revenue_prior_actual_cctw_by_category_and_div add column py_actual_cctw_id int(11) auto_increment primary key;
	alter table budget.revenue_prior_actual_cctw_by_category_and_div add index somDiv (somDiv);
	alter table budget.revenue_prior_actual_cctw_by_category_and_div add index sos (sos);
	
	drop table if exists budget.revenue_prior_actual_cctw_by_category;
	
	create table budget.revenue_prior_actual_cctw_by_category as
	select worksheet_desc_id, somDiv,
    sum(cap_charges) as cap_charges, sum(cap_collections) as cap_collections, sum(cap_trvus) as cap_trvus, sum(cap_wrvus) as cap_wrvus, 
    sum(under_charges) as under_charges, sum(under_collections) as under_collections, sum(under_trvus) as under_trvus, sum(under_wrvus) as under_wrvus, 
    sum(ffs_charges) as ffs_charges, sum(ffs_collections) as ffs_collections, sum(ffs_trvus) as ffs_trvus, sum(ffs_wrvus) as ffs_wrvus, 
    sum(copay) as copay
	from budget.mv_revenue_prior_actuals_cctw_by_category_and_sos py 
	group by worksheet_desc_id, somDiv;
	
	alter table budget.revenue_prior_actual_cctw_by_category add index somDiv (somDiv);
	alter table budget.revenue_prior_actual_cctw_by_category add index worksheet_desc_id (worksheet_desc_id);
	
	drop table if exists budget.revenue_prior_actual_cctw_totals;
	
	create table budget.revenue_prior_actual_cctw_totals as
	select worksheet_desc_id, somDiv,
        sum(charges) as charges,
        sum(collections) as collections,
        sum(trvus) as trvus,
        sum(wrvus) as wrvus,
        sum(copay) as copay
    from budget.revenue_prior_actual_cctw_totals_by_sos totals
    group by worksheet_desc_id, somDiv;
    
    alter table budget.revenue_prior_actual_cctw_totals add index worksheet_desc_id (worksheet_desc_id);
    alter table budget.revenue_prior_actual_cctw_totals add index somDiv (somDiv);
    
    drop table if exists tmp_CCTW;
    
    drop table if exists budget.mv_revenue_prior_actual_cctw_totals;
    
	create table budget.mv_revenue_prior_actual_cctw_totals as
	select worksheet_desc_id, somDiv, 'INP/OUTP' as sos, charges, collections, trvus, wrvus, copay 
	from budget.revenue_prior_actual_cctw_totals totals
	where totals.somDiv <> 22
	union all
	select * from budget.revenue_prior_actual_cctw_totals_by_sos where somDiv=22;
	
	alter table budget.mv_revenue_prior_actual_cctw_totals add column (py_actual_id int(11) primary key auto_increment);
	alter table budget.mv_revenue_prior_actual_cctw_totals add index worksheet_desc_id (worksheet_desc_id);
	alter table budget.mv_revenue_prior_actual_cctw_totals add index somDiv (somDiv);
	alter table budget.mv_revenue_prior_actual_cctw_totals add index sos (sos);
END
GO

call budget.loadRevenuePriorYearActuals()
GO

drop procedure if exists budget.loadRevenuePriorYearActuals
GO