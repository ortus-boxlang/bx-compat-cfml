package ortus.boxlang.modules.compat.interceptors;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;
import ortus.boxlang.runtime.types.IStruct;

public class DateTimeMaskCompat extends BaseInterceptor {

	private static final ArrayList<Key>					formatMethods			= new ArrayList<Key>() {

																					{
																						add( Key.of( "ParseDateTime" ) );
																						add( Key.of( "LSParseDateTime" ) );
																						add( Key.of( "DateTimeFormat" ) );
																						add( Key.of( "DateFormat" ) );
																						add( Key.of( "TimeFormat" ) );
																					}
																				};

	private static final Key							FORMAT_EPOCH			= Key.of( "epoch" );
	private static final Key							FORMAT_EPOCHMS			= Key.of( "epochms" );

	public static final LinkedHashMap<String, String>	dateMaskReplacements	= new LinkedHashMap<String, String>() {

																					{
																						put( "h", "H" );
																						put( "mm/", "MM/" );
																						put( "/mm", "/MM" );
																						put( "-mm", "-MM" );
																						put( "mmm", "MMM" );
																						put( "mmmm", "MMMM" );
																						put( "n", "m" );
																						put( "N", "n" );
																						put( "dddd", "EEEE" );
																						put( "ddd", "EEE" );
																						put( "TT", "a" );
																						put( "tt", "a" );
																						put( ":MM", ":mm" );
																						// Lucee/ACF seconds mask handling
																						put( ":SS", ":ss" );
																						// Lucee/ACF awful miliseconds handling
																						put( ".lll", ".SSS" );
																						put( ".ll", ".SS" );
																						put( ".l", ".S" );
																						put( ".LLL", ".SSS" );
																						put( ".LL", ".SS" );
																						put( ".L", ".S" );
																						// A few common literal formats not caught by the above
																						put( "yyyymmdd", "yyyyMMdd" );

																					}
																				};

	public static final LinkedHashMap<String, String>	literalMaskReplacements	= new LinkedHashMap<String, String>() {

																					{
																						put( "m", "M" );
																						put( "mm", "MM" );
																						put( "mmm", "MMM" );
																						put( "mmmm", "MMMM" );
																						put( "n", "m" );
																						put( "nn", "mm" );
																					}
																				};

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
