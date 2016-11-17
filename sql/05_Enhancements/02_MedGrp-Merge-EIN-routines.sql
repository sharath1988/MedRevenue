drop procedure if exists medgrpRevenueMergeEINs
GO


CREATE PROCEDURE budget.medgrpRevenueMergeEINs() 
BEGIN
	
    DECLARE `_rollback` BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET `_rollback` = 1;
    START TRANSACTION;
    
    update budget.revenue_prior_actuals_by_prov trg
    join budget.medgrp_revenue_worksheet_desc trgDesc on trg.worksheet_desc_id = trgDesc.worksheet_desc_id
    join budget.medgrp_revenue_employee_merge m on m.target_ein = trgDesc.reference_number and m.division_id = trg.division_id
    join budget.medgrp_revenue_worksheet_desc srcDesc on m.source_ein = srcDesc.reference_number
    join budget.revenue_prior_actuals_by_prov src on src.worksheet_desc_id = srcDesc.worksheet_desc_id
    set 
        -- update charges
        trg.cap_charges = ifnull(trg.cap_charges,0) + ifnull(src.cap_charges,0), 
        trg.under_charges = ifnull(trg.under_charges,0) + ifnull(src.under_charges,0),
        trg.ffs_charges = ifnull(trg.ffs_charges,0) + ifnull(src.ffs_charges,0),
        trg.total_charges = ifnull(trg.total_charges,0) + ifnull(src.total_charges,0),

        -- update collections
        trg.cap_collections = ifnull(trg.cap_collections,0) + ifnull(src.cap_collections,0),
        trg.under_collections = ifnull(trg.under_collections,0) + ifnull(src.under_collections,0),
        trg.ffs_collections = ifnull(trg.ffs_collections,0) + ifnull(src.ffs_collections,0),
        trg.total_collections = ifnull(trg.total_collections,0) + ifnull(src.total_collections,0),

        -- update trvus
        trg.cap_trvus = ifnull(trg.cap_trvus,0) + ifnull(src.cap_trvus,0),
        trg.under_trvus = ifnull(trg.under_trvus,0) + ifnull(src.under_trvus,0),
        trg.ffs_trvus = ifnull(trg.ffs_trvus,0) + ifnull(src.ffs_trvus,0),
        trg.total_trvus = ifnull(trg.total_trvus,0) + ifnull(src.total_trvus,0),

        -- update wrvus
        trg.cap_wrvus = ifnull(trg.cap_wrvus,0) + ifnull(src.cap_wrvus,0),    
        trg.under_wrvus = ifnull(trg.under_wrvus,0) + ifnull(src.under_wrvus,0),
        trg.ffs_wrvus = ifnull(trg.ffs_wrvus,0) + ifnull(src.ffs_wrvus,0),
        trg.total_wrvus = ifnull(trg.total_wrvus,0) + ifnull(src.total_wrvus,0),

        -- update copay
        trg.copay = ifnull(trg.copay,0) + ifnull(src.copay,0),

        -- update rates
        trg.cap_collection_rate = ifnull(  (ifnull(trg.cap_collections,0) + ifnull(src.cap_collections,0)) / (ifnull(trg.cap_trvus,0) + ifnull(src.cap_trvus,0)) ,0), 
        trg.under_collection_rate = ifnull(  (ifnull(trg.under_collections,0) + ifnull(src.under_collections,0)) / (ifnull(trg.under_trvus,0) + ifnull(src.under_trvus,0)), 0), 
        trg.ffs_collection_rate = ifnull(  (ifnull(trg.ffs_collections,0) + ifnull(src.ffs_collections,0)) / (ifnull(trg.ffs_trvus,0) + ifnull(src.ffs_trvus,0)), 0),

        -- update splits
        trg.cap_wrvu_split =  ifnull(  (ifnull(trg.cap_wrvus,0) + ifnull(src.cap_wrvus,0)) / (ifnull(trg.total_wrvus,0) + ifnull(src.total_wrvus,0)), 0),
        trg.under_wrvu_split = ifnull(  (ifnull(trg.under_wrvus,0) + ifnull(src.under_wrvus,0)) / (ifnull(trg.total_wrvus,0) + ifnull(src.total_wrvus,0)), 0),
        trg.ffs_wrvu_split = ifnull(  (ifnull(trg.ffs_wrvus,0) + ifnull(src.ffs_wrvus,0)) / (ifnull(trg.total_wrvus,0) + ifnull(src.total_wrvus,0)), 0)
    WHERE m.merge_date is null;

    -- update source to zero
    update budget.revenue_prior_actuals_by_prov src 
    join budget.medgrp_revenue_worksheet_desc srcDesc on src.worksheet_desc_id = srcDesc.worksheet_desc_id
    join budget.medgrp_revenue_employee_merge m on m.source_ein = srcDesc.reference_number
    set 
        -- charges
        src.cap_charges = 0,
        src.under_charges = 0,
        src.ffs_charges = 0,
        src.total_charges = 0,

        -- collections
        src.cap_collections = 0,
        src.under_collections = 0,
        src.ffs_collections = 0,
        src.total_collections = 0,

        -- trvus
        src.cap_trvus = 0,
        src.under_trvus = 0,
        src.ffs_trvus = 0,
        src.total_trvus = 0,

        -- wrvus
        src.cap_wrvus = 0,
        src.under_wrvus = 0,
        src.ffs_wrvus = 0,
        src.total_wrvus = 0,

        -- copay
        src.copay = 0,

        -- rates
        src.cap_collection_rate = 0,
        src.under_collection_rate = 0,
        src.ffs_collection_rate = 0,

        -- splits
        src.cap_wrvu_split = 0,
        src.under_wrvu_split = 0,
        src.ffs_wrvu_split = 0
    WHERE m.merge_date is null;
    
    -- update project wrvus in the revenue worksheet in the legit record
    update budget.medgrp_revenue_worksheet trg
    join budget.medgrp_revenue_worksheet_desc trgDesc on trg.worksheet_desc_id = trgDesc.worksheet_desc_id
    join budget.medgrp_revenue_employee_merge m on m.target_ein = trgDesc.reference_number and m.division_id = trg.division_id
    join budget.medgrp_revenue_worksheet_desc srcDesc on m.source_ein = srcDesc.reference_number
    join budget.medgrp_revenue_worksheet src on src.worksheet_desc_id = srcDesc.worksheet_desc_id
    set trg.proj_wrvus = ifnull(trg.proj_wrvus,0) + ifnull(src.proj_wrvus,0)
    where m.merge_date is null;    

    -- set project wrvus for the source to zero
    update budget.medgrp_revenue_worksheet src
    join budget.medgrp_revenue_worksheet_desc srcDesc on src.worksheet_desc_id = srcDesc.worksheet_desc_id
    join budget.medgrp_revenue_employee_merge m on m.source_ein = srcDesc.reference_number and m.division_id = src.division_id
    set src.proj_wrvus = 0
    where m.merge_date is null;   

    UPDATE budget.medgrp_revenue_employee_merge set merge_date = now() where merge_date is null;

    IF `_rollback` THEN
        ROLLBACK;
    ELSE
        COMMIT;
    END IF;    
END
GO