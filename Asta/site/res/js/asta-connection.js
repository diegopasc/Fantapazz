
			/**
			 * Connection to asta server.
			 * This class allows to send messages and receive them
			 */
			function AstaConnection(namespace, opts) {
	
				// Web Socket connection			
				this.lWSC = null;

				/**
				 * Send message
				 */						
				this.sendMsg = function(clazz, msg) {
					// Set source field
					msg.source = clientID;
					var m = {
						type: clazz,
						object: msg
					};
					var lToken = {
						ns: namespace,
						type: "asta",
						value: JSON.stringify(m)
					};
					lToken.port = serverPort;
					this.lWSC.sendToken( lToken );
				}
				
				/**
				 * Connect to asta server with a given client ID
				 */
				this.disconnect = function() {
				
					if( jws.browserSupportsWebSockets() ) {

						this.lWSC.close();
					
					}
				
				}

				/**
				 * Connect to asta server with a given client ID
				 */
				this.connect = function() {
				
					if( jws.browserSupportsWebSockets() ) {
					
						var lURL = jws.JWS_SERVER_URL + "/;prot=json,timeout=30000";
						
						this.lWSC = new jws.jWebSocketJSONClient();
						
						this.lWSC.logon( lURL, "", "", {
						
							OnOpen: opts.OnOpen,
							OnClose: opts.OnClose,
							
/*
							OnOpen: function( aEvent ) {
								astaConnection.sendHelloMsg(ID);
								astaConnection.lWSC.startKeepAlive({ immediate: true, interval: 15000, echo: true });
							},
							
							OnClose: function( aEvent ) {
								astaController.stopTimer();
							},
*/
							
							OnMessage: function( aEvent, aToken ) {
								if ( aToken.type == "asta" ) {
																
									if ( ! opts.receiveMsg ) {
										return;
									}

									if ( aToken.ns != namespace ) {
										return;
									}

									astaGUI.log("RX - TOKEN: " + JSON.stringify(aToken));

									var text = aToken.value;
									var obj = JSON.parse(text);
									opts.receiveMsg(obj.type, obj.object);
								
									// astaConnection.receiveMsg(aToken);
								}
								else {
									astaGUI.log("Message: " + aEvent.data + ", " + aToken);
								}
							}
							
						});

					}

				}
			
			}
