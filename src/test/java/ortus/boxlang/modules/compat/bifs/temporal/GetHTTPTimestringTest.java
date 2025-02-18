package ortus.boxlang.modules.compat.bifs.temporal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class GetHTTPTimestringTest extends BaseIntegrationTest {

	@DisplayName( "It tests the BIF Will Maintain Locale-specific compatibility with common returns" )
	@Test
	public void testGetHTTPTimeString() {
		// Default Format
		runtime.executeSource(
		    """
		    function assert( actual, expected ) {
		    	if ( compare( expected, actual ) != 0 ) {
		    		throw( "Assertion failed: expected `#expected#`, got `#actual#`" );
		    	}
		    }
		       setLocale( "en_US" );
		    setTimezone( "America/Los_Angeles" );
		       testDate = createDateTime( 2024, 4, 7, 12, 30, 53 );
		       assert( GetHTTPTimestring( testDate ), "Sun, 07 April 12:30:53 PDT" );
		             """,
		    context );
	}

}
