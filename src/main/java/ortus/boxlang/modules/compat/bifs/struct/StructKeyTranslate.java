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
package ortus.boxlang.modules.compat.bifs.struct;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

@BoxBIF( alias = "StructKeyTranslate" )
public class StructKeyTranslate extends ortus.boxlang.runtime.bifs.global.struct.StructKeyTranslate {

	/**
	 * Converts a struct with dot-notated keys in to an unflattened version
	 * Enables the legacy behavior of returning numeric depth of the translation
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.struct The struct to unflatten
	 *
	 * @argument.deep Whether to recurse in to nested keys - default false
	 *
	 * @argument.retainKeys Whether to retain the original dot-notated keys - default false
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		IStruct	original	= arguments.getAsStruct( Key.struct );
		Integer	depth		= 1;
		Boolean	deep		= arguments.getAsBoolean( Key.deep );
		if ( deep ) {
			depth = getTranslationDepth( original );
		}
		Object result = super._invoke( context, arguments );
		return result == null
		    ? depth
		    : result;
	}

	/**
	 * Recurses the provided struct to determine the depth of a new translation
	 *
	 * @param struct
	 * 
	 * @return the integer depth of the translation
	 */
	private Integer getTranslationDepth( IStruct struct ) {
		Integer depth = 1;
		return struct.keySet().stream()
		    .filter( key -> key.getName().contains( "." ) && struct.get( key ) instanceof IStruct )
		    .reduce(
		        depth,
		        ( currentDepth, key ) -> Math.max( currentDepth, getTranslationDepth( struct.getAsStruct( key ) ) ) + 1,
		        Math::max
		    );
	}

}
