use budget
GO

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