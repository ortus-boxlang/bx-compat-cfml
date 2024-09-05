package ortus.boxlang.modules.compat.bifs.temporal;

import java.util.LinkedHashMap;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.DateTime;

@BoxBIF
@BoxBIF( alias = "DateFormat" )
@BoxBIF( alias = "TimeFormat" )
@BoxMember( type = BoxLangType.DATETIME, name = "format" )
@BoxMember( type = BoxLangType.DATETIME, name = "dateFormat" )
@BoxMember( type = BoxLangType.DATETIME, name = "timeFormat" )
@BoxMember( type = BoxLangType.DATETIME, name = "dateTimeFormat" )
public class DateTimeFormat extends ortus.boxlang.runtime.bifs.global.temporal.DateTimeFormat {

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
																						// Lucee/ACF miliseconds handling
																						put( ".l", ".SSS" );
																						put( ".L", "S" );
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
	 * Formats a datetime, date or time
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.date The date string or object
	 *
	 * @argument.mask Optional format mask, or common mask
	 *
	 * @argument.timezone Optional specific timezone to apply to the date ( if not present in the date string )
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {

		String format = arguments.getAsString( Key.mask );
		if ( format != null ) {
			Key		bifMethodKey	= arguments.getAsKey( BIF.__functionName );
			Key		formatKey		= Key.of( format );
			String	mode			= bifMethodKey.equals( Key.dateFormat )
			    ? DateTime.MODE_DATE
			    : bifMethodKey.equals( Key.timeFormat )
			        ? DateTime.MODE_TIME
			        : DateTime.MODE_DATETIME;
			// Create this key instance here so it doesn't get created twice on lookup and retrieval
			Key		commonFormatKey	= Key.of( format.trim() + mode );

			if ( !formatKey.equals( FORMAT_EPOCH ) && !formatKey.equals( FORMAT_EPOCHMS ) && !DateTime.COMMON_FORMATTERS.containsKey( commonFormatKey ) ) {

				if ( literalMaskReplacements.containsKey( format ) ) {
					arguments.put( Key.mask, literalMaskReplacements.get( format ) );
				} else {
					dateMaskReplacements.entrySet().stream().forEach( entry -> {
						arguments.put( Key.mask, arguments.getAsString( Key.mask ).replaceAll( entry.getKey(), entry.getValue() ) );
					} );
				}

				System.out.println( "Mask value: " + arguments.getAsString( Key.mask ) );

			}

		}

		return super._invoke( context, arguments );
	}

}
