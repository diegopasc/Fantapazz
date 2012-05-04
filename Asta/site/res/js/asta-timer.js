		/**
		 * Small OO Timer library
		 */
		function MyTimer(opt) {
		
			this.timer = null;
			this.updater = null;
			this.startTime = null;
			this.timerDuration = null;
			this.aOptions = opt;
			this.updateTime = opt.updateTime != null ? opt.updateTime : 10;
			
			this.getOpts = function() {
				return this.aOptions;
			}
						
			/**
			 * Stop timer
			 */
			this.stopTimer = function() {
				if ( this.timer != null ) {
					clearTimeout(this.timer);
					clearTimeout(this.updater);
					this.timer = null;
					this.updater = null;
					this.startTime = null;
				}
			}
		
			/**
			 * Start timer
			 */
			this.startTimer = function(millis) {
				if ( this.timer == null ) {
					this.timer = setTimeout(TimerTimeout, millis, this);
					this.updater = setTimeout(TimerUpdateTimer, this.updateTime, this);
					this.startTime = new Date().getTime();
					this.timerDuration = millis;
				}
			}
			
		}

		function TimerTimeout(timer) {
			timer.stopTimer();
			if ( timer.getOpts().OnUpdateTime ) {
				timer.getOpts().OnUpdateTime(0);
			}
			if ( timer.aOptions.OnTimeout ) {
				timer.aOptions.OnTimeout();
			}
		}
			
		function TimerUpdateTimer(aTimer) {
			var now = new Date().getTime();
			if ( aTimer.timer != null ) {
				aTimer.updater = setTimeout(TimerUpdateTimer, aTimer.updateTime, aTimer);
				var diff = (aTimer.timerDuration - (now - aTimer.startTime)) / 1000.0;
				if ( aTimer.aOptions.OnUpdateTime ) {
					aTimer.aOptions.OnUpdateTime(diff);
				}
			}
			else {
				aTimer.updater = null;
				if ( aTimer.getOpts().OnUpdateTime ) {
					aTimer.getOpts().OnUpdateTime(0);
				}
			}
		}
			