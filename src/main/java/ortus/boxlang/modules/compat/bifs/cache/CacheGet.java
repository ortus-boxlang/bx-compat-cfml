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

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.cache.providers.ICacheProvider;
import ortus.boxlang.runtime.cache.util.CacheExistsValidator;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.Attempt;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.Array;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;
import ortus.boxlang.runtime.validation.Validator;

@BoxBIF
public class CacheGet extends BIF {

	private static final Validator cacheExistsValidator = new CacheExistsValidator();

	/**
	 * Constructor
	 */
	public CacheGet() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.ANY, Key.id ),
		    new Argument( false, Argument.ANY, Key.cacheName, Key._DEFAULT ),
		    new Argument( false, Argument.ANY, KeyDictionary.throwWhenNotExist, false )
		};
	}

	/**
	 * Get an item from the cache. If the item is not found, the default value will be returned if provided, else null will be returned.
	 * By default, the {@code cacheName} is set to {@code default}.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.id The cache id to retrieve, or an array of ids to retrieve
	 *
	 * @argument.cacheName The cache name to retrieve the id from, defaults to {@code default}
	 *
	 * @return The value of the object in the cache or null if not found, or the default value if provided
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		if ( arguments.get( Key.cacheName ) instanceof Boolean ) {
			// Handle lucees method signature with a boolean as the second arg
			Key cacheName = Key._DEFAULT;
			if ( arguments.get( KeyDictionary.throwWhenNotExist ) != null ) {
				cacheName = Key.of( arguments.getAsString( KeyDictionary.throwWhenNotExist ) );
			}
			arguments.put( KeyDictionary.throwWhenNotExist, arguments.getAsBoolean( Key.cacheName ) );
			arguments.put( Key.cacheName, cacheName );
		}

		if ( arguments.get( Key.cacheName ) instanceof String ) {
			arguments.put( Key.cacheName, Key.of( arguments.getAsString( Key.cacheName ) ) );
		}

		// Get the requested cache
		ICacheProvider cache = cacheService.getCache( arguments.getAsKey( Key.cacheName ) );

		// Single or multiple ids
		if ( arguments.get( Key.id ) instanceof Array casteId ) {
			// Convert the BoxLang array to an array of Strings
			return cache.get( ( String[] ) casteId.stream().map( Object::toString ).toArray() );
		}

		// Get the value
		Attempt<Object>	results	= cache.get( arguments.getAsString( Key.id ) );
		// If we have a value return it, else do we have a defaultValue, else return null
		Object			item	= results.orElse( null );
		if ( item == null && arguments.getAsBoolean( KeyDictionary.throwWhenNotExist ) ) {
			throw new BoxRuntimeException(
			    "Item not found in cache [" + arguments.getAsKey( Key.cacheName ).getName() + "]: " + arguments.getAsString( Key.id )
			);
		} else {
			return item;
		}

	}
}
