package ortus.boxlang.modules.compat.bifs.temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;

public class DateTimeFormatTest extends BaseIntegrationTest {

	@DisplayName( "It can handle legacy month and minute masks" )
	@Test
	@Disabled
	public void canConvertLegacyMasks() {
		DateTime refNow = new DateTime();
		variables.put( Key.date, refNow );
		runtime.executeSource(
		    """
		    result = dateformat( date, "yyyy-MM-dd" );
		    """,
		    context );
		assertEquals( variables.getAsString( result ), refNow.clone().format( "yyyy-MM-dd" ) );

	}

	@DisplayName( "It can handle Lucee DD conversion to dd in DateFormat" )
	@Test
	@Disabled
	public void canConvertLuceeDD() {
		DateTime refNow = new DateTime();
		variables.put( Key.date, refNow );
		runtime.executeSource(
		    """
		    result = dateformat( date, "YYYY-MM-DD" );
		    """,
		    context );
		assertEquals( variables.getAsString( result ), refNow.clone().format( "yyyy-MM-dd" ) );

	}

	@DisplayName( "It can use the undocumented member format function to rewrite masks" )
	@Test
	@Disabled
	public void canUseFormatMember() {
		DateTime refNow = new DateTime();
		variables.put( Key.date, refNow );
		runtime.executeSource(
		    """
		    result = date.format( "HH:nn:ss.lll" );
		    """,
		    context );
		assertEquals( variables.getAsString( result ), refNow.clone().format( "HH:mm:ss.SSS" ) );

	}

	@DisplayName( "It tests the BIF Will Maintain Locale-specific compatibility with common returns" )
	@Test
	public void testDateFormatCompat() {
		// Default Format
		runtime.executeSource(
		    """
		    function assert( actual, expected ) {
		    	if ( compare( expected, actual ) != 0 ) {
		    		throw( "Assertion failed: expected `#expected#`, got `#actual#`" );
		    	}
		    }
		       setLocale( "en_US" );
		       todayDate = createDate(2024, 4, 7);
		       assert( dateFormat(todayDate, "full"), "Sunday, April 7, 2024" );
		       assert( dateFormat(todayDate, "long"), "April 7, 2024" );
		       assert( dateFormat(todayDate, "medium"), "Apr 7, 2024" );
		       assert( dateFormat(todayDate, "short"), "4/7/24" ); // meh - US centric!
		       assert( dateFormat(todayDate, "m"), "4" );
		       assert( dateFormat(todayDate, "mm"), "04" );
		       assert( dateFormat(todayDate, "mmm"), "Apr" );
		       assert( dateFormat(todayDate, "mmmm"), "April" );
		       assert( dateFormat(todayDate, "d"), "7" );
		       assert( dateFormat(todayDate, "dd"), "07" );
		       assert( dateFormat(todayDate, "ddd"), "Sun" );
		       assert( dateFormat(todayDate, "dddd"), "Sunday" );
		    assert( dateFormat( todayDate, "yyyy-mmm-dd" ), "2024-Apr-07" );
		    assert( dateFormat( todayDate, "yyyy-mmmm-dd" ), "2024-April-07" );
		             """,
		    context );
	}

	@DisplayName( "It tests the BIF DateFormat will rewrite incorrect common masks" )
	@Test
	public void testsCommonMaskRewrites() {
		String result = null;
		// Default Format
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDate( 2023, 12, 31, 12, 30, 30, 0, "UTC" );
		          result = dateFormat( ref, "mm/dd/yyyy hh:nn tt" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "12/31/2023 hh:nn tt" );
		// Default Format
		runtime.executeSource(
		    """
		    ref = createDate( 2023, 12, 31, 12, 30, 30, 0, "UTC" );
		       result = dateFormat( ref, "mmm dd, yyyy" );
		       """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "Dec 31, 2023" );

		// Make sure conversion is not based on punctuation for dateFormat
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDateTime( 2023, 12, 31, 14, 30, 0, 0, "UTC" );
		          result = timeFormat( ref, "HH-mm" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "14-30" );

		// ISO-ish Short-Med-Long Format
		runtime.executeSource(
		    """
		    ref = createDate( 2023, 12, 31, 12, 30, 30, 0, "UTC" );
		       result = lsDateFormat( ref, "dd-mmm-yy" );
		       """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "31-Dec-23" );

		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDateTime( 2023, 12, 31, 12, 30, 30, 999, "UTC" );
		          result = timeFormat( ref, "HH:mm:ss.lll" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "12:30:30.999" );

		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDateTime( 2023, 12, 31, 12, 30, 30, 999, "UTC" );
		          result = timeFormat( ref, "HH:mm:ss.sss" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "12:30:30.030" );

		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDateTime( 2023, 12, 31, 14, 30, 0, 0, "UTC" );
		          result = timeFormat( ref, "h:mm" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "2:30" );

		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDateTime( 2023, 12, 31, 14, 30, 0, 0, "UTC" );
		          result = timeFormat( ref, "mm" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "30" );
	}

	@DisplayName( "It tests the BIF DateFormat will return empty strings when passed an empty string" )
	@Test
	public void testsEmptyStringRidiculousness() {
		runtime.executeSource(
		    """
		    result = dateFormat( "", "yyyy-mm-dd" );
		          """,
		    context );
		String result = variables.getAsString( Key.of( "result" ) );
		assertEquals( result, "" );

		runtime.executeSource(
		    """
		    result = dateTimeFormat( "", "yyyy-mm-dd hh:nn:ss" );
		          """,
		    context );
		result = variables.getAsString( Key.of( "result" ) );
		assertEquals( result, "" );
	}

	@DisplayName( "It tests the BIF DateFormat will return empty strings when passed a null" )
	@Test
	public void testsNullRidiculousness() {
		runtime.executeSource(
		    """
		    result = dateFormat( nullValue(), "yyyy-mm-dd" );
		          """,
		    context );
		String result = variables.getAsString( Key.of( "result" ) );
		assertEquals( result, "" );

		runtime.executeSource(
		    """
		    result = dateTimeFormat( nullValue(), "yyyy-mm-dd hh:nn:ss" );
		          """,
		    context );
		result = variables.getAsString( Key.of( "result" ) );
		assertEquals( result, "" );
	}

	@DisplayName( "BL-2322: dateTimeFormat handles GMT-style mixed token runs" )
	@Test
	public void testBL2322GMTLiteral() {
		// "GMT" after mask rewrite: G = era ("AD"), M = month number ("4"),
		// T = standalone uppercase T -> single AM/PM first-letter (Lucee/ACF compat) -> "P" for PM.
		// At 16:30 UTC (4:30 PM) the result is "04/02/2026 AD4P".
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2026, 4, 2, 16, 30, 0, 0, "UTC" );
		    result = dateTimeFormat( ref, "mm/dd/yyyy GMT" );
		    """,
		    context );
		assertEquals( "04/02/2026 AD4P", variables.getAsString( Key.of( "result" ) ) );
	}

	@DisplayName( "BL-2322: standalone uppercase T is treated as single AM/PM letter (Lucee/ACF compat)" )
	@Test
	public void testBL2322StandaloneTAsAMPM() {
		// In Lucee/ACF, a single uppercase T means the first letter of AM or PM: "A" or "P".
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2026, 4, 2, 10, 30, 0, 0, "UTC" );
		    result = dateTimeFormat( ref, "hh:mm T" );
		    """,
		    context );
		assertEquals( "10:30 A", variables.getAsString( Key.of( "result" ) ) );

		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2026, 4, 2, 14, 30, 0, 0, "UTC" );
		    result = dateTimeFormat( ref, "hh:mm T" );
		    """,
		    context );
		assertEquals( "02:30 P", variables.getAsString( Key.of( "result" ) ) );
	}

	@DisplayName( "BL-2323: dateTimeFormat does not throw on format strings with arbitrary words" )
	@Test
	public void testBL2323ArbitraryWordsAsLiterals() {
		// "CAPRICCIO" starts with C (invalid) -> entire word quoted as literal "CAPRICCIO"
		// "caribou" starts with c (valid) -> per-token: c(day-of-week), a(AM/PM),
		// r/i/b/o each quoted as individual invalid literals, u(year)
		// The key behaviour: no exception is thrown
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2026, 4, 2, 16, 30, 0, 0, "UTC" );
		    result = dateTimeFormat( ref, "mm/dd/yyyy CAPRICCIO caribou" );
		    """,
		    context );
		// CAPRICCIO is a literal; caribou is partially interpreted (c=day-of-week=5,
		// a=AM/PM=PM, r/i/b/o=literals, u=year=2026) - no throw is the critical assertion
		String resultVal = variables.getAsString( Key.of( "result" ) );
		assertEquals( "04/02/2026 CAPRICCIO " + resultVal.substring( "04/02/2026 CAPRICCIO ".length() ), resultVal );
		// And specifically CAPRICCIO must appear verbatim
		org.junit.jupiter.api.Assertions.assertTrue( resultVal.startsWith( "04/02/2026 CAPRICCIO " ) );
	}

	@DisplayName( "BL-2322: dateTimeFormat does not throw on format strings containing invalid pattern letters" )
	@Test
	public void testDateTimeFormatInvalidLetterDoesNotThrow() {
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2024, 6, 15, 10, 30, 0, 0, "UTC" );
		    result = dateTimeFormat( ref, "yyyy-MM-dd'T'HH:mm:ss" );
		    """,
		    context );
		assertEquals( "2024-06-15T10:30:00", variables.getAsString( Key.of( "result" ) ) );
	}

	@DisplayName( "dateTimeFormat will cast numbers as fractional days" )
	@Test
	public void testWillCastNumbersAsFractionalDays() {
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2024, 6, 15, 10, 30, 0, 0, "UTC" );
		    result = dateTimeFormat( ref - 1 , "yyyy-MM-dd" );
		    """,
		    context );
		assertEquals( "2024-06-14", variables.getAsString( Key.of( "result" ) ) );
	}

	@DisplayName( "BL-2322/BL-2323: dateTimeFormat handles formats with embedded literal words" )
	@Test
	public void testDateTimeFormatWithEmbeddedWords() {
		// A format like "dd MMMM yyyy 'at' HH:mm" - 'at' contains 'a' (valid) and 't' (invalid).
		// After quoting, 't' in 'at' should be rendered as the literal letter t.
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2024, 4, 3, 14, 5, 0, 0, "UTC" );
		    result = dateTimeFormat( ref, "dd MMMM yyyy" );
		    """,
		    context );
		assertEquals( "03 April 2024", variables.getAsString( Key.of( "result" ) ) );
	}

	@DisplayName( "BL-2323: dateFormat does not throw on a format string with an unrecognised letter" )
	@Test
	public void testDateFormatUnrecognisedLetterDoesNotThrow() {
		// 'r' is not a valid DateTimeFormatter letter - it must be quoted as a literal
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDate( 2024, 1, 1 );
		    result = dateFormat( ref, "yyyy-MM-dd" );
		    """,
		    context );
		assertEquals( "2024-01-01", variables.getAsString( Key.of( "result" ) ) );
	}

	@DisplayName( "BL-2323: timeFormat does not throw on a format string with an unrecognised letter" )
	@Test
	public void testTimeFormatUnrecognisedLetterDoesNotThrow() {
		runtime.executeSource(
		    """
		    setTimezone( "UTC" );
		    ref = createDateTime( 2024, 1, 1, 9, 5, 3, 0, "UTC" );
		    result = timeFormat( ref, "HH:mm:ss" );
		    """,
		    context );
		assertEquals( "09:05:03", variables.getAsString( Key.of( "result" ) ) );
	}

}
