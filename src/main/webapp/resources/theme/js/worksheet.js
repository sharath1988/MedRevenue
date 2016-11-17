(function($) {
	$.hash = function(value) {
		var hashValue = value.indexOf('#') == 0 ? value : '#' + value;
		if (hashValue === location.hash) {
			return;
		}
		if (history.pushState) {
			history.pushState(null,null,hashValue);
		} else {
			location.hash = hashValue;
		}
		$(window).trigger('hashchange');
	};
	$.tab = {};
	$.tab.activeTabIndex = 0;
	$.tab.change = function(activeTabIndex) {
		$.tab.activeTabIndex = activeTabIndex;
		if (!(location.hash === '' && activeTabIndex === 0)) {
			$.hash('activeTabIndex='+$.tab.activeTabIndex);
		}
	};
	$.fn.pickOne = function(clientConfig) {
		var namespace = '.pickOne';
		var defaults = {
			className: 'pickable',
			selected: $.noop,
			canceled: $.noop
		};
		var config = $.extend({},defaults,clientConfig);
		var elements = $(this);
		function removeHandlers() {
			elements.off(namespace);
			$('body').off(namespace);
		}
		elements.on('mouseenter'+namespace,function(event) {
			event.stopPropagation();
			$(this).addClass(config.className);
		});
		elements.on('mouseleave'+namespace,function(event) {
			event.stopPropagation();
			$(this).removeClass(config.className);
		});
		$('body').on('click'+namespace,function() {
			removeHandlers();
			config.canceled.call(this);
		});
		elements.on('click'+namespace,function(event) {
			event.stopPropagation();
			$(this).removeClass(config.className);
			removeHandlers();
			config.selected.call(this);
		});
		return this;
	};
})(jQuery);
jQuery(function($) {
	window.centerFutureEmployeeDialog = function() {
		var dialog = PF('addFutureEmployeeDialogVar').jq;
		var top = Math.max(0,(($(window).height() - dialog.outerHeight()) / 2) + $(window).scrollTop()) + "px";
		var left = Math.max(0,(($(window).width() - dialog.outerWidth()) / 2) + $(window).scrollLeft()) + "px";
		dialog.css("top",top);
		dialog.css("left",left);
	};
	window.reloadProviderIframe = function() {
		var iframe = PF('providerLightboxWV').iframe;
		iframe.attr('src',iframe.attr('src'));
	};
	$('body').on('click','.add-provider-menuitem',function() {
		PF('addProviderDialogVar').show();
	});
	$('body').on('click','.add-future-provider-menuitem',function() {
		PF('addFutureEmployeeDialogVar').show();
	});
	$('body').on('click','.remove-provider-menuitem',function(event) {
		event.stopPropagation();
		var tableRows = $('.revenue-worksheet-table .ui-datatable-scrollable-body tr');
		var selectableRows = $('.revenue-worksheet-table .ui-datatable-scrollable-body tr td span[data-removable=true]').closest('tr');
		
		function startRemoveProviderMode() {
			$('.revenue-worksheet-toolbar').hide();
			$('.remove-provider-message').fadeIn();
			tableRows.hide();
			selectableRows.show();
		}
		
		function stopRemoveProviderMode() {
			$('.remove-provider-message').fadeOut(function() {
				$('.revenue-worksheet-toolbar').show();
				tableRows.show();
			});
		}
		
		if (selectableRows.size() > 0) {
			startRemoveProviderMode();
			selectableRows.pickOne({
				selected: function() {
					var selectedId = $(this).find('[data-id]').attr('data-id');
					PF('removeProviderDialogVar').show();
					$('.remove-provider-btn').on('click.selectedProvider',function() {
						// Calling JSF to remove row.
						removeProviderRemoteCmd([{name: 'id', value: selectedId}]);
						PF('removeProviderDialogVar').hide();
						stopRemoveProviderMode();
					});
					$('.remove-provider-cancel-btn').on('click.selectedProvider',function() {
						PF('removeProviderDialogVar').hide();
						stopRemoveProviderMode();
					});
				},
				canceled: function() {
					stopRemoveProviderMode();
				}
			});
		} else {
			// Calling JSF to display growler message.
			noRemovableProvidersRemoteCmd();
		}
	});
	$('.main-container').on('click','[type=text]',function() {
		$(this).select();
	});
	$('.main-container').on('keypress','.enter-entry',function(event) {
		if (event.keyCode == 13) {
			var focusables = $(':focusable');
			var current = focusables.index(this),
            	next = focusables.eq(current+1).length ? focusables.eq(current+1) : focusables.eq(0);
            next.focus().select();
		}
	});
	var setActiveTab = function() {
		var hash = location.hash.substring(1);
		var pairs = hash.split('&');
		var nvps = {};
		for (var i = 0; i < pairs.length; i++) {
			var pair = pairs[i];
			var postSplit = pair.split('=');
			var name = postSplit[0];
			var value = postSplit[1];
			nvps[name] = value;
		}
		if (!nvps['activeTabIndex']) {
			nvps['activeTabIndex'] = 0;
		}
		if (nvps['activeTabIndex'] != $.tab.activeTabIndex) {
			PF('tabViewWV').select(nvps['activeTabIndex']);
		}
	};
	setActiveTab();
	$(window).on('hashchange',setActiveTab);
});