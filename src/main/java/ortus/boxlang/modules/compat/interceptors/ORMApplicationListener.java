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

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.context.RequestBoxContext;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.types.IStruct;

/**
 * Listens to when an ORM app starts up and tweaks the ORM configuration for CFML compatibility
 */
public class ORMApplicationListener extends BaseInterceptor {

	/**
	 * Intercept ORM config load to tweak the ORM configuration for CFML compatibility
	 *
	 * @param interceptData The struct containing the context and arguments of the BIF Invocation
	 */
	@InterceptionPoint
	public void ORMPreConfigLoad( IStruct interceptData ) {
		IStruct				properties	= ( IStruct ) interceptData.get( "properties" );
		RequestBoxContext	context		= ( RequestBoxContext ) interceptData.get( "context" );

		// backwards compatibility for `skipCFCWithError`
		properties.computeIfAbsent(
		    KeyDictionary.ignoreParseErrors,
		    key -> {
			    // Handles 'skipCFCWithError' true-by-default setting for backwards compatibility
			    return BooleanCaster.cast( properties.getOrDefault( KeyDictionary.skipCFCWithError, true ) );
		    }
		);

		// Alias the old `cfclocation` to the new `entityPaths` setting name
		properties.computeIfAbsent(
		    KeyDictionary.entityPaths,
		    key -> {
			    if ( properties.containsKey( KeyDictionary.cfclocation ) ) {
				    return properties.get( KeyDictionary.cfclocation );
			    }
			    return null;
		    }
		);
		properties.computeIfAbsent(
		    KeyDictionary.autoManageSession,
		    key -> {
			    // Handles 'autoManageSession' true-by-default setting for backwards compatibility
			    return BooleanCaster.cast( properties.getOrDefault( KeyDictionary.autoManageSession, true ) );
		    }
		);
		properties.computeIfAbsent(
		    KeyDictionary.flushAtRequestEnd,
		    key -> {
			    // Handles 'flushAtRequestEnd' true-by-default setting for backwards compatibility
			    return BooleanCaster.cast( properties.getOrDefault( KeyDictionary.flushAtRequestEnd, true ) );
		    }
		);

		// in case of nulls, set defaults
		properties.putIfAbsent( KeyDictionary.ignoreParseErrors, true );
		properties.putIfAbsent( KeyDictionary.autoManageSession, true );
		properties.putIfAbsent( KeyDictionary.flushAtRequestEnd, true );
	}
}
