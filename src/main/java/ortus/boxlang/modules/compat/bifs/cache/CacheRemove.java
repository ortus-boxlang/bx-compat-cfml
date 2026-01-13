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

import java.util.Set;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.cache.providers.ICacheProvider;
import ortus.boxlang.runtime.cache.util.CacheExistsValidator;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.Array;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;
import ortus.boxlang.runtime.validation.Validator;

@BoxBIF
@BoxBIF( alias = "cacheDelete" )
public class CacheRemove extends BIF {

	private static final Validator cacheExistsValidator = new CacheExistsValidator();

	/**
	 * Constructor
	 */
	public CacheRemove() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.ANY, Key.id ),
		    new Argument( false, Argument.BOOLEAN, Key.throwOnError, false ),
		    new Argument( false, Argument.STRING, Key.cacheName, Key._DEFAULT, Set.of( cacheExistsValidator ) )
		};
	}

	/**
	 * Deletes a single element from the cache.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.id A single ID or an array of IDs to remove from the cache.
	 *
	 * @argument.throwOnError If true, throw an exception if the key is not found. Default is false.
	 *
	 * @argument.cacheName The name of the cache to get the keys from. Default is the default cache.
	 *
	 * @return Clears the key if found, else it throws an exception. If the key is an array,
	 *         it returns a Struct with the keys and their results.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		ICacheProvider	cache			= context.getApplicationCache( arguments.getAsKey( Key.cacheName ).getName() );
		Boolean			throwOnError	= arguments.getAsBoolean( Key.throwOnError );
		Boolean			results			= false;

		// One key
		if ( arguments.get( Key.id ) instanceof String castedKey ) {
			results = cache.clear( castedKey );
		}

		// Array of keys
		if ( arguments.get( Key.id ) instanceof Array castedArrayOfKeys ) {
			// Get the ids from the array to native java Array
			String[] keys = castedArrayOfKeys.toArray( String[]::new );
			return cache.clear( keys );
		}

		// Throw it
		if ( !results && throwOnError ) {
			throw new BoxRuntimeException(
			    "Cache id [" + arguments.get( Key.id ).toString() + "] not found in cache [" + arguments.getAsKey( Key.cacheName ) + "]"
			);
		}

		return null;
	}
}
