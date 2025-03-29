package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.dynamic.casters.StringCaster;

/**
 * This loads the module and runs an integration test on the module.
 */
public class ClassesAreStringsTest extends BaseIntegrationTest {

	@DisplayName( "It can cast a class" )
	@Test
	void testItCanCastAClass() {
		String myStr = StringCaster.cast( "".getClass() );
		assertThat( myStr ).isInstanceOf( String.class );
		assertThat( myStr ).isEqualTo( "java.lang.String" );
	}

}
