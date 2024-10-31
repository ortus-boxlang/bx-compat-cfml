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
package ortus.boxlang.modules.compat.bifs.system;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BIFDescriptor;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.Array;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.exceptions.BoxValidationException;

@BoxBIF
public class GetFunctionData extends BIF {

	/**
	 * Constructor
	 */
	public GetFunctionData() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, KeyDictionary.functionName )
		};
	}

	/**
	 * Lucee compat: Return information to of a Function as Struct
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.functionName The BIF function you want information for (Ex: listLen)
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		// Namespace is ignored, they never completed the other ones in lucee
		String			functionName	= arguments.getAsString( KeyDictionary.functionName );
		BIFDescriptor	descriptor		= functionService.getGlobalFunction( functionName );

		if ( descriptor == null ) {
			// Throw Validation Exception
			throw new BoxValidationException( "Function not found: " + functionName );
		}

		return Struct.of(
		    "name", functionName,
		    "status", "implemented",
		    "description", "",
		    "keywords", new Array(),
		    "returnType", "Any",
		    "argumentType", "fixed",
		    "argMin", 0,
		    "argMax", -1,
		    "type", "java",
		    "memeber", Struct.of(),
		    "arguments", descriptor.getArguments()
		);
	}

}
