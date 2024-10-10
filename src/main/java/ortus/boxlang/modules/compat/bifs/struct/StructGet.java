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

import java.util.Arrays;
import java.util.List;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.ExpressionInterpreter;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

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
	@Override
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		Object result = super._invoke( context, arguments );
		if ( result == null ) {
			// Lucee and ACF will both mutate the original struct all of the way down to last segment
			String			path		= arguments.getAsString( Key.path );
			List<String>	segments	= Arrays.asList( path.split( "\\." ) );
			Object			root		= ExpressionInterpreter.getVariable( context, segments.get( 0 ), true );

			// If not a struct, or null then return a new struct
			if ( ! ( root instanceof IStruct ) ) {
				return new Struct();
			}

			// If only one segment, then return the root
			if ( segments.size() == 1 ) {
				return root;
			}

			// Now traverse from position 1 to the end
			IStruct	currentStruct	= ( IStruct ) root;
			String	fullPath		= segments.get( 0 );
			for ( int i = 1; i < segments.size(); i++ ) {
				String segment = segments.get( i );
				fullPath += "." + segment;
				System.out.println( "Checking: " + fullPath );
				if ( !currentStruct.containsKey( segment ) ) {
					ExpressionInterpreter.setVariable( context, fullPath, new Struct() );
				}
				currentStruct = ( IStruct ) currentStruct.get( segment );
			}
			return currentStruct;
		} else {
			return result;
		}
	}

}
