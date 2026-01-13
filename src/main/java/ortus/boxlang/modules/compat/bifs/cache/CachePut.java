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
import ortus.boxlang.runtime.dynamic.casters.DoubleCaster;
import ortus.boxlang.runtime.dynamic.casters.LongCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;
import ortus.boxlang.runtime.validation.Validator;

@BoxBIF
public class CachePut extends BIF {

	private static final Validator	cacheExistsValidator	= new CacheExistsValidator();

	private static final int		SECONDS_IN_A_DAY		= 86400;

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
		ICacheProvider	cache				= context.getApplicationCache( arguments.getAsKey( Key.cacheName ).getName() );
		String			id					= arguments.getAsString( Key.id );
		Object			value				= arguments.get( Key.value );
		Duration		timeout				= null;
		Duration		lastAccessTimeout	= null;

		if ( arguments.get( Key.timespan ) != null && arguments.get( Key.timespan ) instanceof Number ) {
			Double timeoutDays = DoubleCaster.cast( arguments.get( Key.timespan ) );
			timeout = Duration.ofSeconds( LongCaster.cast( timeoutDays * SECONDS_IN_A_DAY ) );
		} else if ( arguments.get( Key.timespan ) != null && arguments.get( Key.timespan ) instanceof Duration tsDuration ) {
			timeout = tsDuration;
		} else if ( arguments.get( Key.timespan ) != null ) {
			throw new BoxRuntimeException(
			    "Invalid timespan argument.  The timespan must be a valid decimal number of days or a duration created by the createTimeSpan method" );
		}

		if ( arguments.get( Key.idleTime ) != null && arguments.get( Key.idleTime ) instanceof Number ) {
			Double idleDays = DoubleCaster.cast( arguments.get( Key.timespan ) );
			lastAccessTimeout = Duration.ofSeconds( LongCaster.cast( idleDays * SECONDS_IN_A_DAY ) );
		} else if ( arguments.get( Key.idleTime ) != null && arguments.get( Key.idleTime ) instanceof Duration idleDuration ) {
			lastAccessTimeout = idleDuration;
		} else if ( arguments.get( Key.idleTime ) != null ) {
			throw new BoxRuntimeException(
			    "Invalid idleTime argument.  The idleTime must be a valid decimal number of days or a duration created by the createTimeSpan method" );
		}

		if ( timeout != null && lastAccessTimeout != null ) {
			cache.set( id, value, timeout, lastAccessTimeout );
			return true;
		} else if ( timeout != null ) {
			cache.set( id, value, timeout );
			return true;
		} else {
			cache.set( id, value );
		}

		return true;
	}
}
