package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.dynamic.casters.StringCaster;

/**
 * This loads the module and runs an integration test on the module.
 */
public class ThrowablesAreStringsTest extends BaseIntegrationTest {

	@DisplayName( "It can cast a Throwable" )
	@Test
	void testItCanCastAThrowable() {
		Throwable	t		= new Throwable( "Boom" );
		String		myStr	= StringCaster.cast( t );
		assertThat( myStr ).isInstanceOf( String.class );
		assertThat( myStr ).isEqualTo( "java.lang.Throwable: Boom" );
	}

}
