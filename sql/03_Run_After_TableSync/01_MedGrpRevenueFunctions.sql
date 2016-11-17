-- Calculate project Wrvus by category
drop function if exists budget.calculateProjWrvusByCategory
GO

create function budget.calculateProjWrvusByCategory(
	proj_wrvus decimal(65,8), py_actual_wrvus decimal(65,8),  
	py_div_cat_split decimal(65,8), py_prov_cat_split decimal(65,8)) 
RETURNS decimal(20,2)
DETERMINISTIC
CONTAINS SQL
BEGIN

    declare projected_wrvus_cat decimal(20,2) default 0.00;

    IF py_actual_wrvus <= 0 THEN
        set projected_wrvus_cat = round((proj_wrvus * py_div_cat_split), 2);    
    ELSE
        set projected_wrvus_cat = round((proj_wrvus * py_prov_cat_split), 2);
    END IF;

    RETURN projected_wrvus_cat;
END
GO

-- Calculate Projected Trvus
drop function if exists calculateProjectedTrvus
GO

/**
 * FUNCTION: calculateProjectedTrvus
 * 
 * This function calculates projected trvus based on the projected wRVUs entered by the user
 * at a PROVIDER level.
 * 
 * Psuedocode:
 * 
 * IF PROVIDER prior year actual wrvus are less than or equal to zero THEN
 * 		provider projected tRVUs = proj provider wrvus * (prior year actual DIVISION TOTAL trvus / prior year actual DIVISION TOTAL wrvus)
 * ELSE IF PROVIDER prior year actual wrvus are greater than zero THEN
 * 		provider projected tRVUS = proj provider wrvus * (prior year actual PROVIDER trvus / prior year actual PROVIDER wrvus)
 * END IF
 */
create function budget.calculateProjectedTrvus(proj_wrvus decimal(65,8), py_actual_trvus decimal(65,8), py_actual_wrvus decimal(65,8), py_total_trvus decimal(65,8), py_total_wrvus decimal(65,8)) RETURNS decimal(20,2)
DETERMINISTIC
CONTAINS SQL
BEGIN

    declare projected_trvus decimal(20,2) default 0.00;
    declare py_trvus_per_wrvus decimal(65,8) default 0.00;

    IF py_actual_wrvus <= 0 THEN
        set py_trvus_per_wrvus = round((py_total_trvus / py_total_wrvus), 8);    
    ELSE
        set py_trvus_per_wrvus = round((py_actual_trvus / py_actual_wrvus), 8);
    END IF;

    set projected_trvus = round((proj_wrvus * py_trvus_per_wrvus), 2);

    RETURN projected_trvus;
END
GO

drop function if exists budget.calculateTrvusByCategory
GO

create function budget.calculateTrvusByCategory(
	proj_wrvus decimal(65,8), py_actual_trvus decimal(65,8), py_actual_wrvus decimal(65,8), 
	py_total_trvus decimal(65,8), py_total_wrvus decimal(65,8), 
	py_div_cat_split decimal(65,8), py_prov_cat_split decimal(65,8)) 
RETURNS decimal(20,2)
DETERMINISTIC
CONTAINS SQL
BEGIN

    declare projected_trvus decimal(20,2) default 0.00;
    declare py_trvus_per_wrvus decimal(65,8) default 0.00;

    IF py_actual_wrvus <= 0 THEN
        set py_trvus_per_wrvus = round((py_total_trvus / py_total_wrvus * py_div_cat_split), 8);    
    ELSE
        set py_trvus_per_wrvus = round((py_actual_trvus / py_actual_wrvus * py_prov_cat_split), 8);
    END IF;

    set projected_trvus = round((proj_wrvus * py_trvus_per_wrvus), 2);

    RETURN projected_trvus;
END
GO

/*
 -- Calculate Projected Charges
 */
drop function if exists budget.calculateProjectedCharges
GO

/**
 * FUNCTION: calculateProjectedCharges
 * 
 * This function calculates projected charges based on the projected wRVUs entered by the user
 * at a PROVIDER level.
 * 
 * Psuedocode:
 * 
 * IF PROVIDER prior year actual wrvus are less than or equal to zero THEN
 * 		provider projected charges = proj provider wrvus * (prior year actual DIVISION TOTAL charges / prior year actual DIVISION TOTAL wrvus)
 * ELSE IF PROVIDER prior year actual wrvus are greater than zero THEN
 * 		provider projected charges = proj provider wrvus * (prior year actual PROVIDER charges / prior year actual PROVIDER wrvus)
 * END IF
 */
create function budget.calculateProjectedCharges(proj_wrvus decimal(65,8), py_actual_charges decimal(65,8), py_actual_wrvus decimal(65,8), py_total_charges decimal(65,8), py_total_wrvus decimal(65,8)) RETURNS decimal(20,2)
DETERMINISTIC
CONTAINS SQL
BEGIN

    declare projected_charges decimal(20,2) default 0.00;
    declare py_charges_per_wrvus decimal(65,8) default 0.00;

    IF py_actual_wrvus <= 0 THEN
        set py_charges_per_wrvus = round((py_total_charges / py_total_wrvus), 8);    
    ELSE
        set py_charges_per_wrvus = round((py_actual_charges / py_actual_wrvus), 8);
    END IF;

    set projected_charges = round((proj_wrvus * py_charges_per_wrvus), 2);

    RETURN projected_charges;
END
GO

drop function if exists budget.calculateProjectedChargesByCategory
GO

create function budget.calculateProjectedChargesByCategory(
	proj_wrvus decimal(65,8), py_actual_charges decimal(65,8), py_actual_wrvus decimal(65,8), 
	py_total_charges decimal(65,8), py_total_wrvus decimal(65,8), 
	p_prov_wrvu_split decimal (65,8), p_div_wrvu_split decimal (65,8)) RETURNS decimal(20,2)
DETERMINISTIC
CONTAINS SQL
BEGIN

    declare projected_charges decimal(20,2) default 0.00;
    declare py_charges_per_wrvus decimal(65,8) default 0.00;

    IF py_actual_wrvus <= 0 THEN
        set py_charges_per_wrvus = round((py_total_charges / py_total_wrvus), 8);
    	set projected_charges = round((proj_wrvus * py_charges_per_wrvus * p_div_wrvu_split), 2);    
    ELSE
        set py_charges_per_wrvus = round((py_actual_charges / py_actual_wrvus), 8);
        set projected_charges = round((proj_wrvus * py_charges_per_wrvus * p_prov_wrvu_split), 2);
    END IF;

    RETURN projected_charges;
END
GO

drop function if exists calculateProjectedCollections
GO

/**
 * FUNCTION: calculateProjectedCollections
 * 
 * Calculates projected collections based on projected tRVUs and projected category level
 * collections to trvu rates, and provider and division level category split percentages.
 * 
 * IF PROVIDER prior year actual wrvus is less than or equal to zero THEN
 * 		A = provider projected cap collections = DIVISION prior year actual cap split * provider projected trvus * DIV proj cap collections to trvus rate
 * 		B = provider projected under collections = DIVISION prior year actual under split * provider projected trvus * DIV proj under collections to trvus rate
 * 		C = provider projected ffs collections = DIVISION prior year actual ffs split * provider projected trvus * DIV proj ffs collections to trvus rate
 *  	provider projected collections = A + B + C
 * ELSE IF PROVIDER prior year actual wrvus is greater than zero THEN
 * 		A = provider projected cap collections = PROVIDER prior year actual cap split * provider projected trvus * PROV proj cap collections to trvus rate
 * 		B = provider projected under collections = PROVIDER prior year actual under split * provider projected trvus * PROV proj under collections to trvus rate
 * 		C = provider projected ffs collections = PROVIDER prior year actual ffs split * provider projected trvus * PROV proj ffs collections to trvus rate
 *  	provider projected collections = A + B + C  
 * END IF
 */
create function budget.calculateProjectedCollections(
    v_proj_trvus decimal (65,8), v_py_actual_wrvus decimal(65,8),
    v_proj_div_cap_rate decimal (65,8), v_proj_div_under_rate decimal (65,8), v_proj_div_ffs_rate decimal (65,8), 
    v_proj_prov_cap_rate decimal (65,8), v_proj_prov_under_rate decimal (65,8), v_proj_prov_ffs_rate decimal (65,8),
    v_prov_cap_split decimal (65,8), v_prov_under_split decimal (65,8), v_prov_ffs_split decimal (65,8),
    v_div_cap_split decimal (65,8), v_div_under_split decimal (65,8), v_div_ffs_split decimal (65,8)

) RETURNS decimal(20,2)
DETERMINISTIC
CONTAINS SQL
BEGIN

    declare projected_collections decimal(65,8) default 0.00;
    declare proj_cap_collections decimal(65,8) default 0.00;
    declare proj_under_collections decimal(65,8) default 0.00;
    declare proj_ffs_collections decimal(65,8) default 0.00;

    IF v_py_actual_wrvus <= 0 THEN
        set proj_cap_collections = round((v_proj_div_cap_rate * v_div_cap_split * v_proj_trvus),8);
        set proj_under_collections = round((v_proj_div_under_rate * v_div_under_split * v_proj_trvus),8);
        set proj_ffs_collections = round((v_proj_div_ffs_rate * v_div_ffs_split * v_proj_trvus),8);
    ELSE
        set proj_cap_collections = round((v_proj_prov_cap_rate * v_prov_cap_split * v_proj_trvus),8);
        set proj_under_collections = round((v_proj_prov_under_rate * v_prov_under_split * v_proj_trvus),8);
        set proj_ffs_collections = round((v_proj_prov_ffs_rate * v_prov_ffs_split * v_proj_trvus),8);
    END IF;

    set projected_collections = (proj_cap_collections + proj_under_collections + proj_ffs_collections);
    RETURN round(projected_collections,2);
END
GO

drop function if exists budget.calculateCollectionsByCategory
GO

/**
 * Function that calculates a rate by category
 */
create function budget.calculateCollectionsByCategory(
	p_proj_trvus decimal (65,8), p_py_actual_wrvus decimal (65,8),
	p_proj_div_category_rate decimal (65,8), p_proj_prov_category_rate decimal (65,8),
	p_prov_split_perc decimal (65,8), p_div_split_perc decimal(65,8) 
) returns decimal (65,8)
DETERMINISTIC
CONTAINS SQL
BEGIN
	declare proj_collections decimal(65,8) default 0.00;
	
	IF p_py_actual_wrvus <= 0 THEN
		set proj_collections = round((p_proj_div_category_rate * p_div_split_perc * p_proj_trvus),8);
	ELSE
		set proj_collections = round((p_proj_prov_category_rate * p_prov_split_perc * p_proj_trvus),8);
	END IF;
	
	RETURN proj_collections;
END 
GO