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

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.IntegerCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;

@BoxBIF( alias = "createDateTime" )
@BoxBIF( alias = "createDate" )
public class CreateDateTime extends ortus.boxlang.runtime.bifs.global.temporal.CreateDateTime {

	/**
	 * Overload to creatDateTime behavior to account for ACF/Lucee specific manipulations ( e.g. converting non-century years to current century )
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.year The year of the date-time object.
	 *
	 * @argument.month The month of the date-time object.
	 *
	 * @argument.day The day of the date-time object.
	 *
	 * @argument.hour The hour of the date-time object.
	 *
	 * @argument.minute The minute of the date-time object.
	 *
	 * @argument.second The second of the date-time object.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		if ( !arguments.isEmpty() && !arguments.getAsInteger( Key.year ).equals( 0 ) && arguments.getAsInteger( Key.year ) < 100 ) {
			DateTime	currentDate			= new DateTime();
			Integer		currentYear			= new DateTime().getWrapped().getYear();
			Integer		currentMillenium	= IntegerCaster.cast( currentDate.format( "YYYY" ) ) - IntegerCaster.cast( currentDate.format( "YY" ) );
			Boolean		inCurrentMillenium	= ( arguments.getAsInteger( Key.year ) + currentMillenium ) <= currentYear;
			if ( inCurrentMillenium ) {
				arguments.put( Key.year, arguments.getAsInteger( Key.year ) + currentMillenium );
			} else {
				arguments.put( Key.year, arguments.getAsInteger( Key.year ) + ( currentMillenium - 100 ) );
			}
		}
		return super._invoke( context, arguments );
	}
}
