use budget
GO

drop procedure if exists budget.loadRevenueRolling12MonthActuals
GO

/*
 * Proc that creates the Tables holding revenue actuals by division.  
 * These are base tables behind a view that retrieves ext employee info.
 * 
 */
create procedure budget.loadRevenueRolling12MonthActuals()
BEGIN

	drop table if exists budget.rolling_12_month_range;
	
	create table budget.rolling_12_month_range as
	select 
		1 as id,
	    STR_TO_DATE(DATE_FORMAT((curdate() - INTERVAL 12 MONTH), '%m/01/%Y'), '%m/%d/%Y') as budget_start_date,
	    LAST_DAY((curdate() - INTERVAL 1 MONTH)) as budget_end_date 
	from dual;
	
    UPDATE mgfs.providerLookupInfo
    	SET ucsdName = null
    	WHERE trim(ucsdName) = '';	
    	
	call budget.sp_REFRESH_PROVIDER_LOOKUP_INFO()
	; 	    	
    
    drop table if exists budget.tmp_12mo_actual_revenue;

    create table budget.tmp_12mo_actual_revenue as
    SELECT 
            rowId,
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
        join budget.rolling_12_month_range meta on mb.postPd >= meta.budget_start_date and mb.postPd <= meta.budget_end_date
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEIN pli ON pli.mgbsPvdEmpId = mb.employeeId
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEINName plini ON plini.mgbsPvdEmpId = mb.employeeId AND plini.mgbsPvdEmpName = mb.provider
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEmpName plin ON plin.mgbsPvdEmpName = mb.provider       
        where mb.billingAreaNumber is not null
        
        union all
        
    SELECT 
            rowId,
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
        join budget.rolling_12_month_range meta on mb.postPd >= meta.budget_start_date and mb.postPd <= meta.budget_end_date        
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEIN pli ON pli.mgbsPvdEmpId = mb.employeeId
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEINName plini ON plini.mgbsPvdEmpId = mb.employeeId AND plini.mgbsPvdEmpName = mb.provider
        LEFT OUTER JOIN budget.tmpProviderLookupInfoEmpName plin ON plin.mgbsPvdEmpName = mb.provider
        where mb.billingAreaNumber is not null;
        
	call sp_REFRESH_PROVIDER_LOOKUP_INFO_CLEANUP()
	;  
		
	ALTER TABLE budget.tmp_12mo_actual_revenue ADD COLUMN id INT PRIMARY KEY AUTO_INCREMENT;
	
	DROP TABLE IF EXISTS budget.tmp_12mo_rev_worksheet_desc;

	CREATE TABLE budget.tmp_12mo_rev_worksheet_desc AS
		SELECT
			id,
			a.provider,
			COALESCE(aev.employee_fullname,UPPER(REPLACE(a.provider,'"','')),'*UNSPECIFIED PROVIDER') AS description,
			a.employeeId,
			MIN(aev.all_employee_id) AS all_employee_id
		FROM
			budget.tmp_12mo_actual_revenue a
		LEFT OUTER JOIN
			dw.all_employee_view aev ON
				aev.employee_ucsd_id = a.employeeId
		GROUP BY
			id,
			provider,
			description,
			employeeId;
	
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc ADD PRIMARY KEY (id);
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc ADD INDEX all_employee_id (all_employee_id);
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc ADD INDEX employeeId (employeeId);
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc ADD INDEX description (description);
	
	DROP TABLE IF EXISTS budget.tmp_12mo_medgrp_revenue_worksheet_desc;
	
	CREATE TABLE budget.tmp_12mo_medgrp_revenue_worksheet_desc AS
		SELECT
			description,
			all_employee_id,
			employeeId AS reference_number
		FROM
			budget.tmp_12mo_rev_worksheet_desc
		GROUP BY
			description,
			all_employee_id,
			reference_number;
			
	ALTER TABLE budget.tmp_12mo_medgrp_revenue_worksheet_desc ADD COLUMN worksheet_desc_id INT;
	
	UPDATE
		budget.tmp_12mo_medgrp_revenue_worksheet_desc m
	JOIN
		budget.medgrp_revenue_worksheet_desc l ON
			l.all_employee_id = m.all_employee_id OR
			((l.all_employee_id IS NULL AND m.all_employee_id IS NULL)
			AND
			(l.reference_number = m.reference_number OR
				(l.reference_number IS NULL AND m.reference_number IS NULL))
			AND
			l.description = m.description)
	SET
		m.worksheet_desc_id = l.worksheet_desc_id;
		
	INSERT INTO budget.medgrp_revenue_worksheet_desc (description, all_employee_id, reference_number)
		SELECT
			description, all_employee_id, reference_number
		FROM
			budget.tmp_12mo_medgrp_revenue_worksheet_desc
		WHERE
			worksheet_desc_id IS NULL;
			
	UPDATE
		budget.tmp_12mo_medgrp_revenue_worksheet_desc m
	JOIN
		budget.medgrp_revenue_worksheet_desc l ON
			l.all_employee_id = m.all_employee_id OR
			((l.all_employee_id IS NULL AND m.all_employee_id IS NULL)
			AND
			(l.reference_number = m.reference_number OR
				(l.reference_number IS NULL AND m.reference_number IS NULL))
			AND
			l.description = m.description)
	SET
		m.worksheet_desc_id = l.worksheet_desc_id
	WHERE
		m.worksheet_desc_id IS NULL;
		
	ALTER TABLE budget.tmp_12mo_medgrp_revenue_worksheet_desc MODIFY worksheet_desc_id INT NOT NULL;
	
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc ADD COLUMN worksheet_desc_id INT;
	
	UPDATE
		budget.tmp_12mo_rev_worksheet_desc m
	JOIN
		budget.tmp_12mo_medgrp_revenue_worksheet_desc l ON
			l.description = m.description AND
			(l.reference_number = m.employeeId OR
			(l.reference_number IS NULL AND m.employeeId IS NULL)) AND
			(l.all_employee_id = m.all_employee_id OR
			(l.all_employee_id IS NULL AND m.all_employee_id IS NULL))
	SET
		m.worksheet_desc_id = l.worksheet_desc_id;
		
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc MODIFY worksheet_desc_id INT NOT NULL;
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc DROP COLUMN provider;
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc DROP COLUMN description;
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc DROP COLUMN employeeId;
	ALTER TABLE budget.tmp_12mo_rev_worksheet_desc DROP COLUMN all_employee_id;
	
	DROP TABLE tmp_12mo_medgrp_revenue_worksheet_desc;

    -- CREATE temp table to hold Charges, collections, trvus, and wrvus
    drop table if exists tmp_12mo_CCTW;

    create table budget.tmp_12mo_CCTW as
    -- CAP
    SELECT
        rwd.worksheet_desc_id,
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
            JOIN budget.tmp_12mo_actual_revenue mg                 ON mg.billingAreaNumber = mb.NUMBER 
			JOIN budget.tmp_12mo_rev_worksheet_desc rwd            ON rwd.id = mg.id 
            JOIN budget.budgetMetadata meta
    WHERE
        mg.originalFscType = 'CAP'
        AND payClass LIKE 'OTHER%'
        AND mb.somDiv <> 1
    GROUP BY 
        mb.somDiv,
        rwd.worksheet_desc_id,
        mg.billing_type, 
        mg.sos

    union all

    -- UNDER
    SELECT
        rwd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type,
        replace(mg.sos, ' ', '') as sos,
        null, null, null, null,
        ROUND(SUM(mg.charges),4) AS under_charges,
        ROUND(SUM(mg.payment - mg.paymentAdj),4) AS collections,
        ROUND(SUM(mg.trvu),4) AS under_trvus,
        ROUND(SUM(mg.wrvu),4) AS under_wrvus,
        null, null, null, null,
        null
    FROM
            mgfs.mgBilling mb 
            JOIN budget.tmp_12mo_actual_revenue mg                 ON mg.billingAreaNumber = mb.NUMBER 
			JOIN budget.tmp_12mo_rev_worksheet_desc rwd            ON rwd.id = mg.id 
            JOIN mgfs.tblMgbsPayorMixCategories mix     ON mg.originalFscCategory=mix.orginalFscCategory 
    WHERE
        (mix.fscCategoryGroupId = 2 and mg.originalFscType <> 'CAP') 
        AND payClass LIKE 'OTHER%'
        AND mb.somDiv <> 1
    GROUP BY 
        mb.somDiv,
        rwd.worksheet_desc_id,
        mg.billing_type, 
        mg.sos

    union all

    -- FFS
    SELECT
        rwd.worksheet_desc_id,
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
            JOIN budget.tmp_12mo_actual_revenue mg                 ON mg.billingAreaNumber = mb.NUMBER 
			JOIN budget.tmp_12mo_rev_worksheet_desc rwd            ON rwd.id = mg.id 
            LEFT OUTER JOIN mgfs.tblMgbsPayorMixCategories mix     ON mg.originalFscCategory=mix.orginalFscCategory  
    WHERE
        ((mix.fscCategoryGroupId is null or mix.fscCategoryGroupId >=3) and mg.originalFscType <> 'CAP')
        AND payClass LIKE 'OTHER%'
        AND mb.somDiv <> 1
    GROUP BY 
        mb.somDiv,
        rwd.worksheet_desc_id,
        mg.billing_type, 
        mg.sos
        
    union all
    
    -- COPAY
    SELECT
        rwd.worksheet_desc_id,
        mb.somDiv,
        mg.billing_type,
        replace(mg.sos, ' ', '') as sos,
        null, null, null, null,
        null, null, null, null,
        null, null, null, null,
        sum(mg.payment) - sum(mg.paymentAdj) as copay
    FROM
            mgfs.mgBilling mb 
            JOIN budget.tmp_12mo_actual_revenue mg                 ON mg.billingAreaNumber = mb.NUMBER 
			JOIN budget.tmp_12mo_rev_worksheet_desc rwd            ON rwd.id = mg.id 
            JOIN mgfs.tblMgbsPayorMixCategories mix     ON mg.originalFscCategory=mix.orginalFscCategory  
    WHERE
        payClass LIKE 'COPAY%' 
    GROUP BY 
        mb.somDiv,
        rwd.worksheet_desc_id,
        mg.billing_type, 
        mg.sos;
    

    -- REMOVE Anes
    delete from budget.tmp_12mo_CCTW where somDiv = 1;
        
    drop table if exists budget.revenue_12mo_actual_cctw_by_category_and_sos;
    
    CREATE TABLE budget.revenue_12mo_actual_cctw_by_category_and_sos as
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
    from tmp_12mo_CCTW cctw
    group by cctw.worksheet_desc_id, cctw.somDiv, cctw.sos
    order by somDiv;
    
    drop table if exists budget.revenue_12mo_actual_cctw_totals_by_sos;

    create table budget.revenue_12mo_actual_cctw_totals_by_sos as
    select 
        worksheet_desc_id, somDiv, sos,
        cap_charges + under_charges + ffs_charges as charges,
        cap_collections + under_collections + ffs_collections as collections,
        cap_trvus + under_trvus + ffs_trvus as trvus,
        cap_wrvus + under_wrvus + ffs_wrvus as wrvus,
        copay
        from budget.revenue_12mo_actual_cctw_by_category_and_sos cat;
        
    alter table budget.revenue_12mo_actual_cctw_totals_by_sos add index worksheet_desc_id (worksheet_desc_id);
    alter table budget.revenue_12mo_actual_cctw_totals_by_sos add index sos (sos);
        
   	drop table if exists budget.mv_revenue_12mo_actual_cctw_totals_by_div;
        
   	create table budget.mv_revenue_12mo_actual_cctw_totals_by_div as
	select somDiv, 
	sum(charges) as total_charges,
	sum(collections) as total_collections,
	sum(trvus) as total_trvus,
	sum(wrvus) as total_wrvus,
	sum(copay) as total_copay
	from budget.revenue_12mo_actual_cctw_totals_by_sos
	group by somDiv;
	
	alter table budget.mv_revenue_12mo_actual_cctw_totals_by_div add index somDiv (somDiv);
	
   	drop table if exists budget.mv_revenue_12mo_actual_cctw_totals_by_div_and_sos;
        
   	create table budget.mv_revenue_12mo_actual_cctw_totals_by_div_and_sos as
	select somDiv, sos,
	sum(charges) as total_charges,
	sum(collections) as total_collections,
	sum(trvus) as total_trvus,
	sum(wrvus) as total_wrvus,
	sum(copay) as total_copay
	from budget.revenue_12mo_actual_cctw_totals_by_sos
	group by somDiv, sos;
	
	alter table budget.mv_revenue_12mo_actual_cctw_totals_by_div_and_sos add index somDiv (somDiv);	
	alter table budget.mv_revenue_12mo_actual_cctw_totals_by_div_and_sos add index sos (sos);	
     
    drop table if exists budget.revenue_12mo_actual_cctw_by_category_and_div;
        
    create table budget.revenue_12mo_actual_cctw_by_category_and_div as
    select somDiv, 
    sum(cap_charges) as total_cap_charges, sum(cap_collections) as total_cap_collections, sum(cap_trvus) as total_cap_trvus, sum(cap_wrvus) as total_cap_wrvus,
    sum(under_charges) as total_under_charges, sum(under_collections) as total_under_collections, sum(under_trvus) as total_under_trvus, sum(under_wrvus) as total_under_wrvus,
    sum(ffs_charges) as total_ffs_charges, sum(ffs_collections) as total_ffs_collections, sum(ffs_trvus) as total_ffs_trvus, sum(ffs_wrvus) as total_ffs_wrvus,
    sum(copay) as total_copay
	from budget.revenue_12mo_actual_cctw_by_category_and_sos
	group by somDiv;
	
	alter table revenue_12mo_actual_cctw_by_category_and_div add index somDiv (somDiv);
	
	drop table if exists budget.revenue_12mo_actual_cctw_by_category;
	
	create table budget.revenue_12mo_actual_cctw_by_category as
	select worksheet_desc_id, somDiv, 
    sum(cap_charges) as cap_charges, sum(cap_collections) as cap_collections, sum(cap_trvus) as cap_trvus, sum(cap_wrvus) as cap_wrvus, 
    sum(under_charges) as under_charges, sum(under_collections) as under_collections, sum(under_trvus) as under_trvus, sum(under_wrvus) as under_wrvus, 
    sum(ffs_charges) as ffs_charges, sum(ffs_collections) as ffs_collections, sum(ffs_trvus) as ffs_trvus, sum(ffs_wrvus) as ffs_wrvus, 
    sum(copay) as copay
	from revenue_12mo_actual_cctw_by_category_and_sos py 
	group by somDiv, worksheet_desc_id;
	
	alter table budget.revenue_12mo_actual_cctw_by_category add index somDiv (somDiv);
	alter table budget.revenue_12mo_actual_cctw_by_category add index worksheet_desc_id (worksheet_desc_id);
	
	drop table if exists budget.revenue_12mo_actual_cctw_totals;
	
	create table budget.revenue_12mo_actual_cctw_totals as
	select worksheet_desc_id, somDiv, 
        sum(charges) as charges,
        sum(collections) as collections,
        sum(trvus) as trvus,
        sum(wrvus) as wrvus,
        sum(copay) as copay
    from budget.revenue_12mo_actual_cctw_totals_by_sos totals
    group by somDiv, worksheet_desc_id;
    
    alter table budget.revenue_12mo_actual_cctw_totals add index somDiv (somDiv);
    alter table budget.revenue_12mo_actual_cctw_totals add index worksheet_desc_id (worksheet_desc_id);	
    
    drop table if exists tmp_12mo_actual_revenue;

    drop table if exists tmp_12mo_CCTW;
    
    drop table if exists budget.mv_revenue_12mo_actual_cctw_totals;
    
	create table budget.mv_revenue_12mo_actual_cctw_totals as
	select worksheet_desc_id, somDiv, 'INP/OUTP' as sos, charges, collections, trvus, wrvus, copay 
	from budget.revenue_12mo_actual_cctw_totals totals
	where totals.somDiv <> 22
	union all
	select * from budget.revenue_12mo_actual_cctw_totals_by_sos where somDiv=22;
	
	alter table budget.mv_revenue_12mo_actual_cctw_totals add column (12mo_actual_id int(11) primary key auto_increment);
	alter table budget.mv_revenue_12mo_actual_cctw_totals add index somDiv (somDiv);
	alter table budget.mv_revenue_12mo_actual_cctw_totals add index worksheet_desc_id (worksheet_desc_id);
	alter table budget.mv_revenue_12mo_actual_cctw_totals add index sos (sos);
	
	/*Delete providers that are separated and have amounts less than or equal to zero*/
	DELETE
		rev.*
	FROM
		budget.mv_revenue_12mo_actual_cctw_totals rev
		JOIN budget.medgrp_revenue_worksheet_desc wd ON
			wd.worksheet_desc_id = rev.worksheet_desc_id
		LEFT OUTER JOIN dw.all_employee_view aev ON
			aev.all_employee_id = wd.all_employee_id
		LEFT OUTER JOIN dw.employee_latest_refresh_view db2e ON
			db2e.emb_employee_number = aev.employee_ucsd_id
	WHERE
		(db2e.emp_employment_status_code = 'S' OR
		db2e.emb_employee_number IS NULL) AND
		(round(collections,2) = 0.00 and round(wrvus,2) = 0.00);	
		
	/*Dont include Opthamology since they are not in EPIC*/		
	/*DELETE FROM budget.mv_revenue_12mo_actual_cctw_totals WHERE somDiv = 18;*/
END
GO

call budget.loadRevenueRolling12MonthActuals()
GO

drop procedure if exists budget.loadRevenueRolling12MonthActuals
GO