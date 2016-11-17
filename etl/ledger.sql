use budget
GO

/**
 *  Copy of cc_parameters from mgStaging 
 */
CREATE TABLE cc_parameters_prior_fy  ( 
	PERIOD                 	varchar(50) NULL,
	FIN_DIV_NAME           	varchar(50) NULL,
	FIN_SUBDIV_NAME        	varchar(50) NULL,
	BILL_AREA_ID           	varchar(18) NULL,
	BILL_AREA_NAME         	varchar(50) NULL,
	POS_ID                 	varchar(50) NULL,
	POS_NAME               	varchar(50) NULL,
	LOC_ID                 	varchar(50) NULL,
	LOC_NAME               	varchar(50) NULL,
	GL_PREFIX              	varchar(18) NULL,
	GL_INDEX               	varchar(18) NULL,
	PL_CATEGORY            	varchar(50) NULL,
	ORIG_PLAN_ID           	varchar(18) NULL,
	ORIG_FSC               	varchar(50) NULL,
	ADJUSTMENT_CATEGORY    	varchar(50) NULL,
	CHARGE                 	decimal(18,4) NULL,
	PAYMENT                	decimal(18,4) NULL,
	ADJUSTMENT             	decimal(18,4) NULL,
	ORIG_CHARGE_POST_PERIOD	varchar(10) NULL 
	)
GO

CREATE TABLE znb_parameters_prior_fy  ( 
	SERVICE_PERIOD         	varchar(50) NULL,
	FIN_DIV_NAME           	varchar(50) NULL,
	FIN_SUBDIV_NAME        	varchar(50) NULL,
	BILL_AREA_ID           	varchar(18) NULL,
	BILL_AREA_NAME         	varchar(50) NULL,
	POS_ID                 	varchar(50) NULL,
	POS_NAME               	varchar(50) NULL,
	LOC_ID                 	varchar(50) NULL,
	LOC_NAME               	varchar(50) NULL,
	GL_PREFIX              	varchar(18) NULL,
	GL_INDEX               	varchar(18) NULL,
	PL_CATEGORY            	varchar(50) NULL,
	ORIG_PLAN_ID           	varchar(18) NULL,
	ORIG_FSC               	varchar(50) NULL,
	ADJUSTMENT_CATEGORY    	varchar(50) NULL,
	CHARGE                 	decimal(18,4) NULL,
	PAYMENT                	decimal(18,4) NULL,
	ADJUSTMENT             	decimal(18,4) NULL,
	ORIG_CHARGE_POST_PERIOD	varchar(10) NULL 
	)
GO