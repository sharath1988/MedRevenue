/**
 * This procedure is responsible for loading static metadata related to the budget app
 * 
 * -- TODO  Create a procedure that calculates the CAP rate based on business rules
 */
drop procedure if exists budget.loadBudgetMetadata
/

create procedure budget.loadBudgetMetadata()
BEGIN
		
	drop table if exists budget.budgetMetadata;
	
	create table budget.budgetMetadata (
        id int primary key auto_increment,
	    budget_start_date date not null default '2014-01-01',
	    budget_end_date date not null default '2014-12-31',
	    budget_current_year varchar(4),
	    budget_prior_year varchar(4),
	    cap_rate decimal (5,2),
	    active_flag bit(1)
	);
	
	insert into budget.budgetMetadata (budget_start_date, budget_end_date, budget_current_year, budget_prior_year, cap_rate, active_flag) values ('2014-01-01', '2014-12-31', '2016', '2014','42.67', 1);
END
/

call budget.loadBudgetMetadata()
/

drop procedure if exists budget.loadBudgetMetadata
/