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
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.util.ListUtil;

@BoxBIF( alias = "StructGet" )
public class StructGet extends ortus.boxlang.runtime.bifs.global.struct.StructGet {

	/**
	 * Retrieves the value from a struct using a path based expression
	 *
	 * Allows the legacy behavior of creating the path if it does not exist
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.path The string path to the object requested in the struct
	 *
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		Object result = super._invoke( context, arguments );
		if ( result == null ) {
			// Lucee and ACF will both mutate the original struct all of the way down to last segment
			IStruct	ref		= arguments.getAsStruct( Key.object );
			String	path	= arguments.getAsString( Key.path );
			for ( Object segment : ListUtil.asList( path, "." ) ) {
				Key segmentKey = Key.of( segment );
				if ( !ref.containsKey( segmentKey ) ) {
					ref.put( segmentKey, new Struct() );
					ref = ref.getAsStruct( segmentKey );
				}
			}
			return ref;
		} else {
			return result;
		}
	}

}
