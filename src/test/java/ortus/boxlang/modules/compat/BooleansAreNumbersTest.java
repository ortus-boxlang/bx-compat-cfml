package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.dynamic.casters.NumberCaster;
import ortus.boxlang.runtime.operators.Negate;

/**
 * This loads the module and runs an integration test on the module.
 */
public class BooleansAreNumbersTest extends BaseIntegrationTest {

	@DisplayName( "It can cast a boolean to a Number" )
	@Test
	void testItCanCastABoolean() {
		Number result = NumberCaster.cast( true );
		assertThat( result ).isInstanceOf( Integer.class );
		assertThat( result.doubleValue() ).isEqualTo( 1 );

		result = NumberCaster.cast( false );
		assertThat( result ).isInstanceOf( Integer.class );
		assertThat( result.doubleValue() ).isEqualTo( 0 );

		result = NumberCaster.cast( "true" );
		assertThat( result ).isInstanceOf( Integer.class );
		assertThat( result.doubleValue() ).isEqualTo( 1 );

		result = NumberCaster.cast( "false" );
		assertThat( result ).isInstanceOf( Integer.class );
		assertThat( result.doubleValue() ).isEqualTo( 0 );

		result = NumberCaster.cast( "yes" );
		assertThat( result ).isInstanceOf( Integer.class );
		assertThat( result.doubleValue() ).isEqualTo( 1 );

		result = NumberCaster.cast( "no" );
		assertThat( result ).isInstanceOf( Integer.class );
		assertThat( result.doubleValue() ).isEqualTo( 0 );
	}

	@DisplayName( "It can mathematically negate a boolean" )
	@Test
	void testItCanNegateBoolean() {
		assertThat( Negate.invoke( true ) ).isEqualTo( -1 );
		assertThat( Negate.invoke( false ) ).isEqualTo( 0 );
		assertThat( Negate.invoke( "true" ) ).isEqualTo( -1 );
		assertThat( Negate.invoke( "false" ) ).isEqualTo( 0 );
		assertThat( Negate.invoke( "yes" ) ).isEqualTo( -1 );
		assertThat( Negate.invoke( "no" ) ).isEqualTo( 0 );
	}

}
