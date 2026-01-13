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

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.Array;

@BoxBIF
public class CacheGetProperties extends BIF {

	/**
	 * Constructor
	 */
	public CacheGetProperties() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( false, Argument.STRING, Key.cacheName, "" )
		};
	}

	/**
	 * Returns some properties of the cache engine
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @return A struct containing the properties of the cache engine
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		String cacheName = arguments.getAsString( Key.cacheName );

		// If we have a cache name, get the results of the cache
		if ( !cacheName.isEmpty() ) {
			return context.getApplicationCache( Key.of( cacheName ).getName() )
			    .getConfig()
			    .toStruct();
		}

		// Otherwise return an array of configs
		return cacheService.getRegisteredCaches()
		    .stream()
		    .map( cache -> cacheService.getCache( Key.of( cache ) ) )
		    .map( cache -> cache.getConfig().toStruct() )
		    // Collect to my Array implementation
		    .collect( Array::new, Array::add, Array::addAll );
	}
}
