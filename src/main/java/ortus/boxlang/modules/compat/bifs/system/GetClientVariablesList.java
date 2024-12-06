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

import java.util.Set;

import ortus.boxlang.modules.compat.runtime.context.ClientBoxContext;
import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.util.BLCollector;
import ortus.boxlang.runtime.types.util.ListUtil;

@BoxBIF
public class GetClientVariablesList extends BIF {

	private static final Set<Key> systemProvidedVariables = Set.of( Key.cfid, Key.cftoken, Key.urlToken, KeyDictionary.hitCount, Key.timeCreated,
	    Key.lastVisit );

	/**
	 * Constructor
	 */
	public GetClientVariablesList() {
		super();
		declaredArguments = new Argument[] {};
	}

	/**
	 * Returns the metadata of a component path or instance in CFML style
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.path The path to the component.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		ClientBoxContext existingClientContext = context.getParentOfType( ClientBoxContext.class );
		if ( existingClientContext == null ) {
			return "";
		}
		return ListUtil.asString(
		    existingClientContext
		        .getClient()
		        .getClientScope()
		        .keySet()
		        .stream()
		        .filter( key -> !systemProvidedVariables.contains( key ) )
		        .collect( BLCollector.toArray() ),
		    ","
		);

	}
}
