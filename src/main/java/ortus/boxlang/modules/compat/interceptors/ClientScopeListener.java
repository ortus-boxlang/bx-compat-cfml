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

import java.time.Duration;

import ortus.boxlang.modules.compat.runtime.context.Client;
import ortus.boxlang.modules.compat.runtime.context.ClientBoxContext;
import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.application.Application;
import ortus.boxlang.runtime.application.BaseApplicationListener;
import ortus.boxlang.runtime.cache.providers.ICacheProvider;
import ortus.boxlang.runtime.context.ApplicationBoxContext;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.RequestBoxContext;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.dynamic.casters.LongCaster;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.services.CacheService;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

/**
 * Listens to when clients get created to manipulate them for CFML
 * compatibility
 */
public class ClientScopeListener extends BaseInterceptor {

	protected CacheService		cacheService			= BoxRuntime.getInstance().getCacheService();

	/**
	 * The clients for this application
	 */
	private ICacheProvider		clientCache;

	private static final Key	DEFAULT_CLIENT_CACHEKEY	= KeyDictionary.bxClients;
	private static final String	CLIENT_STORAGE_MEMORY	= "cookie";

	/**
	 * Intercept BIF Invocation
	 *
	 * @param interceptData The struct containing the context and arguments of the
	 *                      BIF Invocation
	 */
	@InterceptionPoint
	public void onApplicationStart( IStruct interceptData ) {

		// Check for existing client context
		BaseApplicationListener	listener				= ( BaseApplicationListener ) interceptData.get( Key.listener );
		RequestBoxContext		context					= listener.getRequestContext();
		ClientBoxContext		existingClientContext	= context.getParentOfType( ClientBoxContext.class );
		IStruct					settings				= listener.getSettings();
		boolean					clientManagementEnabled	= BooleanCaster.cast( settings.getOrDefault( Key.clientManagement, false ) );

		createClientCache( settings );

		// Create client management if enabled
		if ( existingClientContext == null ) {
			// if client management is enabled, add it
			if ( clientManagementEnabled ) {
				initializeClientScope( context, settings );
			}
		}
		// Update client management if enabled
		else {
			if ( clientManagementEnabled ) {
				ApplicationBoxContext appContext = context.getParentOfType( ApplicationBoxContext.class );
				// Ensure we have the right session (app name could have changed)
				existingClientContext
				    .updateClient( getOrCreateClient( context.getSessionID(), appContext.getApplication(), settings ) );
				// Only starts the first time
				existingClientContext.getClient().start( context );
			} else {
				// If client management is disabled, remove it
				context.removeParentContext( ClientBoxContext.class );
			}
		}
	}

	/**
	 * Intercept BIF Invocation
	 *
	 * @param interceptData The struct containing the context and arguments of the
	 *                      BIF Invocation
	 */
	@InterceptionPoint
	public void onRequestStart( IStruct interceptData ) {
		IBoxContext			context					= ( IBoxContext ) interceptData.get( Key.context );
		ClientBoxContext	existingClientContext	= context.getParentOfType( ClientBoxContext.class );
		if ( existingClientContext != null ) {
			existingClientContext.getClient().updateLastVisit();
		}
	}

	/**
	 * Initializes a new client scope
	 *
	 * @param context The RequestBoxContext
	 */
	public void initializeClientScope( RequestBoxContext context, IStruct settings ) {
		Key						newID			= context.getSessionID();
		ApplicationBoxContext	appContext		= context.getParentOfType( ApplicationBoxContext.class );
		Client					targetClient	= getOrCreateClient( newID, appContext.getApplication(), settings );
		context.removeParentContext( ClientBoxContext.class );
		context.injectTopParentContext( new ClientBoxContext( targetClient ) );
		targetClient.start( context );
	}

	public Client getOrCreateClient( Key ID, Application application, IStruct settings ) {
		Duration	timeoutDuration	= null;
		Object		clientTimeout	= settings.get( KeyDictionary.clientTimeout );

		// Duration is the default, but if not, we will use the number as seconds
		// Which is what the cache providers expect
		if ( clientTimeout instanceof Duration castedTimeout ) {
			timeoutDuration = castedTimeout;
		} else if ( clientTimeout == null ) {
			timeoutDuration = Duration.ofSeconds( 60 );
		} else {
			timeoutDuration = Duration.ofSeconds( LongCaster.cast( clientTimeout ) );
		}

		// Get or create the session
		return ( Client ) this.clientCache.getOrSet(
		    Client.buildCacheKey( ID, application.getName() ),
		    () -> new Client( ID, application ),
		    timeoutDuration,
		    timeoutDuration
		);
	}

	private void createClientCache( IStruct settings ) {
		String	clientStorageName	= StringCaster
		    .attempt( settings.get( KeyDictionary.clientStorage ) )
		    // If present, make sure it has a value or default it
		    .map( ( String setting ) -> setting.trim().isEmpty() ? CLIENT_STORAGE_MEMORY : setting.trim() )
		    // Return the right value or the default name
		    .getOrDefault( CLIENT_STORAGE_MEMORY );
		// @formatter:on

		// Now we can get the right cache name to use
		Key		clientCacheName		= clientStorageName.equals( CLIENT_STORAGE_MEMORY )
		    ? DEFAULT_CLIENT_CACHEKEY
		    : Key.of( clientStorageName );

		// If the cache doesn't exist by now, then throw an exception
		if ( !cacheService.hasCache( clientCacheName ) ) {
			throw new BoxRuntimeException(
			    "Client storage cache not defined in the cache services or config [" + clientCacheName + "]" +
			        "Defined cache names are : " + this.cacheService.getRegisteredCaches()
			);
		}

		// Now store it
		this.clientCache = this.cacheService.getCache( clientCacheName );
	}

}
