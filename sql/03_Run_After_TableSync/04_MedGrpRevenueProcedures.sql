/*
	This stored procedure allows for quick searching of addable 
	providers for the auto-complete on the revenue worksheet.  A
	procedure was used here because the exists syntax (though
	it performs well) cannot be easily made into a view.
	
	For a benchmark, a flattened view version took 14 seconds to
	return results.  Whereas this procedure took 1 second to return
	the same results with the same parameters.
*/
DROP PROCEDURE IF EXISTS budget.find_addable_providers
GO
CREATE PROCEDURE budget.find_addable_providers
    (IN p_query VARCHAR(512), IN p_division_id INT, IN p_sos VARCHAR(40), IN p_limit INTEGER) BEGIN
    SELECT
        aev.* 
    FROM
        dw.all_employee_view aev
    WHERE 
		(status IS NULL OR status = 'A') AND
		employee_fullname LIKE CONCAT(p_query,'%') AND 
		NOT EXISTS
			(SELECT 
				1 
			 FROM 
				v_provider_in_worksheet 
			WHERE
				aev.employee_fullname LIKE CONCAT(p_query,'%') AND 
				aev.all_employee_id = all_employee_id AND 
				division_id = p_division_id AND (p_sos = 'INP/OUTP' OR sos = p_sos))
	LIMIT p_limit;
END
GO

/* NOTE: This procedure is dependent upon SalPro. */
DROP PROCEDURE IF EXISTS budget.add_faculty_tbn_info
GO
CREATE PROCEDURE budget.add_faculty_tbn_info
    (
		IN p_all_employee_id INT,
        IN p_degree VARCHAR(16),
        IN p_compPlan VARCHAR(8),
        IN p_rank VARCHAR(16),
        IN p_step VARCHAR(8),
        IN p_offScale BIT(1),
        IN p_appointment DECIMAL(6,2)
    )	
	CALL salpro.add_employee_from_tbn(p_all_employee_id,p_degree,p_compPlan,p_rank,p_step,p_offScale,p_appointment);
GO
/*
Version of the procedure that was used to test it without real SalPro dependency.

CREATE PROCEDURE budget.add_faculty_tbn_info
    (
		IN p_all_employee_id INT,
        IN p_degree VARCHAR(16),
        IN p_compPlan VARCHAR(8),
        IN p_rank VARCHAR(16),
        IN p_step VARCHAR(8),
        IN p_offScale BIT(1),
        IN p_appointment DECIMAL(6,2)
    )
BEGIN
    DROP TABLE IF EXISTS dw.salpro_int_test;
    CREATE TABLE dw.salpro_int_test AS
        SELECT p_all_employee_id, p_degree, p_compPlan, p_rank, p_step, p_offScale, p_appointment;
END
GO
*/