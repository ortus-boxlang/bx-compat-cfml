/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.modules.compat.runtime.context;

import java.io.Serializable;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.application.Application;
import ortus.boxlang.runtime.application.BaseApplicationListener;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

/**
 * I represent a Client. This will be stored in a BoxLang cache
 * and will be used to store session data.
 */
public class Client implements Serializable {

	/**
	 * The concatenator for client IDs
	 * We leverage the `:` as it's a standard in many distributed caches like Redis and Couchbase to denote namespaces
	 */
	public static final String	ID_CONCATENATOR		= ":";

	/**
	 * The URL token format
	 * MOVE TO COMPAT MODULE
	 */
	public static final String	URL_TOKEN_FORMAT	= "CFID=%s";

	/**
	 * --------------------------------------------------------------------------
	 * Private Properties
	 * --------------------------------------------------------------------------
	 */

	/**
	 * The unique ID of this session
	 */
	private Key					ID;

	/**
	 * The scope for this client
	 */
	private ClientScope			clientScope;

	/**
	 * Flag for when client has been started
	 */
	private boolean				isNew				= true;

	/**
	 * The application name linked to
	 */
	private Key					applicationName		= null;

	/**
	 * --------------------------------------------------------------------------
	 * Constructor(s)
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Constructor
	 *
	 * @param ID          The ID of this client
	 * @param application The application that this client belongs to
	 */
	public Client( Key ID, Application application ) {
		this.ID					= ID;
		this.applicationName	= application.getName();
		this.clientScope		= new ClientScope();
		DateTime timeNow = new DateTime();

		// Initialize the client scope
		this.clientScope.put( Key.cfid, ID.getName() );
		// This is 0 for compatibility with CFML and for security.
		this.clientScope.put( Key.cftoken, 0 );
		// URL Token Compat
		String bxid = application.getName() + Client.ID_CONCATENATOR + ID.getName();
		this.clientScope.put( Key.urlToken, String.format( Client.URL_TOKEN_FORMAT, bxid ) );
		this.clientScope.put( KeyDictionary.hitCount, 0 );
		this.clientScope.put( Key.lastVisit, timeNow );
		this.clientScope.put( Key.timeCreated, timeNow );

		// Announce it's creation
		BoxRuntime.getInstance()
		    .getInterceptorService()
		    .announce( KeyDictionary.ON_CLIENT_CREATED, Struct.of(
		        Key.session, this
		    ) );
	}

	/**
	 * --------------------------------------------------------------------------
	 * Static Helper Methods
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Build a cache key for a client
	 *
	 * @param id              The ID of the client
	 * @param applicationName The application name
	 *
	 * @return The cache key
	 */
	public static String buildCacheKey( Key id, Key applicationName ) {
		return applicationName + ID_CONCATENATOR + id;
	}

	/**
	 * --------------------------------------------------------------------------
	 * Client Methods
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Update the last visit time
	 */
	public void updateLastVisit() {
		this.clientScope.put( Key.lastVisit, new DateTime() );
		this.clientScope.put( KeyDictionary.hitCount, this.clientScope.getOrDefault( KeyDictionary.hitCount, 0 ) );
	}

	/**
	 * Start the client if not already started. If it is already started, just update the last visit time
	 *
	 * @param context The context that is starting the client
	 *
	 * @return This client
	 */
	public Client start( IBoxContext context ) {
		// If the client has started, just return it and update it's last visit time
		if ( !isNew ) {
			updateLastVisit();
			return this;
		}
		synchronized ( this ) {
			// Double check lock
			if ( !isNew ) {
				return this;
			}

			BoxRuntime.getInstance()
			    .getInterceptorService()
			    .announce( KeyDictionary.ON_CLIENT_CREATED, Struct.of(
			        KeyDictionary.client, this
			    ) );

			isNew = true;
		}
		return this;
	}

	/**
	 * Get the ID of this client
	 *
	 * @return The ID
	 */
	public Key getID() {
		return this.ID;
	}

	/**
	 * Get the scope for this client
	 *
	 * @return The scope
	 */
	public ClientScope getClientScope() {
		return this.clientScope;
	}

	/**
	 * Get the application that this client belongs to
	 *
	 * @return The application name
	 */
	public Key getApplicationName() {
		return this.applicationName;
	}

	/**
	 * Get the cache key for this client
	 */
	public String getCacheKey() {
		return buildCacheKey( this.ID, this.applicationName );
	}

	/**
	 * Shutdown the client
	 *
	 * @param listener The listener that is shutting down the session
	 */
	public void shutdown( BaseApplicationListener listener ) {
		// Announce it's destruction
		BoxRuntime.getInstance()
		    .getInterceptorService()
		    .announce( KeyDictionary.ON_CLIENT_DESTROYED, Struct.of(
		        KeyDictionary.client, this
		    ) );

		// Clear the session scope
		if ( this.clientScope != null ) {
			this.clientScope.clear();
		}
		this.clientScope = null;
	}

	/**
	 * Convert to string
	 */
	@Override
	public String toString() {
		return "Client{" +
		    "ID=" + ID +
		    ", clientScope=" + clientScope +
		    ", isNew=" + isNew +
		    ", applicationName=" + applicationName +
		    '}';
	}

	/**
	 * Get the session state as a struct representation
	 */
	public IStruct asStruct() {
		return Struct.of(
		    Key.id, this.ID,
		    Key.scope, this.clientScope,
		    "isNew", isNew,
		    Key.applicationName, this.applicationName
		);
	}

}