(function($) {
	$.fn.sessionTimeout = function(settings) {
		var config = $.fn.extend({},$.fn.sessionTimeout.defaults,settings);
		var warning, timeout, timer, lastResetTime;
        function now() {
            return (new Date()).getTime();
        }
        function timeLeft() {
            return lastResetTime + config.timeout - now();
        }
        function timeLeftInSeconds() {
        	return Math.ceil(timeLeft() / 1000.0);
        }
        function setTimeouts() {
			warning = setTimeout(function() {
				config.onWarning();
                if (config.timer && config.onTimer !== $.noop) {
                    config.onTimer(timeLeftInSeconds());
                    timer = setInterval(function() {
                        config.onTimer(timeLeftInSeconds());
                    },config.timerTick);
                }
			},config.timeout - config.warning);
			timeout = setTimeout(function() {
				config.onTimeout();
                clearInterval(timer);
			},config.timeout);
		}
        function setClocks() {
            clearTimeout(warning);
			clearTimeout(timeout);
            clearInterval(timer);
			setTimeouts();
            lastResetTime = now();
        }
		$.fn.sessionTimeout.reset = function() {
            setClocks();
            config.onReset();
		};
        setClocks();
	};
	$.fn.sessionTimeout.defaults = {
		timeout: 30 * 60 * 1000,
		warning: 5 * 60 * 1000,
        timer: true,
        timerTick: 1 * 1000,
		onWarning: $.noop,
		onTimeout: $.noop,
        onTimer: $.noop,
        onReset: $.noop
	};
})(jQuery);