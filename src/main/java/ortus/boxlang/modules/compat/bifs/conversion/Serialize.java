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
package ortus.boxlang.modules.compat.bifs.conversion;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

@BoxBIF
public class Serialize extends BIF {

	private static final Key jsonSerializeKey = Key.of( "JSONSerialize" );

	/**
	 * Constructor
	 */
	public Serialize() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.ANY, Key.object ),
		    new Argument( false, Argument.STRING, Key.type, "json" ),
		    new Argument( false, Argument.BOOLEAN, Key.useCustomSerializer, false )
		};
	}

	/**
	 * Serializes an object to a specific type.
	 *
	 * Minimal compat behavior:
	 * - Only JSON is supported
	 * - Custom serializers are not supported
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.object The object to serialize.
	 *
	 * @argument.type The target format. Only "json" is supported.
	 *
	 * @argument.useCustomSerializer Whether to use custom serializers. Not supported.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		if ( arguments.getAsBoolean( Key.useCustomSerializer ) ) {
			throw new BoxRuntimeException( "Custom serializers are not yet supported." );
		}

		String type = arguments.getAsString( Key.type ).toLowerCase();
		if ( !type.equals( "json" ) ) {
			if ( type.equals( "xml" ) ) {
				throw new BoxRuntimeException( "XML serialization is not yet supported." );
			}
			throw new BoxRuntimeException( "Unsupported serialize type [" + type + "]. Only JSON is supported." );
		}

		return context.invokeFunction( jsonSerializeKey, new Object[] { arguments.get( Key.object ) } );
	}
}
