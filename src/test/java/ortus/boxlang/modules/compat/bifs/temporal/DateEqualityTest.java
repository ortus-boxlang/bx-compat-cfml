package ortus.boxlang.modules.compat.bifs.temporal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class DateEqualityTest extends BaseIntegrationTest {

	@DisplayName( "It will only compare dates loosely, based on the instant" )
	@Test
	public void testCanConvertToMillenium() {
		runtime.executeSource(
		    """
			dateOne = parseDateTime( '2025-02-27T13:00:00-05:00' );
			dateTwo = parseDateTime( '2025-02-27T10:00:00-08:00' );
			assert dateOne == dateTwo;
			assert dateOne.equals( dateTwo );
			assert dateCompare(dateOne, dateTwo) == 0;
		       """,
		    context );
	}
}
