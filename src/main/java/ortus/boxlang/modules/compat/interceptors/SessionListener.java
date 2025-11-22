/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.modules.compat.interceptors;

import ortus.boxlang.runtime.application.Session;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.SessionScope;
import ortus.boxlang.runtime.types.IStruct;

/**
 * Listens to when sessions get created to manipulate them for CFML compatibility
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.0.0
 */
public class SessionListener extends BaseInterceptor {

	/**
	 * The URL token format
	 */
	public static final String URL_TOKEN_FORMAT = "CFID=%s&CFTOKEN=%s";

	/**
	 * Intercept BIF Invocation
	 *
	 * @param interceptData The struct containing the context and arguments of the BIF Invocation
	 */
	@InterceptionPoint
	public void onSessionCreated( IStruct interceptData ) {
		// Get the session from the interceptData
		Session			userSession		= ( Session ) interceptData.get( Key.session );
		SessionScope	sessionScope	= userSession.getSessionScope();
		Key				sessionID		= userSession.getID();

		// This is 0 for compatibility with CFML and for security.
		sessionScope.put( Key.cftoken, 0 );
		// This is the session ID for compatibility with CFML
		sessionScope.put( Key.cfid, sessionID.getName() );

		// URL Token Compat
		sessionScope.put( Key.urlToken,
		    String.format( URL_TOKEN_FORMAT, sessionScope.getAsString( Key.cfid ), StringCaster.cast( sessionScope.get( Key.cftoken ) ) ) );
	}

}
