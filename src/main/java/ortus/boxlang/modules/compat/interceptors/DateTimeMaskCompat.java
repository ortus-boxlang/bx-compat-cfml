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

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import ortus.boxlang.modules.compat.util.SettingsUtil;
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

	/**
	 * Sentinel character used to mark positions in a rewritten format string where a
	 * single Lucee/ACF {@code T} or {@code t} AM/PM abbreviation token appeared.
	 * Java's {@code DateTimeFormatter} has no single-character AM/PM pattern letter,
	 * so occurrences of this sentinel must be resolved via {@link #buildFormatter(String)},
	 * which uses {@code DateTimeFormatterBuilder.appendText} with a custom {@code A}/{@code P}
	 * text map in place of the sentinel.
	 */
	public static final String			AMPM_ABBR_SENTINEL				= "\u00B6";

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
		FORMAT_METHODS.add( Key.of( "Format" ) );
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

	private static final Map<String, String> DATE_FORMAT_REPLACEMENTS = new LinkedHashMap<>();
	static {
		DATE_FORMAT_REPLACEMENTS.put( "nn", "'nn'" );
		DATE_FORMAT_REPLACEMENTS.put( "H", "h" );
		DATE_FORMAT_REPLACEMENTS.put( "hh", "'hh'" );
		DATE_FORMAT_REPLACEMENTS.put( "ss", "'ss'" );
		DATE_FORMAT_REPLACEMENTS.put( "TT", "tt" );
		DATE_FORMAT_REPLACEMENTS.put( "tt", "'tt'" );
		DATE_FORMAT_REPLACEMENTS.put( "l", "'l'" );
		DATE_FORMAT_REPLACEMENTS.put( "Y", "y" );
		DATE_FORMAT_REPLACEMENTS.put( "m", "M" );
		// Long form text dates
		DATE_FORMAT_REPLACEMENTS.put( "dddd", "EEEE" );
		DATE_FORMAT_REPLACEMENTS.put( "ddd", "EEE" );
	}

	private static final Map<String, String> TIME_FORMAT_REPLACEMENTS = new LinkedHashMap<>();
	static {
		TIME_FORMAT_REPLACEMENTS.put( "M", "m" );
		TIME_FORMAT_REPLACEMENTS.put( "n", "m" );
		TIME_FORMAT_REPLACEMENTS.put( "S", "s" );
		TIME_FORMAT_REPLACEMENTS.put( "l", "S" );
		TIME_FORMAT_REPLACEMENTS.put( "L", "S" );
		TIME_FORMAT_REPLACEMENTS.put( ".sss", ".'0'ss" );
		// AM/PM indicators
		TIME_FORMAT_REPLACEMENTS.put( "TT", "a" );
		TIME_FORMAT_REPLACEMENTS.put( "tt", "a" );
		TIME_FORMAT_REPLACEMENTS.put( "t", "a" );
	}

	private static final Map<String, String> LUCEE_DATE_FORMAT_REPLACEMENTS = new LinkedHashMap<>();
	static {
		LUCEE_DATE_FORMAT_REPLACEMENTS.put( "D", "d" );
	}

	/**
	 * The set of valid Java DateTimeFormatter pattern letters.
	 * Any letter character in a format string that is NOT in this set will cause
	 * an IllegalArgumentException when passed to DateTimeFormatter.ofPattern(), so
	 * we need to quote those letters as literals.
	 *
	 * Derived from the JDK DateTimeFormatter specification — invalid letters (those that
	 * throw when used alone) are: C, I, J, P, R, T, U, V, b, f, i, j, o, p, r, t
	 *
	 * @see java.time.format.DateTimeFormatter
	 */
	private static final Set<Character> VALID_PATTERN_LETTERS = new HashSet<>();
	static {
		// Era, Year, Week-based year, Cyclic year, Related year
		VALID_PATTERN_LETTERS.add( 'G' );
		VALID_PATTERN_LETTERS.add( 'y' );
		VALID_PATTERN_LETTERS.add( 'Y' );
		VALID_PATTERN_LETTERS.add( 'u' );
		VALID_PATTERN_LETTERS.add( 'g' );
		// Quarter
		VALID_PATTERN_LETTERS.add( 'Q' );
		VALID_PATTERN_LETTERS.add( 'q' );
		// Month (in year), Stand-alone month
		VALID_PATTERN_LETTERS.add( 'M' );
		VALID_PATTERN_LETTERS.add( 'L' );
		// Week (in year / in month)
		VALID_PATTERN_LETTERS.add( 'w' );
		VALID_PATTERN_LETTERS.add( 'W' );
		// Day of year / month / week-in-month
		VALID_PATTERN_LETTERS.add( 'D' );
		VALID_PATTERN_LETTERS.add( 'd' );
		VALID_PATTERN_LETTERS.add( 'F' );
		// Day of week
		VALID_PATTERN_LETTERS.add( 'E' );
		VALID_PATTERN_LETTERS.add( 'e' );
		VALID_PATTERN_LETTERS.add( 'c' );
		// AM/PM, Day period
		VALID_PATTERN_LETTERS.add( 'a' );
		VALID_PATTERN_LETTERS.add( 'B' );
		// Hour (1-12, 0-23, 1-24, 0-11)
		VALID_PATTERN_LETTERS.add( 'h' );
		VALID_PATTERN_LETTERS.add( 'H' );
		VALID_PATTERN_LETTERS.add( 'k' );
		VALID_PATTERN_LETTERS.add( 'K' );
		// Minute / Second / Fraction of second
		VALID_PATTERN_LETTERS.add( 'm' );
		VALID_PATTERN_LETTERS.add( 's' );
		VALID_PATTERN_LETTERS.add( 'S' );
		// Millisecond of day / Nano of second / Nano of day
		VALID_PATTERN_LETTERS.add( 'A' );
		VALID_PATTERN_LETTERS.add( 'n' );
		VALID_PATTERN_LETTERS.add( 'N' );
		// Time zone
		VALID_PATTERN_LETTERS.add( 'v' );
		VALID_PATTERN_LETTERS.add( 'z' );
		VALID_PATTERN_LETTERS.add( 'Z' );
		VALID_PATTERN_LETTERS.add( 'O' );
		VALID_PATTERN_LETTERS.add( 'X' );
		VALID_PATTERN_LETTERS.add( 'x' );
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

		// if it's a common pattern don't bother processing further
		if ( DateTime.COMMON_FORMATTERS.containsKey( commonFormatKey ) || formatKey.equals( FORMAT_EPOCH ) || formatKey.equals( FORMAT_EPOCHMS ) ) {
			return;
		}

		if ( LITERAL_MASK_REPLACEMENTS.containsKey( format ) && !mode.equals( DateTime.MODE_TIME ) ) {
			format = LITERAL_MASK_REPLACEMENTS.get( format );
			arguments.put( finalArg, format );
			return;
		}
		if ( mode.equals( DateTime.MODE_DATE ) ) {
			if ( SettingsUtil.isLucee() ) {
				for ( String key : LUCEE_DATE_FORMAT_REPLACEMENTS.keySet() ) {
					format = format.replaceAll( key, LUCEE_DATE_FORMAT_REPLACEMENTS.get( key ) );
				}
			}
			// for dateFormat compat, specifically, we replace all lowercase `m` characters with uppercase
			for ( String key : DATE_FORMAT_REPLACEMENTS.keySet() ) {
				format = format.replaceAll( key, DATE_FORMAT_REPLACEMENTS.get( key ) );
			}
		} else if ( mode.equals( DateTime.MODE_TIME ) ) {
			for ( String key : TIME_FORMAT_REPLACEMENTS.keySet() ) {
				format = format.replaceAll( key, TIME_FORMAT_REPLACEMENTS.get( key ) );
			}
		} else {
			if ( SettingsUtil.isLucee() ) {
				for ( String key : LUCEE_DATE_FORMAT_REPLACEMENTS.keySet() ) {
					format = format.replaceAll( key, LUCEE_DATE_FORMAT_REPLACEMENTS.get( key ) );
				}
			}
			if ( LITERAL_MASK_REPLACEMENTS.containsKey( format ) ) {
				format = LITERAL_MASK_REPLACEMENTS.get( format );
			} else {
				for ( String key : DATE_MASK_REPLACEMENTS.keySet() ) {
					format = format.replaceAll( key, DATE_MASK_REPLACEMENTS.get( key ) );
				}
			}
		}

		format = quoteInvalidPatternLetters( format );
		arguments.put( finalArg, format );

	}

	/**
	 * Scans a DateTimeFormatter pattern string and quotes any contiguous run of letters
	 * that contains at least one letter not recognised by Java's DateTimeFormatter.
	 *
	 * <p>
	 * The Java {@code DateTimeFormatter} treats every unbroken sequence of the <em>same</em>
	 * letter as a single formatting token (e.g. {@code yyyy}, {@code MM}, {@code HH}).
	 * Mixing different letters together in a word-like sequence can cause an
	 * {@link java.lang.IllegalArgumentException} when an unrecognised letter is encountered.
	 * </p>
	 *
	 * <p>
	 * This method scans each contiguous letter run (letters between non-letter characters)
	 * and applies the following rule:
	 * <ul>
	 * <li>If the run <em>starts</em> with an invalid letter the entire run is wrapped in
	 * single-quotes so that it renders as a literal string (e.g. {@code caribou} →
	 * {@code 'caribou'}, {@code CAPRICCIO} → {@code 'CAPRICCIO'}).</li>
	 * <li>If the run starts with a valid letter, each consecutive sequence of the
	 * <em>same</em> letter (the natural {@code DateTimeFormatter} token unit) is
	 * inspected individually: valid same-char runs are emitted as-is; invalid
	 * same-char runs are quoted (e.g. {@code GMT} → {@code GM'T'}, producing
	 * era + month + literal&nbsp;T).</li>
	 * </ul>
	 * Already-quoted sections ({@code '...'}) are passed through verbatim.
	 * Non-letter characters (digits, spaces, punctuation) are never modified.
	 * </p>
	 *
	 * @param format the raw format string that may contain mixed or invalid letter runs
	 *
	 * @return the format string with offending letter runs wrapped as quoted literals
	 */
	static String quoteInvalidPatternLetters( String format ) {
		if ( format == null || format.isEmpty() ) {
			return format;
		}

		StringBuilder	result	= new StringBuilder( format.length() + 8 );
		int				len		= format.length();
		int				i		= 0;

		while ( i < len ) {
			char c = format.charAt( i );

			// Pass through existing quoted literals unchanged
			if ( c == '\'' ) {
				result.append( c );
				i++;
				while ( i < len ) {
					char inner = format.charAt( i );
					result.append( inner );
					i++;
					if ( inner == '\'' ) {
						break;
					}
				}
				continue;
			}

			if ( Character.isLetter( c ) ) {
				// Collect the full contiguous letter run
				int	wordStart	= i;
				int	wordEnd		= i;
				while ( wordEnd < len && Character.isLetter( format.charAt( wordEnd ) ) ) {
					wordEnd++;
				}

				if ( !VALID_PATTERN_LETTERS.contains( c ) ) {
					// Run starts with an invalid letter.
					// Special case: a single T or t is the Lucee/ACF AM/PM first-letter
					// indicator — emit the sentinel so buildFormatter() can handle it.
					if ( ( c == 'T' || c == 't' ) && ( wordEnd - wordStart ) == 1 ) {
						result.append( AMPM_ABBR_SENTINEL );
					} else {
						// Quote the entire run as a literal
						result.append( '\'' ).append( format, wordStart, wordEnd ).append( '\'' );
					}
				} else {
					// Run starts with a valid letter — process each same-char token independently
					int j = wordStart;
					while ( j < wordEnd ) {
						char	token	= format.charAt( j );
						int		tStart	= j;
						while ( j < wordEnd && format.charAt( j ) == token ) {
							j++;
						}
						if ( VALID_PATTERN_LETTERS.contains( token ) ) {
							result.append( format, tStart, j );
						} else if ( ( token == 'T' || token == 't' ) && ( j - tStart ) == 1 ) {
							// Single T/t mid-word (e.g. the T in GMT): AM/PM abbreviation sentinel
							result.append( AMPM_ABBR_SENTINEL );
						} else {
							result.append( '\'' ).append( format, tStart, j ).append( '\'' );
						}
					}
				}

				i = wordEnd;
				continue;
			}

			// Non-letter, non-quote: pass through unchanged
			result.append( c );
			i++;
		}

		return result.toString();
	}

	/**
	 * AM/PM abbreviation map used by {@link #buildFormatter(String)} when assembling a
	 * {@code DateTimeFormatterBuilder} in place of the {@link #AMPM_ABBR_SENTINEL}.
	 * Lucee/ACF define a single {@code T} or {@code t} as the first letter of the AM/PM
	 * indicator: {@code A} for AM and {@code P} for PM.
	 */
	private static final Map<Long, String> AMPM_ABBR_MAP = Map.of( 0L, "A", 1L, "P" );

	/**
	 * Converts a rewritten format string that may contain {@link #AMPM_ABBR_SENTINEL}
	 * placeholders into a fully-built {@link DateTimeFormatter}.
	 *
	 * <p>
	 * When the format string contains no sentinels this method delegates directly to
	 * {@code DateTimeFormatter.ofPattern(format)} and is therefore zero-overhead for the
	 * common case.
	 * </p>
	 *
	 * <p>
	 * When sentinels are present the string is split on each sentinel boundary.
	 * Each non-sentinel segment is added to a {@link DateTimeFormatterBuilder} via
	 * {@code appendPattern}, and each sentinel position is replaced with a custom
	 * {@code appendText(AMPM_OF_DAY)} call that maps {@code 0 → "A"} and {@code 1 → "P"},
	 * producing the single-character AM/PM abbreviation that Lucee/ACF expect from a
	 * single {@code T} or {@code t} mask letter.
	 * </p>
	 *
	 * @param format the post-processed format string from {@link #quoteInvalidPatternLetters}
	 *
	 * @return a {@link DateTimeFormatter} ready for use
	 */
	public static DateTimeFormatter buildFormatter( String format ) {
		if ( !format.contains( AMPM_ABBR_SENTINEL ) ) {
			return DateTimeFormatter.ofPattern( format );
		}

		DateTimeFormatterBuilder	builder	= new DateTimeFormatterBuilder();
		String[]					parts	= format.split( AMPM_ABBR_SENTINEL, -1 );
		for ( int idx = 0; idx < parts.length; idx++ ) {
			if ( !parts[ idx ].isEmpty() ) {
				builder.appendPattern( parts[ idx ] );
			}
			if ( idx < parts.length - 1 ) {
				// Replace sentinel with single-char AM/PM abbreviation
				builder.appendText( ChronoField.AMPM_OF_DAY, AMPM_ABBR_MAP );
			}
		}
		return builder.toFormatter();
	}

}
