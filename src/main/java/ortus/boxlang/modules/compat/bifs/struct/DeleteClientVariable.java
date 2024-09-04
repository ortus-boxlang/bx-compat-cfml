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

import java.util.Set;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.validation.Validator;

@BoxBIF
public class DeleteClientVariable extends BIF {

	/**
	 * Name of client scope
	 */
	private static final Key clientKey = Key.of( "client" );

	/**
	 * Constructor
	 */
	public DeleteClientVariable() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, Key._NAME, Set.of( Validator.NON_EMPTY ) )
		};
	}

	/**
	 * Deletes a client variable. Returns true if variable was successfully deleted; false if it was not deleted.
	 * 
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.name The name of the variable to delete.
	 *
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		Key		name		= Key.of( arguments.getAsString( Key._NAME ) );

		IScope	clientScope	= context.getScope( clientKey );
		boolean	existed		= clientScope.containsKey( name );
		clientScope.remove( name );
		return existed;
	}

}
