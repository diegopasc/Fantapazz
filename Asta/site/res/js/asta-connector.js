
	function FantapazzConnector() {
	
		this.debug = false;
		
		this.site = "http://www.beta.fantapazz.com";
		
		this.token = "69222d343f634e55796a54687d553c786f637e7a5a55757d6f72293328";

		// List of calciatori (key: ID calciatore)
		this.calciatori = new Object();
		
		// List of Aste (key: ID asta)
		this.asta = new Object();

		// List of squadre (key: ID squadra)
		this.squadre = new Object();

		/**
		 * Load data related to asta
		 */		
		this.load = function() {
		
			$.ajaxSetup({
				accepts: 'application/json'
			});
		
			this.loadCalciatori();
		
		}
		
		/**
		 * Get asta by ID
		 */
		this.getAsta = function(astaID) {
			var command = "/servizi/fantacalcio/asta/getAsta/" + astaID;
			var ret = null;
			this.makeCall(command, {
  				method: 'get',
  				async: false,
  				error: function(jqXHR, textStatus, errorThrown) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  					alert("getAsta ERROR");
  				},
	  			success: function(data, textStatus, jqXHR) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  				}
			});
  			return ret;
		}
		
		this.getSquadra = function(squadraID) {
		
			var squadra = this.squadre[squadraID];
			if ( squadra != null ) {
				return squadra;
			}
			
			var command = "/servizi/fantacalcio/asta/getSquadra?playerID=" + squadraID;
			
			var ret = null;
			
			this.makeCall(command, {
  				method: 'get',
  				async: false,
				// dataType: "json",
  				error: function(jqXHR, textStatus, errorThrown) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  					alert("getSquadra ERROR: " + textStatus + ", " + errorThrown + ", " + jqXHR + ", " + text);
  				},
	  			success: function(data, textStatus, jqXHR) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  				}
			});

			this.squadre[squadraID] = ret;

  			return ret;
		}

		// SquadraComm
		this.getSquadraForAstaAndPlayer = function(astaInfo, playerID) {
		
			for ( i = 0; i < astaInfo.ID_fSquadre.length; i ++ ) {
				var squadraID = astaInfo.ID_fSquadre[i];
				var squadra = this.getSquadra(squadraID);
								
				if ( squadra.uidPresidente == playerID )
					return squadra;
				if ( $.inArray(playerID, squadra.uidAllenatori ) )
					return squadra;
			}
			
			return null;
		
		}
		
		this.getUser = function(uid) {
			var command = "/servizi/fantacalcio/asta/get_user?uid=" + uid;
			var ret = null;
			this.makeCall(command, {
  				method: 'get',
  				async: false,
  				error: function(jqXHR, textStatus, errorThrown) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  					alert("getUser ERROR");
  				},
	  			success: function(data, textStatus, jqXHR) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  				}
			});
  			return ret;
		}

		this.getUserByPassword = function(username, password) {
		
			var command = "/servizi/fantacalcio/asta/login/?user=" + username + "&pass=" + password;
			var ret = null;
															
			this.makeCall(command, {
			
  				method: 'get',
  				async: false,
  				
  				error: function(jqXHR, textStatus, errorThrown) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  					alert("getUserByPassword ERROR");
  				},
  				
	  			success: function(data, textStatus, jqXHR) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  				}
  				
			});
			
			// alert(JSON.stringify(ret));
			
  			return ret;
		}
		
		this.getAsteForUser = function(uid) {
			var command = "/servizi/fantacalcio/asta/getAsteForUser?uid=" + uid;
			var ret = null;
			this.makeCall(command, {
  				method: 'get',
  				async: false,
  				error: function(jqXHR, textStatus, errorThrown) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  					alert("getAsteForUser ERROR");
  				},
	  			success: function(data, textStatus, jqXHR) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  				}
			}, true);
  			return ret;
  		}

		/**
		 * Fantapazz connection
		 */
		this.loadCalciatori = function() {
			var command = "/servizi/fantacalcio/asta/lista_calciatori";
			var here = this;
			this.makeCall(command, {
  				method: 'get',
  				async: false,
  				error: function(jqXHR, textStatus, errorThrown) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  					alert("getAsteForUser ERROR: " + textStatus + ", " + errorThrown + ", " + jqXHR);
  				},

	  			success: function(data, textStatus, jqXHR) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					var list = JSON.parse(text);
  					for ( i = 0; i < list.length; i ++ ) {
  						here.calciatori[list[i].ID_Calciatore] = list[i];
  					}
  				}
			}, true);
		}
		
		this.loadAsta = function(astaID) {
			var command = "/servizi/fantacalcio/asta/getAsta/" + astaID;
			makeCall(command, {
  				method: 'get',
  				
  				error: function(jqXHR, textStatus, errorThrown) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
  					ret = JSON.parse(text);
  					alert("loadAsta ERROR: " + textStatus + ", " + errorThrown + ", " + jqXHR);
  				},

	  			success: function(data, textStatus, jqXHR) {
  					var text = jqXHR.responseText.replace(/\\\'/g, "\'");
					var astaInfo = JSON.parse(text);
					
/*
					astaInfo.ID_Asta
					astaInfo.ID_Lega
					astaInfo.ID_fSquadre
					astaInfo.ID_Calciatori
					astaInfo.startDate
					astaInfo.Status
*/
					
					asta[astaInfo.ID_Asta] = astaInfo;
					
  				}
			});
		
		}
		
		/**
		 * Fantapazz connection
		 */
		this.makeCall = function(command, opts, dump) {

			var random = Math.random();
			
			var finalURL = this.site + "/" + command + "&token=" + this.token + "&random=" + random;
			
			if ( opts.debug_url && this.debug ) {
				finalURL = opts.debug_url;
			}
									
			var proxyURL = "proxy.php?mode=native&url=" + escape(finalURL);
			
			// alert(proxyURL);
			
			opts["url"] = proxyURL;
			
			if ( dump != null ) {
				alert(proxyURL);
			}
			
			$.ajax(opts);
			
		}

	}