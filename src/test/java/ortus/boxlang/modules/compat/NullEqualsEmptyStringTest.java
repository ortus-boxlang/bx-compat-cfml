package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This loads the module and runs an integration test on the module.
 */
public class NullEqualsEmptyStringTest extends BaseIntegrationTest {

	ByteArrayOutputStream baos;

	@DisplayName( "Test null equals empty string" )
	@Test
	public void testNullEqualsEmptyString() {
		runtime.executeSource(
		    """
		    import java:ortus.boxlang.runtime.operators.Compare;
		    		    println( "in test set to: " & Compare.nullEqualsEmptyString );
		    		       result = (nullValue() == "");
		    		       """, context );
		// @formatter:on
		assertThat( variables.get( result ) ).isEqualTo( true );
	}

}
