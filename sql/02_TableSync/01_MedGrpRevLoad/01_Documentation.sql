/* 
   Job Name:        VchsRevenueLoadActual

   Purpose:         Loads prior year actual revenue from mgbs into the budget database.
                    Also responsible for loading revenue category split percentages from the 
                    prior year budget database.  Finally, it creates the revenue worksheet table for projections
                    and payment information.

   Source Data:     mgfs.mgbs
                    mgfs.mgbs_outside_billers


   Operation:       

   Scheduled Job    Job should only be run at the beginning of the budget season. 
    Reasons for scheduling at this time
                    The VCHS Revenue web app's application database tables are initially
                    created with data from prior year actuals.  Therefore, we dont want to
                    run this job until the database has been backed up and the budget
                    season is about to commence. 


   Written By:      Mike Yebba
   Date Written:    12/19/2014

   Modification History:
     Modifier's Initials   Date    Comment
     ---------------------------------------------------------------------------

*/
GO
-- 
--    Tablesync fields:
-- =========================
-- Column Name 	Description
-- Name 	    The name of the job
-- Type 	    They type of work we want to perform.
-- ReturnTable 	Where we will put the data.
-- Command 	    What we wish to execute
-- Ordinal 	    The order we will perform the Elements in the Job.
-- ConnAlias    The connection Alias we want to use for the remote data.
-- Enabled      Is the Job element enabled. A bit value of 0/1. A second security measure.
-- Comment 	    A short description of the Job element, displayed during a non-verbose Job run. 
-- 
-- 
--    Tablesync commands:
-- ========================
-- Use Type = Command to run a standard SQL command on the server pointed to by the ConnAlias 
--
-- Use Type = Create  to create a new table structure, drop any existing table is included.
--
-- Use Type = Upload  to move a new table/data from the ConnAlias (source) to the target table
--                      identified in the Command field.
-- [ PayPeriod  Upload   s_pp      SELECT* from $op-Award   10     Coeus       false  "No Comment" ]
-- 
-- Use Type = Swap    to replace an existing table with the exact same name and stucture from
--                       the source you specify in the Command field to the som_portal database
--                       on the server specified by the ConnAlias.
-- [ OverDraft  Swap overDraft   select * from overDraft  50  SomProd  true  "swap overDraft onto SomProd"]
--
--
--
-- Use Type = Push    to move table from the Command (source) to the ConnAlias (target), a table 
--                      with the same name can already exist on the target and it will overwrite it.
-- [ SomDept Push  ucsd_department    5000   SomDev2V5     true        "push ucsd_department to Dev2" ]
-- 
-- Use CreateFunc     to create a function
--  [StipFix  CreateFunc  periodStartDate  create function periodStartDate(period char(8)) RETURNS date...
--      135         JobServer     true        "create function periodStartDate"] 
-- 
-- Use CreateProc     to create a procedure
-- 
-- Use Type = Execute to execute a stored procedure or function
--  [SomDept   Execute  dw_org_status { call org_list(?) }  520 DW_DB2 true "upload org status list (DW_DB2)"]
--
-- Use Type = Drop    to drop a table from the ConnAlias database (target)
--  [PayPeriod  Drop     s_pp            (null)      1000        JobServer     true        "drop s_pp (JobServer)"] 
-- 
-- Use type = InsPdox  to move a paradox table to another paradox table
-- [ Index    InsPdox  IndxFyl   select * from ifoapal  410   PdoxLcl  false  "swap ifoapal to IndxFyl.db (test)" ]
-- 
-- Use Type = Procedure  to run a procedure
--  [ SomRollup	Procedure	(null)	{ call som_portal.createSomRollup() }	150	SomDev2V5	true	run createSomRollup Dev2 ]
-- 
-- 
-- 
-- 
--    Tablesync ConnAlias
--  ============================
-- 
--  Coeus      
--  CoeusT4      
--  Darwin       
--  DW_DB2       
--  FacLink     (server  
--  JobServer   (Server no longer exists) 
--  MdbHxtt      
--  MdbLcl       
--  MdbMed       
--  MdbMsp       
--  PdoxAlloc    
--  PdoxAsa      
--  PdoxCedfHca  
--  PdoxFsA      
--  PdoxGrant    
--  PdoxLcl      
--  PdoxTest     
--  pdx          
--  SomDev    (Server no longer exists)
--  SomDev2V4 (Server no longer exists)
--  SomDev2V5 (Server no longer exists)
--  SomPortal (server = som-prod2.ucsd.edu)
--  SomProd   (Server no longer exists)  
--  SomProd2  (server = som-prod2.ucsd.edu)
--  SomQA2V2  (Server no longer exists)
--  SomQA2V5  (server = som-qa2.ucsd.edu)

-- 
-- 
--
GO