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

import ortus.boxlang.modules.compat.runtime.context.ClientBoxContext;
import ortus.boxlang.runtime.application.BaseApplicationListener;
import ortus.boxlang.runtime.application.Session;
import ortus.boxlang.runtime.context.ApplicationBoxContext;
import ortus.boxlang.runtime.context.RequestBoxContext;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

/**
 * Listens to when sessions get created to manipulate them for CFML
 * compatibility
 */
public class ClientScopeListener extends BaseInterceptor {

	/**
	 * Intercept BIF Invocation
	 *
	 * @param interceptData The struct containing the context and arguments of the
	 *                      BIF Invocation
	 */
	@InterceptionPoint
	public void afterApplicationListenerLoad( IStruct interceptData ) {
		// Check for existing client context
		RequestBoxContext		context					= ( RequestBoxContext ) interceptData.get( Key.context );
		ClientBoxContext		existingClientContext	= context.getParentOfType( ClientBoxContext.class );
		BaseApplicationListener	listener				= ( BaseApplicationListener ) interceptData.get( Key.listener );
		IStruct					settings				= listener.getSettings();
		boolean					clientManagementEnabled	= BooleanCaster.cast( settings.get( Key.clientManagement ) );

		// Create client management if enabled
		if ( existingClientContext == null ) {
			// if client management is enabled, add it
			if ( clientManagementEnabled ) {
				initializeClientScope( context );
			}
		}
		// Update client management if enabled
		else {
			if ( clientManagementEnabled ) {
				ApplicationBoxContext appContext = context.getParentOfType( ApplicationBoxContext.class );
				// Ensure we have the right session (app name could have changed)
				existingClientContext
				    .updateSession( appContext.getApplication().getOrCreateSession( context.getSessionID() ) );
				// Only starts the first time
				existingClientContext.getSession().start( context );
			} else {
				// If client management is disabled, remove it
				context.removeParentContext( ClientBoxContext.class );
			}
		}
	}

	/**
	 * Initializes a new client scope
	 *
	 * @param context The RequestBoxContext
	 */
	public void initializeClientScope( RequestBoxContext context ) {
		Key						newID			= context.getSessionID();
		ApplicationBoxContext	appContext		= context.getParentOfType( ApplicationBoxContext.class );
		Session					targetSession	= appContext.getApplication().getOrCreateSession( newID );
		context.removeParentContext( ClientBoxContext.class );
		context.injectTopParentContext( new ClientBoxContext( targetSession ) );
		targetSession.start( context );
	}

}
