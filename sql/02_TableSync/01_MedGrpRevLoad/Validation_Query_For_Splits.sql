-- validation query
/*
select cctw.total_wrvus_amt, cat.total_cat_amt, (cat.total_cat_amt - cctw.total_wrvus_amt) as diff, cctw.division_id from (
    select sum(py_wrvus) as total_wrvus_amt, division_id from v_medgrp_revenue_worksheet
    group by division_id
    ) cctw
    join (
        select sum(total_cap_wrvus) + sum(total_under_wrvus) + sum(total_ffs_wrvus) as total_cat_amt, division_id from revenue_prior_actual_split_perc_by_category_and_prov
        group by division_id
    ) cat on cat.division_id = cctw.division_id
    where cctw.total_wrvus_amt <> cat.total_cat_amt
*/