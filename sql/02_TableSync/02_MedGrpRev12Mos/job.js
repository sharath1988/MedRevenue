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
	'02_MedGrpRevenueLoadRolling12MonthActuals.sql',
	'03_MedGrpRevenueRolling12MonthsCleanup.sql'
];

docFiles.forEach(function(docFile) {
	dc.executeFile(dc.sqlFile({alias: docAlias, filename: docFile, delimiter: 'GO'}));
});

jobAliases.forEach(function(jobAlias) {
	jobFiles.forEach(function(jobFile) {
		dc.executeFile(dc.sqlFile({alias: jobAlias, filename: jobFile, delimiter: 'GO'}));
	});
});