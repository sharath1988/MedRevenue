DROP TABLE IF EXISTS budget.medgrp_revenue_other_income
GO
CREATE TABLE budget.medgrp_revenue_other_income (
    division_id     int(11) PRIMARY KEY NOT NULL,
    co_payment      int(11) NOT NULL,
    incentive       int(11) NOT NULL,
    membership      int(11) NOT NULL, 
    stip            int(11) NOT NULL,
    other           int(11) NOT NULL
) ENGINE=InnoDB
GO