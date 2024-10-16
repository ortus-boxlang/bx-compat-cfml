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
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

/**
 * Listens to when sessions get created to manipulate them for CFML compatibility
 */
public class SessionListener extends BaseInterceptor {

	/**
	 * Intercept BIF Invocation
	 *
	 * @param interceptData The struct containing the context and arguments of the BIF Invocation
	 */
	@InterceptionPoint
	public void onSessionCreated( IStruct interceptData ) {
		// Get the session from the interceptData
		Session userSession = ( Session ) interceptData.get( Key.session );

		// This is 0 for compatibility with CFML and for security.
		userSession.getSessionScope().put( Key.cftoken, 0 );

	}

}
