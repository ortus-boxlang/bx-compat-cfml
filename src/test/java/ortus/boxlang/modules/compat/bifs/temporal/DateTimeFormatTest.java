package ortus.boxlang.modules.compat.bifs.temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;

public class DateTimeFormatTest extends BaseIntegrationTest {

	@DisplayName( "It can handle legacy month and minute masks" )
	@Test
	public void canConvertLegacyMasks() {
		DateTime refNow = new DateTime();
		variables.put( Key.date, refNow );
		runtime.executeSource(
		    """
		    result = dateformat( date, "yyyy-mm-dd hh:nn:ss" );
		    """,
		    context );
		assertEquals( variables.getAsString( result ), refNow.clone().format( "yyyy-MM-dd HH:mm:ss" ) );

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
		assertEquals( result, "12/31/2023 12:30 PM" );
		// Default Format
		runtime.executeSource(
		    """
		    ref = createDate( 2023, 12, 31, 12, 30, 30, 0, "UTC" );
		       result = dateFormat( ref, "mmm dd, yyyy" );
		       """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "Dec 31, 2023" );

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
	}
}
