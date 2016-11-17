(function($) {
	$.fn.clearFilter = function() {
		$('.worksheet-search-filter').val('');
	};
	$.fn.searchFilter = function() {
		var search = $('.worksheet-search-filter').val().toUpperCase();
		var rows = $('.revenue-worksheet-table .ui-datatable-scrollable-body table tr');
		rows.hide();
		rows.filter(function() {
			return $(this).find('td:first').text().toUpperCase().indexOf(search) !== -1;
		}).show();
	};
})(jQuery);
jQuery(function($) {
	$('body').on('keyup','.worksheet-search-filter',$.fn.searchFilter);
});