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

public class DateTimeMaskCompat extends BaseInterceptor {

	private static final Key			FORMAT_EPOCH	= Key.of( "epoch" );
	private static final Key			FORMAT_EPOCHMS	= Key.of( "epochms" );
	private static final ArrayList<Key>	formatMethods	= new ArrayList<>();
	static {
		formatMethods.add( Key.of( "ParseDateTime" ) );
		formatMethods.add( Key.of( "LSParseDateTime" ) );
		formatMethods.add( Key.of( "DateTimeFormat" ) );
		formatMethods.add( Key.of( "DateFormat" ) );
		formatMethods.add( Key.of( "TimeFormat" ) );
	}

	private static final Map<String, String> dateMaskReplacements = new LinkedHashMap<>();
	static {
		dateMaskReplacements.put( "h", "H" );
		dateMaskReplacements.put( "mmmm", "MMMM" );
		dateMaskReplacements.put( "mmm", "MMM" );
		dateMaskReplacements.put( "mm/", "MM/" );
		dateMaskReplacements.put( "/mm", "/MM" );
		dateMaskReplacements.put( "-mm", "-MM" );
		dateMaskReplacements.put( "n", "m" );
		dateMaskReplacements.put( "N", "n" );
		dateMaskReplacements.put( "dddd", "EEEE" );
		dateMaskReplacements.put( "ddd", "EEE" );
		dateMaskReplacements.put( "TT", "a" );
		dateMaskReplacements.put( "tt", "a" );
		dateMaskReplacements.put( "t", "a" );
		dateMaskReplacements.put( ":MM", ":mm" );
		// Lucee/ACF seconds mask handling
		dateMaskReplacements.put( ":SS", ":ss" );
		// Lucee/ACF awful milliseconds handling
		dateMaskReplacements.put( ".lll", ".SSS" );
		dateMaskReplacements.put( ".ll", ".SS" );
		dateMaskReplacements.put( ".l", ".S" );
		dateMaskReplacements.put( ".LLL", ".SSS" );
		dateMaskReplacements.put( ".LL", ".SS" );
		dateMaskReplacements.put( ".L", ".S" );
		// A few common literal formats not caught by the above
		dateMaskReplacements.put( "yyyymmdd", "yyyyMMdd" );
	}

	private static final Map<String, String> literalMaskReplacements = new LinkedHashMap<>();
	static {
		literalMaskReplacements.put( "m", "M" );
		literalMaskReplacements.put( "mm", "MM" );
		literalMaskReplacements.put( "mmm", "MMM" );
		literalMaskReplacements.put( "mmmm", "MMMM" );
		literalMaskReplacements.put( "n", "m" );
		literalMaskReplacements.put( "nn", "mm" );
	}

	/**
	 * Intercept BIF Invocation
	 *
	 * @param interceptData The struct containing the context and arguments of the BIF Invocation
	 */
	@InterceptionPoint
	public void onBIFInvocation( IStruct interceptData ) {
		IStruct	arguments		= interceptData.getAsStruct( Key.arguments );
		Key		bifMethodKey	= arguments.getAsKey( BIF.__functionName );
		if ( !formatMethods.contains( bifMethodKey ) )
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

			if ( literalMaskReplacements.containsKey( format ) ) {
				arguments.put( finalArg, literalMaskReplacements.get( format ) );
			} else {
				dateMaskReplacements.entrySet().stream().forEach( entry -> {
					arguments.put( finalArg, arguments.getAsString( finalArg ).replaceAll( entry.getKey(), entry.getValue() ) );
				} );
			}

		}

	}

}
