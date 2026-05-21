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
package ortus.boxlang.modules.compat.bifs.list;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF( description = "Add qualifiers around each item in a list" )
@BoxMember( type = BoxLangType.STRING_STRICT, name = "ListQualify" )
public class ListQualify extends ortus.boxlang.runtime.bifs.global.list.ListQualify {

	/**
	 * Constructor
	 */
	public ListQualify() {
		super();
	}

	/**
	 * Inserts a string at the beginning and end of list elements.
	 * 
	 * If this BIF is being called from inside of a query component,
	 * and the qualifier is a single quote, any single quotes in the values will be escaped by doubling them up.
	 * This protects against SQL Injection attacks.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.list The list to qualify.
	 *
	 * @argument.qualifier The string to insert at the beginning and end of each element.
	 *
	 * @argument.delimiter The delimiter used in the list.
	 *
	 * @argument.elements The elements to qualify. If set to "char", only elements that are all alphabetic characters will be qualified.
	 *
	 * @argument.includeEmptyFields If true, empty fields will be qualified.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		String	qualifier			= arguments.getAsString( Key.qualifier );
		String	list				= arguments.getAsString( Key.list );
		boolean	includeEmptyFields	= arguments.getAsBoolean( Key.includeEmptyFields );

		// Adobe-specific workaround, where an empty string qualifies to ""
		// Lucee just returns an empty string in this case as it considers the empty string input list
		// to be a list with zero elements, not a list with a single empty element.
		if ( includeEmptyFields && "".equals( list ) && isAdobe() ) {
			return qualifier + qualifier;
		}

		return super._invoke( context, arguments );
	}

	/**
	 * Determines if the current environment is Adobe ColdFusion.
	 * 
	 * @return True if the environment is Adobe ColdFusion, false otherwise.
	 */
	private boolean isAdobe() {
		return BooleanCaster.cast( moduleService.getModuleSettings( KeyDictionary.moduleName ).getAsBoolean( KeyDictionary.isAdobe ) );
	}

}
