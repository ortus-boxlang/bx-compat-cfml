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

package ortus.boxlang.modules.compat.bifs.temporal;

import ortus.boxlang.modules.compat.interceptors.DateTimeMaskCompat;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.Struct;

@BoxBIF
@BoxMember( type = BoxLangType.STRING, name = "LSParseDateTime" )
public class LSParseDateTime extends ortus.boxlang.runtime.bifs.global.temporal.ParseDateTime {

	/**
	 * Constructor
	 */
	public LSParseDateTime() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.date ),
		    new Argument( false, "string", Key.locale ),
		    new Argument( false, "string", Key.timezone ),
		    new Argument( false, "string", Key.format )
		};
	}

	/**
	 * Parses a locale-specific datetime string or object
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.date the date, datetime string or an object
	 *
	 * @argument.the ISO locale string ( e.g. en-US, en_US, es-SA, es_ES, ru-RU, etc )
	 *
	 * @argument.format the format mask to use in parsing
	 *
	 * @argument.timezone the timezone to apply to the parsed datetime
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		interceptorService.announce(
		    DateTimeMaskCompat.ON_LEGACY_DATE_FORMAT_REQUEST,
		    Struct.of(
		        Key.context, context,
		        Key.arguments, arguments
		    )
		);
		return super._invoke( context, arguments );
	}

}
