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

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF( description = "Delete an item at a specific position in a list" )
@BoxMember( type = BoxLangType.STRING_STRICT, name = "listDeleteAt" )
public class ListDeleteAt extends ortus.boxlang.runtime.bifs.global.list.ListDeleteAt {

	/**
	 * Constructor
	 */
	public ListDeleteAt() {
		super();
	}

	/**
	 * Deletes an element from a list. Returns a copy of the list, without the
	 * specified element.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.list The list to delete from.
	 *
	 * @argument.position The one-based index position of the element to delete.
	 *
	 * @argument.delimiter The delimiter used in the list.
	 *
	 * @argument.includeEmptyFields Whether to include empty fields in the list.
	 *
	 * @argument.multiCharacterDelimiter Whether the delimiter is a multi-character
	 *                                   delimiter.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		var includeEmptyFields = arguments.getAsBoolean( Key.includeEmptyFields );

		// Adobe/Lucee have an oddball behavior where leading delimiters are preserved even when
		// includeEmptyFields is false and only for this BIF
		if ( !includeEmptyFields ) {
			String	prefix			= "";
			var		list			= arguments.getAsString( Key.list );
			var		delimiter		= arguments.getAsString( Key.delimiter );
			var		delimiterChars	= delimiter.chars().mapToObj( c -> ( char ) c ).toList();
			// Strip off all leading chars in the list which are in the delimiter and place them in the prefix
			int		i				= 0;
			while ( i < list.length() && delimiterChars.contains( list.charAt( i ) ) ) {
				prefix += list.charAt( i );
				i++;
			}
			return prefix + ( String ) super._invoke( context, arguments );
		}
		return super._invoke( context, arguments );
	}

}
