package ortus.boxlang.modules.compat.bifs.temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class CreateDateTimeTest extends BaseIntegrationTest {

	@DisplayName( "It can handle Lucee and ACFs non-century date weirdness" )
	@Test
	public void testCanConvertToMillenium() {
		runtime.executeSource(
		    """
		       dateObj = createDateTime( 5 );
		    result = year( dateObj );
		       """,
		    context );
		assertEquals( 2005, variables.getAsInteger( result ) );

		runtime.executeSource(
		    """
		       dateObj = createDateTime( 99 );
		    result = year( dateObj );
		       """,
		    context );
		assertEquals( 1999, variables.getAsInteger( result ) );
	}
}
