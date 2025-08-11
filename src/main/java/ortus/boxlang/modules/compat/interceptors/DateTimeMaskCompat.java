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
package ortus.boxlang.modules.compat.interceptors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;
import ortus.boxlang.runtime.types.IStruct;

/**
 * This interceptor is used to convert date/time masks to the Java date/time format.
 * It checks for the Lucee/ACF date/time format and converts it to the Java date/time format.
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.26.0
 */
public class DateTimeMaskCompat extends BaseInterceptor {

	public static final String			ON_LEGACY_DATE_FORMAT_REQUEST	= "onLegacyDateFormatRequest";

	private static final Key			FORMAT_EPOCH					= Key.of( "epoch" );
	private static final Key			FORMAT_EPOCHMS					= Key.of( "epochms" );
	private static final ArrayList<Key>	FORMAT_METHODS					= new ArrayList<>();
	static {
		FORMAT_METHODS.add( Key.of( "ParseDateTime" ) );
		FORMAT_METHODS.add( Key.of( "LSParseDateTime" ) );
		FORMAT_METHODS.add( Key.of( "DateTimeFormat" ) );
		FORMAT_METHODS.add( Key.of( "LSDateTimeFormat" ) );
		FORMAT_METHODS.add( Key.of( "DateFormat" ) );
		FORMAT_METHODS.add( Key.of( "LSDateFormat" ) );
		FORMAT_METHODS.add( Key.of( "TimeFormat" ) );
		FORMAT_METHODS.add( Key.of( "LSTimeFormat" ) );
	}

	private static final Map<String, String> DATE_MASK_REPLACEMENTS = new LinkedHashMap<>();
	static {
		DATE_MASK_REPLACEMENTS.put( "mmmm", "MMMM" );
		DATE_MASK_REPLACEMENTS.put( "mmm", "MMM" );
		DATE_MASK_REPLACEMENTS.put( "mm/", "MM/" );
		DATE_MASK_REPLACEMENTS.put( "/mm", "/MM" );
		DATE_MASK_REPLACEMENTS.put( "-mm", "-MM" );
		DATE_MASK_REPLACEMENTS.put( "n", "m" );
		DATE_MASK_REPLACEMENTS.put( "N", "n" );
		DATE_MASK_REPLACEMENTS.put( "dddd", "EEEE" );
		DATE_MASK_REPLACEMENTS.put( "ddd", "EEE" );
		DATE_MASK_REPLACEMENTS.put( "TT", "a" );
		DATE_MASK_REPLACEMENTS.put( "tt", "a" );
		DATE_MASK_REPLACEMENTS.put( "t", "a" );
		// this handles a potential incorrect replacement on AM/PM indicators
		DATE_MASK_REPLACEMENTS.put( "HH:mm a", "hh:mm a" );
		DATE_MASK_REPLACEMENTS.put( ":MM", ":mm" );
		// Lucee/ACF seconds mask handling
		DATE_MASK_REPLACEMENTS.put( ":SS", ":ss" );
		// Lucee/ACF awful milliseconds handling
		DATE_MASK_REPLACEMENTS.put( "l", "S" );
		// We need to keep this weird uppercase pattern match since `L` is also a DateTimeFormatter mask for Month
		DATE_MASK_REPLACEMENTS.put( ".LLL", ".SSS" );
		DATE_MASK_REPLACEMENTS.put( ".LL", ".SS" );
		DATE_MASK_REPLACEMENTS.put( ".L", ".S" );
		// A few common literal formats not caught by the above
		DATE_MASK_REPLACEMENTS.put( "yyyymmdd", "yyyyMMdd" );
	}

	private static final Map<String, String> LITERAL_MASK_REPLACEMENTS = new LinkedHashMap<>();
	static {
		LITERAL_MASK_REPLACEMENTS.put( "m", "M" );
		LITERAL_MASK_REPLACEMENTS.put( "mm", "MM" );
		LITERAL_MASK_REPLACEMENTS.put( "mmm", "MMM" );
		LITERAL_MASK_REPLACEMENTS.put( "mmmm", "MMMM" );
		LITERAL_MASK_REPLACEMENTS.put( "n", "m" );
		LITERAL_MASK_REPLACEMENTS.put( "nn", "mm" );
	}

	/**
	 * Intercept BIF Invocation
	 *
	 * @param interceptData The struct containing the context and arguments of the BIF Invocation
	 */
	@InterceptionPoint
	public void onLegacyDateFormatRequest( IStruct interceptData ) {
		IStruct	arguments		= interceptData.getAsStruct( Key.arguments );
		Key		bifMethodKey	= arguments.getAsKey( BIF.__functionName );
		if ( !FORMAT_METHODS.contains( bifMethodKey ) )
			return;

		Key		formatArgument	= Key.mask;
		String	format			= arguments.getAsString( formatArgument );
		if ( format == null ) {
			formatArgument	= Key.format;
			format			= arguments.getAsString( formatArgument );
		}

		final Key finalArg = formatArgument;

		if ( format == null )
			return;

		Key		formatKey		= Key.of( format );
		String	mode			= bifMethodKey.equals( Key.dateFormat )
		    ? DateTime.MODE_DATE
		    : bifMethodKey.equals( Key.timeFormat )
		        ? DateTime.MODE_TIME
		        : DateTime.MODE_DATETIME;

		Key		commonFormatKey	= Key.of( format.trim() + mode );

		if ( !formatKey.equals( FORMAT_EPOCH ) && !formatKey.equals( FORMAT_EPOCHMS ) && !DateTime.COMMON_FORMATTERS.containsKey( commonFormatKey ) ) {

			if ( LITERAL_MASK_REPLACEMENTS.containsKey( format ) ) {
				arguments.put( finalArg, LITERAL_MASK_REPLACEMENTS.get( format ) );
			} else {
				DATE_MASK_REPLACEMENTS.entrySet().stream().forEach( entry -> {
					arguments.put( finalArg, arguments.getAsString( finalArg ).replaceAll( entry.getKey(), entry.getValue() ) );
				} );
			}

		}

	}

}
