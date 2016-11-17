DROP TABLE IF EXISTS budget.medgrp_revenue_cap_distribution
GO
create table budget.medgrp_revenue_cap_distribution (
	division_id 	int(11) PRIMARY KEY,
	amount			decimal(65,4)
) ENGINE=InnoDB
GO