use budget
;

/*
 * ZNB Division level charges, adj percentages and collection percentages
 */
DROP TABLE IF EXISTS budget.MV_ZNB_ADJUSTMENTS_PRIOR_FY
;

CREATE TABLE budget.MV_ZNB_ADJUSTMENTS_PRIOR_FY AS
SELECT 
    SERVICE_PERIOD as PERIOD,
    IFNULL(sdiv.somDivisionId, un.somDivisionId) AS SOM_DIVISION_ID, 
    IFNULL(sdiv.somDivisionName, un.somDivisionName) as SOM_DIVISION_NAME,
    IFNULL(sdiv.somDepartmentId, un.somDepartmentId) AS SOM_DEPARTMENT_ID, 
    IFNULL(sdiv.somDepartmentName, un.somDepartmentName) as SOM_DEPARTMENT_NAME,
    IFNULL(mg.indx, 'MSC4800') AS GL_INDEX,
    (CASE WHEN PL_CATEGORY = '' THEN 'OTHER' ELSE PL_CATEGORY END) as PL_CATEGORY,
	PAYMENT,
    ADJUSTMENT,
	CHARGE,
    (CASE WHEN 
        ADJUSTMENT_CATEGORY <> 'Bad Debt Write Off' 
        AND ADJUSTMENT_CATEGORY <> 'Capitation' 
        AND ADJUSTMENT_CATEGORY <>  'Contractual'
    THEN 'OTHER' else ADJUSTMENT_CATEGORY END) as ADJUSTMENT_CATEGORY
FROM budget.znb_parameters_prior_fy z
LEFT OUTER JOIN mgfs.mgBilling mg ON mg.number = z.BILL_AREA_ID
LEFT OUTER JOIN mgfs.somMgDivisionView sdiv ON sdiv.somDivisionId = mg.somDiv
JOIN mgfs.somMgDivisionView un on un.somDivisionId=91
;

/*
 * CC Division level collections
 */
DROP TABLE IF EXISTS budget.MV_CC_COLLECTIONS_PRIOR_FY
;

CREATE TABLE budget.MV_CC_COLLECTIONS_PRIOR_FY AS
SELECT 
    PERIOD,
    IFNULL(sdiv.somDivisionId, un.somDivisionId) AS SOM_DIVISION_ID, 
    IFNULL(sdiv.somDivisionName, un.somDivisionName) as SOM_DIVISION_NAME,
    IFNULL(sdiv.somDepartmentId, un.somDepartmentId) AS SOM_DEPARTMENT_ID, 
    IFNULL(sdiv.somDepartmentName, un.somDepartmentName) as SOM_DEPARTMENT_NAME,
    IFNULL(mg.indx, 'MSC4800') AS GL_INDEX,
    (CASE WHEN (PL_CATEGORY = '' OR PL_CATEGORY = 'NULL') THEN 'OTHER' ELSE PL_CATEGORY END) as PL_CATEGORY,
	PAYMENT,
	CHARGE
FROM budget.cc_parameters_prior_fy cc
LEFT OUTER JOIN mgfs.mgBilling mg ON mg.number = cc.BILL_AREA_ID
LEFT OUTER JOIN mgfs.somMgDivisionView sdiv ON sdiv.somDivisionId = mg.somDiv
JOIN mgfs.somMgDivisionView un on un.somDivisionId=91
;


/**
 * ZNB Pivot by PL
 */
DROP TABLE IF EXISTS budget.MV_ZNB_PIVOT_BY_PL
;

CREATE TABLE budget.MV_ZNB_PIVOT_BY_PL AS
SELECT
    PERIOD, GL_INDEX, SOM_DIVISION_ID, SOM_DIVISION_NAME, PL_CATEGORY, sum(CHARGE) as CHARGES,
    SUM(CASE WHEN ADJUSTMENT_CATEGORY = 'Bad Debt Write Off' THEN ADJUSTMENT ELSE 0 END) AS BAD_DEBT_ADJ,
    SUM(CASE WHEN ADJUSTMENT_CATEGORY = 'Capitation' THEN ADJUSTMENT ELSE 0 END) AS CAPITATION_ADJ,
    SUM(CASE WHEN ADJUSTMENT_CATEGORY = 'Contractual' THEN ADJUSTMENT ELSE 0 END) AS CONTRACTUAL_ADJ,
    SUM(CASE WHEN ADJUSTMENT_CATEGORY = 'Other' THEN ADJUSTMENT ELSE 0 END) AS OTHER_ADJ,
	SUM(ADJUSTMENT) AS TOTAL_ADJ
FROM budget.MV_ZNB_ADJUSTMENTS_PRIOR_FY z
GROUP BY PERIOD, GL_INDEX, SOM_DIVISION_ID, SOM_DIVISION_NAME, PL_CATEGORY
;


/**
 * COLLECTIONS PIVOT BY PL
 */
DROP TABLE IF EXISTS budget.MV_CC_PIVOT_BY_PL
;

CREATE TABLE budget.MV_CC_PIVOT_BY_PL AS
SELECT
    PERIOD, GL_INDEX, SOM_DIVISION_ID, SOM_DIVISION_NAME, PL_CATEGORY,
	SUM(PAYMENT) AS TOTAL_COLLECTIONS
FROM budget.MV_CC_COLLECTIONS_PRIOR_FY z
GROUP BY PERIOD, GL_INDEX, SOM_DIVISION_ID, SOM_DIVISION_NAME, PL_CATEGORY
;

/**
 * Division level adjustment percentages by PL and Adjustment category
 */
DROP TABLE IF EXISTS budget.MV_ADJ_PERC_BY_DIV
;

CREATE TABLE budget.MV_ADJ_PERC_BY_DIV AS
SELECT 
    --pr.PR, 
    z.SOM_DIVISION_ID, z.SOM_DIVISION_NAME, z.PL_CATEGORY,

    --dc.CHARGES AS ZNB_DIV_CHARGES, dc.ADJ_PERC AS ZNB_DIV_ADJ_PERC, dc.COLL_PERC AS ZNB_DIV_COLL_PERC, 

    SUM(z.CHARGES) as ZNB_PL_CHARGES, MAX(ch.TOTAL_CHARGES) TOTAL_ZNB_CHARGES, (SUM(z.CHARGES) / MAX(ch.TOTAL_CHARGES)) as PL_CHARGE_PERC,

    SUM(IFNULL(c.TOTAL_COLLECTIONS,0)) as COLLECTIONS,
    SUM(z.BAD_DEBT_ADJ) AS BAD_DEBT_ADJ,
    SUM(z.BAD_DEBT_ADJ) / sum(NULLIF(z.CHARGES,0)) AS BAD_DEBT_ADJ_PERC,

    --	Capitated for Capitated Payer includes Capitated + Contractual
    -- 	Capitated for Other Payers Categories is simply Capitated Adjustments less charges
    SUM(z.CAPITATION_ADJ) AS CAPITATION_ADJ,
    (CASE 
        WHEN z.PL_CATEGORY = 'CAPITATED' THEN 
            SUM(z.CAPITATION_ADJ) / sum(NULLIF(z.CHARGES,0)) + SUM(z.CONTRACTUAL_ADJ) / sum(NULLIF(z.CHARGES,0))
        ELSE
            SUM(z.CAPITATION_ADJ) / sum(NULLIF(z.CHARGES,0))
        END
    ) AS CAPITATION_ADJ_PERC,

    SUM(z.OTHER_ADJ) AS OTHER_ADJ,
    -- For Capitated Payer catetogy, use Other Adjustments divided by Charges
    -- For Other payer categories other includes Other Divided Charges + Contractual %
    (CASE
        WHEN z.PL_CATEGORY = 'CAPITATED' THEN
            SUM(z.OTHER_ADJ) / sum(NULLIF(z.CHARGES,0))
        ELSE
            SUM(z.OTHER_ADJ) / sum(NULLIF(z.CHARGES,0)) + SUM(z.CONTRACTUAL_ADJ) / sum(NULLIF(z.CHARGES,0))
        END
    ) AS OTHER_ADJ_PERC,
	
	SUM(z.TOTAL_ADJ) as TOTAL_ADJ,
	SUM(z.TOTAL_ADJ) / sum(NULLIF(z.CHARGES,0)) AS TOTAL_ADJ_PERC	

    --SUM(z.CONTRACTUAL_ADJ) AS CONTRACTUAL_ADJ,
    --SUM(z.CONTRACTUAL_ADJ) / NULLIF(dc.CHARGES,0) AS CONTRACTUAL_ADJ_PERC
FROM budget.MV_ZNB_PIVOT_BY_PL z
LEFT JOIN budget.MV_CC_PIVOT_BY_PL c ON c.PERIOD = z.PERIOD and c.SOM_DIVISION_ID = z.SOM_DIVISION_ID and c.GL_INDEX = z.GL_INDEX and c.PL_CATEGORY = z.PL_CATEGORY
JOIN (SELECT SOM_DIVISION_ID, SUM(CHARGES) TOTAL_CHARGES 
    FROM budget.MV_ZNB_PIVOT_BY_PL
    GROUP BY SOM_DIVISION_ID) ch on ch.SOM_DIVISION_ID = z.SOM_DIVISION_ID
GROUP BY z.SOM_DIVISION_ID, z.SOM_DIVISION_NAME, z.PL_CATEGORY--, dc.CHARGES, dc.ADJ_PERC, dc.COLL_PERC
;

/**
 * Total projected charges by division.
 */
CREATE OR REPLACE VIEW budget.V_TOTAL_PROJ_CHARGES_BY_DIV AS
select division_id, total_proj_charges from budget.v_medgrp_revenue_worksheet_totals
union all
select division_id, sum(proj_cap_charges + proj_under_charges + proj_ffs_charges) as total_proj_charges from v_revenue_budget_exceptions
--select division_id, sum(total_proj_charges) as total_proj_charges from budget.v_medgrp_revenue_worksheet_totals_psych WHERE sos = 'INP/OUTP' group by division_id
;

/**
 * Need to get these accounted for
 */
/*
select c.*, p.SOM_DIVISION_ID from V_TOTAL_PROJ_CHARGES_BY_DIV c
LEFT JOIN MV_ADJ_PERC_BY_DIV p on c.division_id = p.SOM_DIVISION_ID
where p.SOM_DIVISION_ID is null
*/

CREATE OR REPLACE VIEW budget.V_ADJ_CHARGES_BY_PL_CATEGORY AS
-- inside
select 
    concat(c.division_id,'-',p.PL_CATEGORY) as id, c.division_id, c.total_proj_charges, p.PL_CATEGORY, 
    round(c.total_proj_charges * p.PL_CHARGE_PERC,2) as total_proj_pl_charges,
    p.PL_CHARGE_PERC,
    p.BAD_DEBT_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC) as BAD_DEBT_ADJ,
    p.CAPITATION_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) as CAP_ADJ, 
    p.OTHER_ADJ_PERC, round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC) as OTHER_ADJ, 

    (round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC)  +
    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) +
    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC)) as TOTAL_ADJ,
    
    (round(c.total_proj_charges * p.PL_CHARGE_PERC,2) + 

    (round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.BAD_DEBT_ADJ_PERC)  +
    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.CAPITATION_ADJ_PERC) +
    round((c.total_proj_charges * p.PL_CHARGE_PERC) * p.OTHER_ADJ_PERC))) as TOTAL_ADJ_CHARGES, 'inside' as datasource

from budget.V_TOTAL_PROJ_CHARGES_BY_DIV c
JOIN budget.MV_ADJ_PERC_BY_DIV p on c.division_id = p.SOM_DIVISION_ID
--WHERE c.division_id not in (SELECT distinct SOM_DIVISION_ID FROM ADJ_PERC_WORKSHEET_201609_in_out where DATA_SOURCE = 'outside')
;
/*
UNION ALL
-- outside
select 
    concat(c.division_id,'-',adj.PL_CATEGORY) as id, c.division_id, c.total_proj_charges, adj.PL_CATEGORY, 
    round(c.total_proj_charges,2) as total_proj_pl_charges,
    1 as PL_CHARGE_PERC,
    adj.BAD_DEBT_ADJ_PERC, round((c.total_proj_charges) * adj.BAD_DEBT_ADJ_PERC) as BAD_DEBT_ADJ,
    adj.CAP_ADJ_PERC, round((c.total_proj_charges) * adj.CAP_ADJ_PERC) as CAP_ADJ, 
    adj.OTHER_ADJ_PERC, round((c.total_proj_charges) * adj.OTHER_ADJ_PERC) as OTHER_ADJ, 

    (round((c.total_proj_charges) * adj.BAD_DEBT_ADJ_PERC)  +
    round((c.total_proj_charges) * adj.CAP_ADJ_PERC) +
    round((c.total_proj_charges) * adj.OTHER_ADJ_PERC)) as TOTAL_ADJ,
    
    (round(c.total_proj_charges,2) + 

    (round((c.total_proj_charges) * adj.BAD_DEBT_ADJ_PERC)  +
    round((c.total_proj_charges) * adj.CAP_ADJ_PERC) +
    round((c.total_proj_charges) * adj.OTHER_ADJ_PERC))) as TOTAL_ADJ_CHARGES, 'outside' as datasource

from budget.V_TOTAL_PROJ_CHARGES_BY_DIV c
JOIN ADJ_PERC_WORKSHEET_201609_in_out adj on adj.SOM_DIVISION_ID = c.division_id and adj.DATA_SOURCE = 'outside'
WHERE c.division_id in (SELECT distinct SOM_DIVISION_ID FROM ADJ_PERC_WORKSHEET_201609_in_out where DATA_SOURCE = 'outside')
;*/

CREATE OR REPLACE VIEW budget.V_TOTAL_BUDGET_ADJ_BY_DIV AS
select 
    c.division_id as DIVISION_ID, 
    max(total_proj_charges) as TOTAL_PROJ_CHARGES, 
    sum(BAD_DEBT_ADJ) TOTAL_BAD_DEBT_ADJ, (sum(BAD_DEBT_ADJ) / max(total_proj_charges)) as TOTAL_BAD_DEBT_ADJ_PERC,
    sum(CAP_ADJ) as TOTAL_CAP_ADJ, (sum(CAP_ADJ) / max(total_proj_charges)) as TOTAL_CAP_ADJ_PERC,
    sum(OTHER_ADJ) as TOTAL_OTHER_ADJ, (sum(OTHER_ADJ) / max(total_proj_charges)) as TOTAL_OTHER_ADJ_PERC,
    sum(TOTAL_ADJ) as TOTAL_ADJ, (sum(TOTAL_ADJ) / max(total_proj_charges)) as TOTAL_ADJ_PERC,
    sum(TOTAL_ADJ_CHARGES) as TOTAL_ADJ_CHARGES,
    COALESCE(max(cap.amount), max(anes.proj_cap_distribution), 0) AS TOTAL_CAP_DISTRIBUTION,
    max(total_proj_charges) + sum(TOTAL_ADJ) + IFNULL(max(cap.amount), 0) as TOTAL_EXPECTED_REVENUE -- TODO = total proj charges - total adj + total cap distribution
from budget.V_ADJ_CHARGES_BY_PL_CATEGORY c
LEFT JOIN budget.medgrp_revenue_cap_distribution cap on cap.division_id = c.division_id
LEFT JOIN budget.revenue_sheet_master anes ON anes.division_id = c.division_id
group by division_id
;

CREATE OR REPLACE VIEW budget.V_TOTAL_OTHER_ASSESSMENTS_BY_DIV AS
select 244 AS DIVISION_ID, 1 as DATA_GROUP_ID, 2 AS MISSION_ID,
sum(a.TOTAL_EXPECTED_REVENUE + IFNULL(oi.co_payment, 0) + IFNULL(oi.incentive, 0) + IFNULL(oi.membership, 0) + IFNULL(oi.stip, 0) + IFNULL(oi.other, 0)) * .06 AS OTHER_ASSESSMENTS
from budget.V_TOTAL_BUDGET_ADJ_BY_DIV a
left join budget.medgrp_revenue_other_income oi on oi.division_id = a.DIVISION_ID
;


CREATE OR REPLACE VIEW budget.V_RADIOLOGY_DIVISION_XREF_FOR_LP AS
select distinct somDiv, 23 as mapToDiv from mgfs.mgBilling mg
join som_portal.som_division sd on sd.division_id = mg.somDiv
where sd.department_id = 306
;

CREATE OR REPLACE VIEW budget.V_TOTAL_BUDGET_ADJ_BY_DIV_FOR_LP AS
select 
    COALESCE(x.mapToDiv,w.DIVISION_ID) as DIVISION_ID,
    SUM(w.TOTAL_PROJ_CHARGES) as TOTAL_PROJ_CHARGES, 
    SUM(w.TOTAL_BAD_DEBT_ADJ) as TOTAL_BAD_DEBT_ADJ, AVG(w.TOTAL_BAD_DEBT_ADJ_PERC) as TOTAL_BAD_DEBT_ADJ_PERC, 
    SUM(w.TOTAL_CAP_ADJ) as TOTAL_CAP_ADJ, AVG(w.TOTAL_CAP_ADJ_PERC) as TOTAL_CAP_ADJ_PERC, 
    SUM(w.TOTAL_OTHER_ADJ) as TOTAL_OTHER_ADJ, AVG(w.TOTAL_OTHER_ADJ_PERC) as TOTAL_OTHER_ADJ_PERC,
    SUM(w.TOTAL_ADJ) as TOTAL_ADJ, AVG(w.TOTAL_ADJ_PERC) as TOTAL_ADJ_PERC, 
    SUM(w.TOTAL_ADJ_CHARGES) as TOTAL_ADJ_CHARGES,
    SUM(w.TOTAL_CAP_DISTRIBUTION) as TOTAL_CAP_DISTRIBUTION, 
    SUM(w.TOTAL_EXPECTED_REVENUE) as TOTAL_EXPECTED_REVENUE 
from budget.V_TOTAL_BUDGET_ADJ_BY_DIV w
left join budget.V_RADIOLOGY_DIVISION_XREF_FOR_LP x on x.somDiv = w.DIVISION_ID
GROUP BY COALESCE(x.mapToDiv,w.DIVISION_ID)
;