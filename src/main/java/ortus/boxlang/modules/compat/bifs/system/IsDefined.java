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

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.VariableNameCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.STRING )
public class IsDefined extends ortus.boxlang.runtime.bifs.global.decision.IsDefined {

	/**
	 * Checks if a variable is defined. For CFML compatibility, this handles literal dot-notation variable names
	 * (e.g., "foo.bar" as a literal variable name, not property access).
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.name The name of the variable to check. Can be a variable name, scoped name (e.g., "local.x", "arguments.y"),
	 *                or literal variable name with dots (e.g., "foo.bar" as a literal variable name).
	 *
	 * @return true if the variable is defined and not null, false otherwise
	 */
	@Override
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		Object	result		= super._invoke( context, arguments );
		boolean	isDefined	= ( Boolean ) result;

		// If already defined, return true
		if ( isDefined ) {
			return true;
		}

		// Second pass for literal dot-notation names like "foo.bar"
		String name = arguments.getAsString( Key.variable );

		// Check if name contains at least one period
		if ( !name.contains( "." ) ) {
			return false;
		}

		// Validate using VariableNameCaster (handles proper variable name rules with dots)
		if ( VariableNameCaster.attempt( name ).isEmpty() ) {
			return false;
		}

		// Try to find the literal variable name in the nearest scope
		Object value = context.scopeFindNearby( Key.of( name ), context.getDefaultAssignmentScope(), false ).value();
		return value != null;
	}

}
