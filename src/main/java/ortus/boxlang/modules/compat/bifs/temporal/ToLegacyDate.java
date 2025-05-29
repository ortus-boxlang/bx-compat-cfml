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

import java.util.Date;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.DateTimeCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.DateTime;

@BoxBIF
@BoxMember( type = BoxLangType.DATETIME, name = "toLegacyDate" )

public class ToLegacyDate extends BIF {

	/**
	 * Constructor
	 */
	public ToLegacyDate() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.date )
		};
	}

	/**
	 * Takes a BoxLang DateTime object and converts it to the legacy java.util.Date object used by ACF and Lucee.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.date The date/time string to convert.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		DateTime dateRef = DateTimeCaster.cast( arguments.get( Key.date ) );

		return Date.from( dateRef.toInstant() );

	}

}
