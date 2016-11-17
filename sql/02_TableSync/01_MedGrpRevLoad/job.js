var docAlias = 'JobServer';

var jobAliases = [
	'SomDev4',
	'SomQa4',
	'SomProd4'
];

var docFiles = [
	'01_Documentation.sql'
];

var jobFiles = [
    '01.1_MedgrpBackupCurrentBudgetActivity.sql',
	'02_MedGrpRevenueLoadPriorYearActuals.sql',
	'03_MedGrpRevenueLoadPriorYearCategorySplits.sql',
	'04_MedGrpRevenueBudgetConsolidate.sql',
	'05_MedGrpRevenueWorksheet.sql',
	'06_MedGrpRevenuePaymentInformation.sql',
	'07_MedGrpRevenueCleanup.sql',
	'08_MedGrpRevenueLoadBudgetActivity.sql',
	'09_MedGrpRevenueLoadPsych-Retrofit.sql',
	'10_MedGrpRevenueLoadCarePayments.sql',
	'11_MedGrpRevenuePerformanceProcs.sql'
];

docFiles.forEach(function(docFile) {
	dc.executeFile(dc.sqlFile({alias: docAlias, filename: docFile, delimiter: 'GO'}));
});

jobAliases.forEach(function(jobAlias) {
	jobFiles.forEach(function(jobFile) {
		dc.executeFile(dc.sqlFile({alias: jobAlias, filename: jobFile, delimiter: 'GO'}));
	});
});