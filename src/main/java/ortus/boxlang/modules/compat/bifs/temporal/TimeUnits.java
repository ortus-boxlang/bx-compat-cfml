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
package ortus.boxlang.modules.compat.bifs.temporal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.CastAttempt;
import ortus.boxlang.runtime.dynamic.casters.IntegerCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.util.LocalizationUtil;

@BoxBIF( alias = "DayOfWeekAsString" )
@BoxBIF( alias = "DayOfWeekShortAsString" )
@BoxBIF( alias = "MonthAsString" )
@BoxBIF( alias = "MonthShortAsString" )
public class TimeUnits extends ortus.boxlang.runtime.bifs.global.temporal.TimeUnits {

	/**
	 * Overload to `asString` datetime BIFS to support the ACF/Lucee specific `dayOfWeekAsString`, `dayOfWeekAsShortString`, `monthAsString`, and
	 * `monthAsShortString` BIFS.
	 *
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.date The numeric integer to extract the day of week or month from.
	 *
	 * @function.DayOfWeekAsString Returns the full day of the week name of a date object. Note that the behavior of this function is not localized, as
	 *                             Sunday is always considered to be the first day of the week.
	 *
	 * @function.DayOfWeekShortAsString Returns the short day of the week name of a date object. Note that the behavior of this function is not localized,
	 *                                  as Sunday is always considered to be the first day of the week.
	 *
	 * @function.MonthAsString Returns the full month name of a date object
	 *
	 * @function.MonthShortAsString Returns the short month name of a date object
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		Object					dateValue	= arguments.get( Key.date );
		CastAttempt<Integer>	castAttempt	= IntegerCaster.attempt( dateValue );
		if ( castAttempt.wasSuccessful() && castAttempt.get() != null ) {
			Integer	date			= castAttempt.get();
			Key		bifMethodKey	= arguments.getAsKey( BIF.__functionName );
			Integer	month			= 1;
			// This is the first Saturday of the epoch year - which we can use for addition on dates
			// The legacy behavior of both Lucee and ACF is that Sunday is always considered to be the start of the week.
			Integer	day				= 3;
			if ( bifMethodKey.equals( BIFMethods.monthAsString ) || bifMethodKey.equals( BIFMethods.monthShortAsString ) ) {
				month = date;
			} else if ( bifMethodKey.equals( BIFMethods.dayOfWeekAsString ) || bifMethodKey.equals( BIFMethods.dayOfWeekShortAsString ) ) {
				day += date;
			}
			LocalDate	dateRef	= LocalDate.of( 1970, month, day );
			String		mask	= bifMethodKey.equals( BIFMethods.monthAsString ) ? MONTH_LONG_FORMAT
			    : bifMethodKey.equals( BIFMethods.monthShortAsString ) ? MONTH_SHORT_FORMAT
			        : bifMethodKey.equals( BIFMethods.dayOfWeekAsString ) ? DOW_LONG_FORMAT
			            : bifMethodKey.equals( BIFMethods.dayOfWeekShortAsString ) ? DOW_SHORT_FORMAT
			                : null;
			return DateTimeFormatter.ofPattern( mask )
			    .localizedBy( LocalizationUtil.COMMON_LOCALES.get( Key.of( "US" ) ) )
			    .withLocale( LocalizationUtil.parseLocaleFromContext( context, arguments ) )
			    .format( dateRef );
		}

		return super._invoke( context, arguments );
	}

}
