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
import ortus.boxlang.runtime.config.segments.CacheConfig;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

@BoxBIF
public class CacheRegionNew extends BIF {

	/**
	 * Constructor
	 */
	public CacheRegionNew() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, Key.region ),
		    new Argument( true, Argument.STRUCT, Key.properties ),
		    new Argument( false, Argument.BOOLEAN, Key.throwOnError, false )
		};
	}

	/**
	 * Checks if the cache region exists.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.region The cache region to check for existence.
	 *
	 * @return True if the cache region exists, false otherwise.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		Key		cacheName		= Key.of( arguments.getAsString( Key.region ) );
		IStruct	properties		= arguments.getAsStruct( Key.properties );
		Boolean	throwOnError	= arguments.getAsBoolean( Key.throwOnError );

		if ( !cacheService.hasCache( cacheName ) ) {
			cacheService.createDefaultCache( cacheName, new CacheConfig( cacheName ).process( properties ) );
			return true;
		}

		if ( throwOnError ) {
			throw new BoxRuntimeException( "The cache region you want to create already exists." );
		}

		return false;
	}
}
