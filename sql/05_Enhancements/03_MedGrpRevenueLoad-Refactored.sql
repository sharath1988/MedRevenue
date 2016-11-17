use mgfs
/

create table budget.mv_mgbs_current_fy_all as
select 
    mb.employeeId, mb.provider, mb.billingArea, mb.billingAreaNumber,
    mb.division as department,  mb.divisionNumber as departmentNum,
    sr.deptId as som_department_id, deptName as som_department_name, divId as som_division_id, divName as som_division_name,
    mb.charges, mb.trvu, (mb.payment - mb.paymentAdj) as collections, mb.wrvu, mb.anesUnits,
    mb.payClass, mb.originalFscType, mb.originalFscCategory,
    mb.sos,
    'INSIDE' as billing_type
from mgbs mb
join budget.budgetMetadata meta on mb.postPd >= meta.budget_start_date and mb.postPd <= meta.budget_end_date
join mgBilling b on b.number = mb.billingAreaNumber
join (select distinct deptId, deptName, divId, divName from som_portal.som_rollup) sr on sr.divId = b.somDiv

union all

select 
    mb.employeeId, mb.provider, mb.billingArea, mb.billingAreaNumber,
    mb.division as department,  mb.divisionNumber as departmentNum,
    sr.deptId as som_department_id, deptName as som_department_name, divId as som_division_id, divName as som_division_name,
    mb.charges, mb.trvu, (mb.payment - mb.paymentAdj) as collections, mb.wrvu, mb.anesUnits,
    mb.payClass, mb.originalFscType, mb.originalFscCategory,
    mb.sos,
    'OUTSIDE' as billing_type
from mgbs_outside_billers mb
join budget.budgetMetadata meta on mb.postPd >= meta.budget_start_date and mb.postPd <= meta.budget_end_date
join mgBilling b on b.number = mb.billingAreaNumber
join (select distinct deptId, deptName, divId, divName from som_portal.som_rollup) sr on sr.divId = b.somDiv
/

select * from budget.mv_mgbs_current_fy_all where som_division_id=5
/

drop table budget.mv_mgbs_current_fy_all
/

-- create budget line ids
create table budget.mv_mgbs_line_items as
select distinct billingAreaNumber as mgbs_line_id, billingArea as mgbs_line_desc, null as matched, 'billing_area' as mgbs_line_type 
from budget.mv_mgbs_current_fy_all b
union all
select distinct employeeId, provider, pps.EMB_EMPLOYEE_NUMBER as matched, 'provider' as mgbs_line_type 
from budget.mv_mgbs_current_fy_all e
left outer join dw.db2_p_employee pps on pps.EMB_EMPLOYEE_NUMBER = e.employeeId
/


select e.*, pps.EMB_EMPLOYEE_NUMBER 
from budget.mv_mgbs_line_items e
left outer join dw.db2_p_employee pps on pps.EMB_EMPLOYEE_NAME = e.mgbs_line_desc
where matched is null and mgbs_line_type = 'provider' and pps.EMB_EMPLOYEE_NUMBER is not null
order by mgbs_line_desc
/

drop table budget.mv_mgbs_line_items
/