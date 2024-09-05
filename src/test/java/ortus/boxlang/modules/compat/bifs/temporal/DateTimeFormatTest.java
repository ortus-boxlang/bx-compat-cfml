package ortus.boxlang.modules.compat.bifs.temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.bifs.BIFDescriptor;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.types.DateTime;

public class DateTimeFormatTest {

	static BoxRuntime	instance;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= new Key( "result" );

	@BeforeAll
	public static void setUp() {
		instance = BoxRuntime.getInstance( true );
		BIFDescriptor descriptor = instance.getFunctionService().getGlobalFunction( "dateFormat" );
		assertEquals( "ortus.boxlang.modules.compat.bifs.temporal.DateTimeFormat", descriptor.BIFClass.getName() );
	}

	@AfterAll
	public static void teardown() {

	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext( instance.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@DisplayName( "It can handle legacy month and minute masks" )
	@Test
	public void canConvertLegacyMasks() {
		DateTime refNow = new DateTime();
		variables.put( Key.date, refNow );
		instance.executeSource(
		    """
		    result = dateformat( date, "yyyy-mm-dd hh:nn:ss" );
		    """,
		    context );
		assertEquals( refNow.clone().format( "yyyy-MM-dd HH:mm:ss" ), variables.getAsString( result ) );

	}

	@DisplayName( "It tests the BIF Will Maintain Locale-specific compatibility with common returns" )
	@Test
	public void testDateFormatCompat() {
		// Default Format
		instance.executeSource(
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
		             """,
		    context );
	}

	@DisplayName( "It tests the BIF DateFormat will rewrite incorrect common masks" )
	@Test
	public void testsCommonMaskRewrites() {
		String result = null;
		// Default Format
		instance.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDate( 2023, 12, 31, 12, 30, 30, 0, "UTC" );
		          result = dateFormat( ref, "mm/dd/yyyy hh:nn tt" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "12/31/2023 12:30 PM" );
		// Default Format
		instance.executeSource(
		    """
		    ref = createDate( 2023, 12, 31, 12, 30, 30, 0, "UTC" );
		       result = dateFormat( ref, "mmm dd, yyyy" );
		       """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "Dec 31, 2023" );

		instance.executeSource(
		    """
		    setTimezone( "UTC" );
		       ref = createDateTime( 2023, 12, 31, 12, 30, 30, 999, "UTC" );
		          result = timeFormat( ref, "HH:mm:ss.l" );
		          """,
		    context );
		result = ( String ) variables.get( Key.of( "result" ) );
		assertEquals( result, "12:30:30.999" );
	}
}
