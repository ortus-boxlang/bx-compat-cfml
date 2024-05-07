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
package ortus.boxlang.modules.compat.bifs.cache;

import java.time.Duration;
import java.util.Set;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.cache.providers.ICacheProvider;
import ortus.boxlang.runtime.cache.util.CacheExistsValidator;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.validation.Validator;

@BoxBIF
public class CachePut extends BIF {

	private static final Validator cacheExistsValidator = new CacheExistsValidator();

	/**
	 * Constructor
	 */
	public CachePut() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.ANY, Key.id ),
		    new Argument( true, Argument.ANY, Key.value ),
		    new Argument( false, Argument.ANY, Key.timespan ),
		    new Argument( false, Argument.ANY, Key.idleTime ),
		    new Argument( false, Argument.STRING, Key.cacheName, Key._DEFAULT, Set.of( cacheExistsValidator ) )
		};
	}

	/**
	 * Get an item from the cache. If the item is not found, the default value will be returned if provided, else null will be returned.
	 * By default, the {@code cacheName} is set to {@code default}.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.id The cache id to store
	 *
	 * @argument.value The value to store in the cache
	 *
	 * @argument.timespan The duration for the cache to expire in seconds
	 *
	 * @argument.idleTime The duration for the cache to expire after last access in seconds
	 *
	 * @argument.cacheName The cache name to retrieve the id from, defaults to {@code default}
	 *
	 * @return True if the value was successfully stored in the cache, false otherwise.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		// Get the requested cache
		ICacheProvider	cache				= cacheService.getCache( arguments.getAsKey( Key.cacheName ) );
		String			id					= arguments.getAsString( Key.id );
		Object			value				= arguments.get( Key.value );
		Object			timeout				= arguments.get( Key.timespan );
		Object			lastAccessTimeout	= arguments.get( Key.idleTime );

		if ( timeout != null && lastAccessTimeout != null ) {
			cache.set( id, value, ( Duration ) timeout, ( Duration ) lastAccessTimeout );
			return true;
		}

		if ( timeout != null ) {
			cache.set( id, value, ( Duration ) timeout );
			return true;
		}

		cache.set( id, value );
		return true;
	}
}
