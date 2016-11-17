use budget
GO



show tables like 'rev%'
/
create table BackupArea.revenue_prior_actuals_by_prov_20150424 like budget.revenue_prior_actuals_by_prov
/
insert into BackupArea.revenue_prior_actuals_by_prov_20150424 select * from budget.revenue_prior_actuals_by_prov
/

create table BackupArea.medgrp_revenue_worksheet_20150424 like budget.medgrp_revenue_worksheet
/
insert into BackupArea.medgrp_revenue_worksheet_20150424 select * from budget.medgrp_revenue_worksheet
/


/**
 * CREATE TABLE medgrp_revenue_employee_merge
 * 
 * source_ein - the source of the employee record to merge.  This is typically the 999* record.  After merge, this will be set to zero
 * target_ein - the target employee record that the source_ein will be merged into.  This is typically a valid current employee_ucsd_id
 * division_id - the divisionId the records will be merged
 */
create table budget.medgrp_revenue_employee_merge (

    source_ein int not null,
    target_ein int not null,
    division_id int not null,
    merge_date datetime,
    PRIMARY KEY (source_ein, target_ein, division_id)
)
GO

-- Insert initial records for Orthopedic
INSERT INTO budget.medgrp_revenue_employee_merge(source_ein, target_ein, division_id) 
	VALUES(99976105, 13299, 19)
GO

INSERT INTO budget.medgrp_revenue_employee_merge(source_ein, target_ein, division_id) 
	VALUES(99003539, 85568, 19)
GO

INSERT INTO budget.medgrp_revenue_employee_merge(source_ein, target_ein, division_id) 
	VALUES(99071075, 928466, 19)
GO


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
        trg.cap_charges = trg.cap_charges + src.cap_charges, 
        trg.under_charges = trg.under_charges + src.under_charges,
        trg.ffs_charges = trg.ffs_charges + src.ffs_charges,
        trg.total_charges = trg.total_charges + src.total_charges,

        -- update collections
        trg.cap_collections = trg.cap_collections + src.cap_collections,
        trg.under_collections = trg.under_collections + src.under_collections,
        trg.ffs_collections = trg.ffs_collections + src.ffs_collections,
        trg.total_collections = trg.total_collections + src.total_collections,

        -- update trvus
        trg.cap_trvus = trg.cap_trvus + src.cap_trvus,
        trg.under_trvus = trg.under_trvus + src.under_trvus,
        trg.ffs_trvus = trg.ffs_trvus + src.ffs_trvus,
        trg.total_trvus = trg.total_trvus + src.total_trvus,

        -- update wrvus
        trg.cap_wrvus = trg.cap_wrvus + src.cap_wrvus,    
        trg.under_wrvus = trg.under_wrvus + src.under_wrvus,
        trg.ffs_wrvus = trg.ffs_wrvus + src.ffs_wrvus,
        trg.total_wrvus = trg.total_wrvus + src.total_wrvus,

        -- update copay
        trg.copay = trg.copay + src.copay,

        -- update rates
        trg.cap_collection_rate = (trg.cap_collections + src.cap_collections) / (trg.cap_trvus + src.cap_trvus), 
        trg.under_collection_rate = (trg.under_collections + src.under_collections) / (trg.under_trvus + src.under_trvus), 
        trg.ffs_collection_rate = (trg.ffs_collections + src.ffs_collections) / (trg.ffs_trvus + src.ffs_trvus),

        -- update splits
        trg.cap_wrvu_split = (trg.cap_wrvus + src.cap_wrvus) / (trg.total_wrvus + src.total_wrvus),
        trg.under_wrvu_split = (trg.under_wrvus + src.under_wrvus) / (trg.total_wrvus + src.total_wrvus),
        trg.ffs_wrvu_split = (trg.ffs_wrvus + src.ffs_wrvus) / (trg.total_wrvus + src.total_wrvus)
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
    
    -- update project wrvus in the revenue worksheet
    update budget.medgrp_revenue_worksheet trg
    join budget.medgrp_revenue_worksheet_desc trgDesc on trg.worksheet_desc_id = trgDesc.worksheet_desc_id
    join budget.medgrp_revenue_employee_merge m on m.target_ein = trgDesc.reference_number and m.division_id = trg.division_id
    join budget.medgrp_revenue_worksheet_desc srcDesc on m.source_ein = srcDesc.reference_number
    join budget.medgrp_revenue_worksheet src on src.worksheet_desc_id = srcDesc.worksheet_desc_id
    set trg.proj_wrvus = trg.proj_wrvus + src.proj_wrvus
    where m.merge_date is null;    

    UPDATE budget.medgrp_revenue_employee_merge set merge_date = now() where merge_date is null;

    IF `_rollback` THEN
        ROLLBACK;
    ELSE
        COMMIT;
    END IF;    
END
GO

call budget.medgrpRevenueMergeEINs() 
GO