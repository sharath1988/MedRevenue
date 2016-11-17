USE budget
GO

create or replace view v_revenue_budget_exceptions as 
select * from revenue_budget_exceptions WHERE division_id = 1
GO