/* Drop the procedure */
DROP PROCEDURE IF EXISTS budget.separatedUserRemoval
GO

/* Create the procedure to remove separated users */
CREATE PROCEDURE budget.separatedUserRemoval() 
BEGIN 
	DELETE FROM 
		budget.emp_divisions 
	WHERE 
		employee_ucsd_id IN ( 
			SELECT * FROM ( 
				SELECT 
					target.employee_ucsd_id 
				FROM 
					dw.db2_p_employee e, 
					budget.emp_divisions target 
				WHERE 
					e.emb_employee_number = target.employee_ucsd_id AND 
					e.emp_employment_status_code = 'S' 
				GROUP BY 
					target.employee_ucsd_id 
			) AS separated 
		); 
		
	DELETE FROM 
		budget.emp_departments 
	WHERE 
		employee_ucsd_id IN ( 
			SELECT * FROM ( 
				SELECT 
					target.employee_ucsd_id 
				FROM 
					dw.db2_p_employee e, 
					budget.emp_departments target 
				WHERE 
					e.emb_employee_number = target.employee_ucsd_id AND 
					e.emp_employment_status_code = 'S' 
				GROUP BY 
					target.employee_ucsd_id 
			) AS separated 
		); 
			
	DELETE FROM 
		budget.emp_index 
	WHERE 
		employee_ucsd_id IN ( 
			SELECT * FROM ( 
				SELECT 
					target.employee_ucsd_id 
				FROM 
					dw.db2_p_employee e, 
					budget.emp_index target 
				WHERE 
					e.emb_employee_number = target.employee_ucsd_id AND 
					e.emp_employment_status_code = 'S' 
				GROUP BY 
					target.employee_ucsd_id 
			) AS separated 
		); 

	DELETE FROM 
		budget.emp_orgs 
	WHERE 
		employee_ucsd_id IN ( 
			SELECT * FROM ( 
				SELECT 
					target.employee_ucsd_id 
				FROM 
					dw.db2_p_employee e, 
					budget.emp_orgs target 
				WHERE 
					e.emb_employee_number = target.employee_ucsd_id AND 
					e.emp_employment_status_code = 'S' 
				GROUP BY 
					target.employee_ucsd_id 
			) AS separated 
		);		
END
GO

/* Call the procedure */
CALL budget.separatedUserRemoval()
GO

/* Drop the procedure */
DROP PROCEDURE IF EXISTS budget.separatedUserRemoval
GO