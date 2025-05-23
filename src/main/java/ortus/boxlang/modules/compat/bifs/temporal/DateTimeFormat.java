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
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.Struct;

@BoxBIF
@BoxBIF( alias = "DateFormat" )
@BoxBIF( alias = "TimeFormat" )
@BoxMember( type = BoxLangType.DATETIME, name = "format" )
@BoxMember( type = BoxLangType.DATETIME, name = "dateFormat" )
@BoxMember( type = BoxLangType.DATETIME, name = "timeFormat" )
@BoxMember( type = BoxLangType.DATETIME, name = "dateTimeFormat" )
@BoxMember( type = BoxLangType.STRING_STRICT, name = "dateFormat" )
@BoxMember( type = BoxLangType.STRING_STRICT, name = "timeFormat" )
@BoxMember( type = BoxLangType.STRING_STRICT, name = "dateTimeFormat" )
public class DateTimeFormat extends ortus.boxlang.runtime.bifs.global.temporal.DateTimeFormat {

	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		// Lucee and ACF will accept a empty string and return an empty string...
		Object formattable = arguments.get( Key.date );
		if ( formattable instanceof String && StringCaster.cast( formattable ).trim().isEmpty() ) {
			return formattable;
		}
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
