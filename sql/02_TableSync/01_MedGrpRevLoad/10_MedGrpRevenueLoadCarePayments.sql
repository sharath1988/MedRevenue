use budget
GO

CREATE TABLE IF NOT EXISTS budget.stg_care_payment (
	BILLER_TYPE VARCHAR(32),
    UNIT_TYPE VARCHAR(256),
    POSTING_MONTH INT,
    POSTING_YEAR INT,
    BILLING_PROVIDER_ID INT,
    BILLING_PROVIDER_NAME VARCHAR(256),
    BILLING_PROVIDER_TYPE VARCHAR(256),
    BILLING_PROVIDER_TYPE_GROUP VARCHAR(256),
    BILLING_PROVIDER_SPECIALITY VARCHAR(256),
    SERVICE_PROVIDER_ID INT,
    SERVICE_PROVIDER_NAME VARCHAR(256),
    SERVICE_PROVIDER_TYPE VARCHAR(256),
    SERVICE_PROVIDER_TYPE_GROUP VARCHAR(256),
    TX_BILL_AREA_NAME VARCHAR(256),
    TX_BILL_AREA_ID INT,
    BA_INDEX VARCHAR(256),
    BILL_AREA_SPECIALITY VARCHAR(256),
    DIV_ID INT,
    DIV_NAME VARCHAR(256),
    SUBDIV_ID INT,
    SUBDIV_NAME VARCHAR(256),
    SER_MEDIAN DECIMAL(65,4),
    BA_MEDIAN DECIMAL (65,4),
    --BA_TEMP_RATE DECIMAL (65,4),
    EMP_NAME VARCHAR(256),
    EMP_ID_MPI_ID INT,
    POSTED_UNITS DECIMAL (65,4),
    LOGIC VARCHAR(256),
    RATE_SPECIALITY VARCHAR(256),
    RATE DECIMAL (65,4),
    CARE_PAYMENT_AMOUNT DECIMAL (65,4)
)
GO

/*
 * Procedure responsible for joining the wrvu and asa unit care payment data against budgeted wrvus
 */
DROP PROCEDURE IF EXISTS budget.sp_LOAD_CARE_PAYMENT_WORKSHEET
GO
CREATE PROCEDURE budget.sp_LOAD_CARE_PAYMENT_WORKSHEET()
BEGIN
	
	DROP TABLE IF EXISTS TMP_CARE_PAYMENT_CY_HIST
	;
	
	call budget.sp_REFRESH_PROVIDER_LOOKUP_INFO()
	;

	-- map EMP_IN to providerLookupViews
	CREATE TABLE budget.TMP_CARE_PAYMENT_CY_HIST AS
	SELECT 
	    COALESCE(pluc.ucsdName, pli.ucsdName,plini.ucsdName,plin.ucsdName,cp.EMP_NAME) AS EMP_NAME,
	    COALESCE(pluc.ucsdEIN, pli.ucsdEIN,plini.ucsdEIN,plin.ucsdEIN,cp.EMP_ID_MPI_ID) AS EMP_ID_MPI_ID,
	    cp.UNIT_TYPE, cp.TX_BILL_AREA_ID, cp.POSTED_UNITS, cp.RATE, 
	    mg.somDiv AS SOM_DIVISION_ID, mg.indx, 
	    (CASE WHEN LOGIC = 'LOGIC 4' THEN 'AHP' ELSE 'Provider' END) AS BILLING_TYPE
	FROM budget.stg_care_payment cp
	JOIN mgfs.mgBilling mg ON mg.number = cp.TX_BILL_AREA_ID
    LEFT OUTER JOIN (select distinct ucsdEIN, ucsdName from tmpProviderLookupInfoEIN) pluc on pluc.ucsdEIN = cp.EMP_ID_MPI_ID
	LEFT OUTER JOIN budget.tmpProviderLookupInfoEIN pli ON pli.mgbsPvdEmpId = cp.EMP_ID_MPI_ID
    LEFT OUTER JOIN budget.tmpProviderLookupInfoEINName plini ON plini.mgbsPvdEmpId = cp.EMP_ID_MPI_ID and plini.mgbsPvdEmpName = cp.EMP_NAME
	LEFT OUTER JOIN budget.tmpProviderLookupInfoEmpName plin ON plin.mgbsPvdEmpName = cp.EMP_NAME
	;	
	
	call sp_REFRESH_PROVIDER_LOOKUP_INFO_CLEANUP()
	;
    
    DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_POSTED_WRVUS_BY_DIV
    ;  
    
    CREATE TABLE budget.TMP_CARE_PAYMENT_POSTED_WRVUS_BY_DIV AS 
    SELECT EMP_NAME, EMP_ID_MPI_ID, SOM_DIVISION_ID, SUM(POSTED_UNITS) TOTAL_WRVUS
    FROM budget.TMP_CARE_PAYMENT_CY_HIST 
    WHERE UNIT_TYPE = 'wRVU'
    GROUP BY EMP_NAME, EMP_ID_MPI_ID, SOM_DIVISION_ID
    ;
    
    /*ALTER TABLE budget.TMP_CARE_PAYMENT_POSTED_WRVUS_BY_DIV ADD INDEX idx_name_id_div (EMP_NAME, EMP_ID_MPI_ID, SOM_DIVISION_ID)
    ;*/
    
    ALTER TABLE budget.TMP_CARE_PAYMENT_POSTED_WRVUS_BY_DIV ADD INDEX (EMP_ID_MPI_ID, SOM_DIVISION_ID)
    ;    

    DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_HIST_WRVU_PERC
    ;
    
    CREATE TABLE budget.TMP_CARE_PAYMENT_HIST_WRVU_PERC AS
    SELECT 
        cp.EMP_NAME, cp.EMP_ID_MPI_ID, /*cp.ALL_EMP_ID,*/
        cp.TX_BILL_AREA_ID, cp.SOM_DIVISION_ID,
        sum(cp.POSTED_UNITS) as POSTED_WRVU, 
        t.TOTAL_WRVUS, (SUM(POSTED_UNITS) / t.TOTAL_WRVUS) AS HIST_WRVU_PERC
    FROM budget.TMP_CARE_PAYMENT_CY_HIST cp
    LEFT JOIN budget.TMP_CARE_PAYMENT_POSTED_WRVUS_BY_DIV t ON /*t.EMP_NAME = cp.EMP_NAME AND*/ t.EMP_ID_MPI_ID = cp.EMP_ID_MPI_ID AND t.SOM_DIVISION_ID = cp.SOM_DIVISION_ID
    WHERE cp.UNIT_TYPE = 'wRVU'
    GROUP BY cp.EMP_NAME, cp.EMP_ID_MPI_ID, cp.TX_BILL_AREA_ID, cp.SOM_DIVISION_ID
    ;
    
    DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_BILLING_TYPE_PERC
    ;

    CREATE TABLE budget.TMP_CARE_PAYMENT_BILLING_TYPE_PERC AS
	SELECT 
	    cp.EMP_NAME, cp.EMP_ID_MPI_ID, cp.SOM_DIVISION_ID, cp.TX_BILL_AREA_ID, cp.BILLING_TYPE, 
	    SUM(cp.POSTED_UNITS) AS POSTED_WRVU_FOR_BILLING_TYPE, ba.POSTED_WRVU_FOR_BILLING_AREA,
	    (SUM(cp.POSTED_UNITS) / ba.POSTED_WRVU_FOR_BILLING_AREA) AS BILLING_TYPE_PERC,
	    cp.RATE
	FROM budget.TMP_CARE_PAYMENT_CY_HIST cp
	JOIN (
	    SELECT EMP_NAME, EMP_ID_MPI_ID, TX_BILL_AREA_ID, SUM(POSTED_UNITS) AS POSTED_WRVU_FOR_BILLING_AREA 
	    FROM budget.TMP_CARE_PAYMENT_CY_HIST
        WHERE UNIT_TYPE = 'wRVU'
	    GROUP BY EMP_NAME, EMP_ID_MPI_ID, TX_BILL_AREA_ID
	) ba ON /*ba.EMP_NAME = cp.EMP_NAME AND*/ ba.EMP_ID_MPI_ID = cp.EMP_ID_MPI_ID AND ba.TX_BILL_AREA_ID = cp.TX_BILL_AREA_ID
    WHERE cp.UNIT_TYPE = 'wRVU'
	GROUP BY cp.EMP_NAME, cp.EMP_ID_MPI_ID, cp.TX_BILL_AREA_ID, cp.BILLING_TYPE, cp.RATE
	;
    
    DROP TABLE IF EXISTS budget.MV_CARE_PAYMENT_WORKSHEET
    ;
    
    CREATE TABLE budget.MV_CARE_PAYMENT_WORKSHEET AS
	SELECT bt.*, h.HIST_WRVU_PERC
	FROM budget.TMP_CARE_PAYMENT_BILLING_TYPE_PERC bt
	JOIN budget.TMP_CARE_PAYMENT_HIST_WRVU_PERC h ON h.TX_BILL_AREA_ID = bt.TX_BILL_AREA_ID AND /*h.EMP_NAME = bt.EMP_NAME AND*/ h.EMP_ID_MPI_ID = bt.EMP_ID_MPI_ID
	;
	
	ALTER TABLE budget.MV_CARE_PAYMENT_WORKSHEET ADD INDEX idx_EMP_ID_MPI_ID_DIV (EMP_ID_MPI_ID, SOM_DIVISION_ID)
	;
			    
    DROP TABLE IF EXISTS budget.MV_CARE_PAYMENT_WORKSHEET_ANES
    ;
    
    /* MV for anesthesiology : need to pull in asa rates and wrvu rates */
    CREATE TABLE budget.MV_CARE_PAYMENT_WORKSHEET_ANES AS
	SELECT BILLING_AREA_ID, SOM_DIVISION_ID, MAX(ASA_RATE) AS ASA_RATE, MAX(WRVU_RATE) AS WRVU_RATE FROM (
	    select distinct 
	        TX_BILL_AREA_ID AS BILLING_AREA_ID, somDiv AS SOM_DIVISION_ID, RATE as ASA_RATE, null AS WRVU_RATE
	    from budget.TMP_CARE_PAYMENT_CY_HIST cp
	    join mgfs.mgBilling mg on mg.number = cp.TX_BILL_AREA_ID
	    where UNIT_TYPE = 'ASA' AND somDiv = 1
	
	    union all
	
	    select distinct TX_BILL_AREA_ID, somDiv, null, RATE 
	    from budget.TMP_CARE_PAYMENT_CY_HIST cp
	    join mgfs.mgBilling mg on mg.number = cp.TX_BILL_AREA_ID
	    where UNIT_TYPE = 'wRVU' AND somDiv = 1
	    order by SOM_DIVISION_ID, BILLING_AREA_ID
	) u
	GROUP BY BILLING_AREA_ID, SOM_DIVISION_ID    
	;
	
    /*
	 * Views for the Medgrp revenue application
	 */
	CREATE OR REPLACE VIEW budget.V_MEDGRP_REVENUE_WORKSHEET_CP AS
	SELECT 
	    IFNULL(wd.reference_number, 0) AS EMPLOYEE_UCSD_ID, wd.all_employee_id AS ALL_EMP_ID, wd.description AS EMPLOYEE_NAME, w.division_id AS SOM_DIVISION_ID,
	    SUM(w.proj_wrvus) AS PROJ_WRVUS, SUM(ws.proj_charges) AS PROJ_CHARGES, 
	    CAST(concat(wd.description, '-', IFNULL(wd.reference_number, 0)) AS CHAR(512)) AS TOTAL_SORT_COL,
	    w.care_payment_rate, wd.worksheet_desc_id
	FROM budget.medgrp_revenue_worksheet_desc wd
	JOIN budget.medgrp_revenue_worksheet w ON w.worksheet_desc_id = wd.worksheet_desc_id
	JOIN budget.v_medgrp_revenue_worksheet ws on ws.division_id = w.division_id and ws.worksheet_desc_id = w.worksheet_desc_id
	GROUP BY wd.reference_number, wd.all_employee_id, wd.description, w.division_id
	ORDER BY wd.description
	;
	
	CREATE OR REPLACE VIEW budget.V_MEDGRP_REVENUE_WORKSHEET_CP_ANES AS
	SELECT 
	    r.provider AS BILLING_AREA, r.employee_ucsd_id as BILLING_AREA_ID, r.division_id AS SOM_DIVISION_ID,
	    sum(IFNULL(r.proj_cap_wrvus,0) + IFNULL(r.proj_under_wrvus,0) + IFNULL(r.proj_ffs_wrvus,0)) AS proj_wrvus,
	    sum(IFNULL(r.proj_cap_asaunits,0) + IFNULL(r.proj_under_asaunits,0) + IFNULL(r.proj_ffs_asaunits,0)) as proj_asaunits,
	    sum(IFNULL(r.proj_cap_charges,0) + IFNULL(r.proj_under_charges,0) + IFNULL(r.proj_ffs_charges,0)) as proj_charges,
	    e.proj_care_payment_wrvu_rate, e.proj_care_payment_asa_rate
	FROM v_revenue_budget_exceptions r 
	JOIN revenue_budget_exceptions e on e.id = r.id
	WHERE r.division_id = 1
	GROUP BY r.provider, r.employee_ucsd_id, r.division_id
    ;
	
	CREATE OR REPLACE VIEW budget.V_MEDGRP_REVENUE_CARE_PAYMENT_TOTAL AS
	SELECT 
	    w.EMPLOYEE_UCSD_ID, w.EMPLOYEE_NAME, w.SOM_DIVISION_ID, w.TOTAL_SORT_COL,
        SUM((CASE 
            WHEN w.care_payment_rate IS NOT NULL THEN 
                ROUND(w.care_payment_rate * w.PROJ_WRVUS, 4)
            ELSE
                ROUND(cp.BILLING_TYPE_PERC * cp.HIST_WRVU_PERC * cp.RATE * w.PROJ_WRVUS, 4)
        END)) AS PROJ_CARE_PAYMENT_AMOUNT
    FROM budget.V_MEDGRP_REVENUE_WORKSHEET_CP w        
	LEFT JOIN budget.MV_CARE_PAYMENT_WORKSHEET cp ON cp.SOM_DIVISION_ID = w.SOM_DIVISION_ID AND cp.EMP_ID_MPI_ID = w.EMPLOYEE_UCSD_ID
	GROUP BY w.EMPLOYEE_UCSD_ID, w.SOM_DIVISION_ID
	ORDER BY w.EMPLOYEE_UCSD_ID, w.SOM_DIVISION_ID
	;	
	
	CREATE OR REPLACE VIEW budget.V_MEDGRP_REVENUE_CARE_PAYMENT_DETAIL_ANES AS
	SELECT 
	    w.BILLING_AREA_ID AS EMPLOYEE_UCSD_ID, null AS ALL_EMP_ID, w.BILLING_AREA, w.SOM_DIVISION_ID, w.BILLING_AREA_ID, 'MGMA' AS BILLING_TYPE, null as BILLING_TYPE_PERC, null as HIST_WRVU_PERC, 
	    COALESCE(w.proj_care_payment_wrvu_rate, cp.WRVU_RATE,0) as WRVU_RATE, COALESCE(w.proj_care_payment_asa_rate, cp.ASA_RATE,0) AS ASA_RATE,
	    IFNULL(w.PROJ_WRVUS,0) AS PROJ_WRVUS, IFNULL(w.PROJ_ASAUNITS,0) AS PROJ_ASAUNITS, IFNULL(w.proj_charges,0) AS PROJ_CHARGES,
	    (IFNULL(w.PROJ_WRVUS,0) * COALESCE(w.proj_care_payment_wrvu_rate, cp.WRVU_RATE,0)) + (IFNULL(w.PROJ_ASAUNITS,0) * COALESCE(w.proj_care_payment_asa_rate, cp.ASA_RATE,0)) as PROJ_CARE_PAYMENT_AMOUNT,
	    null as TOTAL_PROJ_CARE_PAYMENT_AMOUNT, 
	    CAST(concat(w.BILLING_AREA, '-', IFNULL(w.BILLING_AREA_ID, 0)) AS CHAR(512)) AS TOTAL_SORT_COL, w.BILLING_AREA_ID as worksheet_desc_id
	FROM budget.V_MEDGRP_REVENUE_WORKSHEET_CP_ANES w
	LEFT JOIN budget.MV_CARE_PAYMENT_WORKSHEET_ANES cp ON cp.BILLING_AREA_ID = w.BILLING_AREA_ID AND cp.SOM_DIVISION_ID = w.SOM_DIVISION_ID
    ;
	
	CREATE OR REPLACE VIEW budget.V_MEDGRP_REVENUE_CARE_PAYMENT_DETAIL AS
	SELECT 
	    w.EMPLOYEE_UCSD_ID, w.ALL_EMP_ID, w.EMPLOYEE_NAME, w.SOM_DIVISION_ID, 
	    IFNULL(cp.TX_BILL_AREA_ID,0) AS TX_BILL_AREA_ID, IFNULL(cp.BILLING_TYPE, '') AS BILLING_TYPE, cp.BILLING_TYPE_PERC, cp.HIST_WRVU_PERC, 
	    COALESCE(w.care_payment_rate, cp.RATE) AS RATE, 0 AS ASA_RATE,
	    IFNULL(w.PROJ_WRVUS,0) AS PROJ_WRVUS, 0 AS PROJ_ASAUNITS,
	    round(IFNULL(w.PROJ_CHARGES,0) * IFNULL(cp.BILLING_TYPE_PERC,1) * IFNULL(cp.HIST_WRVU_PERC,1), 2)  AS PROJ_CHARGES,
	    (CASE 
	        WHEN w.care_payment_rate IS NOT NULL THEN 
	            ROUND(w.care_payment_rate * w.PROJ_WRVUS, 4)
	        ELSE
	            ROUND(cp.BILLING_TYPE_PERC * cp.HIST_WRVU_PERC * cp.RATE * IFNULL(w.PROJ_WRVUS,0), 4)
	    END) AS PROJ_CARE_PAYMENT_AMOUNT, 
	    tot.PROJ_CARE_PAYMENT_AMOUNT AS TOTAL_PROJ_CARE_PAYMENT_AMOUNT,
	    w.TOTAL_SORT_COL, w.worksheet_desc_id 
	FROM budget.V_MEDGRP_REVENUE_WORKSHEET_CP w
	LEFT JOIN budget.MV_CARE_PAYMENT_WORKSHEET cp ON cp.SOM_DIVISION_ID = w.SOM_DIVISION_ID AND cp.EMP_ID_MPI_ID = w.EMPLOYEE_UCSD_ID
	LEFT JOIN budget.V_MEDGRP_REVENUE_CARE_PAYMENT_TOTAL tot ON tot.SOM_DIVISION_ID = w.SOM_DIVISION_ID AND tot.TOTAL_SORT_COL = w.TOTAL_SORT_COL
	WHERE w.SOM_DIVISION_ID <> 1 
	UNION ALL
	/* Include Anes */
	SELECT * FROM V_MEDGRP_REVENUE_CARE_PAYMENT_DETAIL_ANES
	ORDER BY SOM_DIVISION_ID, EMPLOYEE_NAME, TX_BILL_AREA_ID, BILLING_TYPE
	;

	CREATE OR REPLACE VIEW budget.V_MEDGRP_REVENUE_CARE_PAYMENT_TOTAL_BY_DIV AS
	SELECT SOM_DIVISION_ID, SUM(PROJ_CARE_PAYMENT_AMOUNT) AS PROJ_CARE_PAYMENT_AMOUNT 
	FROM V_MEDGRP_REVENUE_CARE_PAYMENT_DETAIL
	GROUP BY SOM_DIVISION_ID
	ORDER BY SOM_DIVISION_ID
	;
	
	/* view for salpro */
	CREATE OR REPLACE VIEW budget.v_medgrp_revenue_carepayment AS 
	SELECT 
	    sdiv.department_id, sdiv.division_id, 
	    cp.ALL_EMP_ID AS all_employee_id, 
	    cp.EMPLOYEE_UCSD_ID AS employee_ucsd_id, 
        cp.EMPLOYEE_NAME as employee_name,
	    md.budget_current_year AS budget_current_fy,
        PROJ_WRVUS as proj_wrvus,
        PROJ_ASAUNITS as proj_asaunits,
	    SUM(IFNULL(cp.PROJ_CARE_PAYMENT_AMOUNT,0)) AS proj_care_payment_amount
	FROM budget.V_MEDGRP_REVENUE_CARE_PAYMENT_DETAIL cp
	JOIN som_portal.som_division sdiv ON sdiv.division_id = cp.SOM_DIVISION_ID
	JOIN budget.budgetMetadata md
	GROUP BY sdiv.department_id, sdiv.division_id, cp.ALL_EMP_ID, cp.EMPLOYEE_UCSD_ID, md.budget_current_year, PROJ_WRVUS, PROJ_ASAUNITS
	ORDER BY sdiv.division_id, cp.EMPLOYEE_UCSD_ID
	;
	
	
	CREATE OR REPLACE VIEW budget.V_MEDGRP_REVENUE_CARE_PAYMENT_RATES AS
	SELECT DISTINCT 
        concat(SOM_DIVISION_ID, '-', TX_BILL_AREA_ID, '-', BILLING_TYPE, '-', employee_ucsd_id) as ID,
        SOM_DIVISION_ID, sdiv.name AS SOM_DIVISION_NAME, 
        TX_BILL_AREA_ID, mg.name AS BILL_AREA_NAME,
        RATE, BILLING_TYPE, employee_ucsd_id AS LOGGED_IN_EIN
	FROM budget.MV_CARE_PAYMENT_WORKSHEET 
    LEFT JOIN som_portal.som_division sdiv ON sdiv.division_id = SOM_DIVISION_ID
    LEFT JOIN mgfs.mgBilling mg ON mg.number = TX_BILL_AREA_ID
	LEFT JOIN budget.v_employee_division e on e.division_id = SOM_DIVISION_ID
	ORDER BY employee_ucsd_id, SOM_DIVISION_ID, TX_BILL_AREA_ID	
	;
	
	DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_CY_HIST
	;

	DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_RATE
	;

    DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_POSTED_WRVUS_BY_DIV
    ;

    DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_HIST_WRVU_PERC
    ;

    DROP TABLE IF EXISTS budget.TMP_CARE_PAYMENT_BILLING_TYPE_PERC
    ;    	
END
GO

call budget.sp_LOAD_CARE_PAYMENT_WORKSHEET()
GO

/*

delete w, d, f, a
from budget.medgrp_revenue_worksheet_desc d
join budget.medgrp_revenue_worksheet w on w.worksheet_desc_id = d.worksheet_desc_id
join dw.all_employee a on a.all_employee_id = d.all_employee_id
join dw.future_employee f on f.tbn_id = a.tbn_id
where d.all_employee_id = 191824
/

select * 
from budget.medgrp_revenue_worksheet_desc d
join budget.medgrp_revenue_worksheet w on w.worksheet_desc_id = d.worksheet_desc_id
join dw.all_employee a on a.all_employee_id = d.all_employee_id
join dw.future_employee f on f.tbn_id = a.tbn_id
where d.all_employee_id = 191824

*/