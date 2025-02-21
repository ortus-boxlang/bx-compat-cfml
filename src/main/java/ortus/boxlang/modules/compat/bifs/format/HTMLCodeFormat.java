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

package ortus.boxlang.modules.compat.bifs.format;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.NUMERIC )
public class HTMLCodeFormat extends BIF {

	public static final String	PRE_OPEN	= "<pre>\n";
	public static final String	PRE_CLOSE	= "\n</pre>";

	/**
	 * Constructor
	 */
	public HTMLCodeFormat() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "string", Key.string ),
		    new Argument( false, "string", Key.version )
		};
	}

	/**
	 * Formats a number as a U.S. Dollar string with two decimal places, thousands separator, and a dollar sign.
	 * If the number is negative, the return value is enclosed in parentheses.
	 * If the number is an empty string, the function returns "0.00".
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.string The number to format as a U.S. Dollar string.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		if ( arguments.get( Key.version ) != null ) {
			logger.warn( "The [version] argument to HTMLCodeFormat is not implemented and will be ignored" );
		}
		return PRE_OPEN + arguments.get( Key.string ) + PRE_CLOSE;
	}

}
