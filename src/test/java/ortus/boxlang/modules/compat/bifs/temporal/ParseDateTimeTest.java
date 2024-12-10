package ortus.boxlang.modules.compat.bifs.temporal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.dynamic.casters.IntegerCaster;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;

public class ParseDateTimeTest extends BaseIntegrationTest {

	@DisplayName( "It can parse legacy compat date masks" )
	@Test
	public void testCanConvertToMillenium() {
		runtime.executeSource(
		    """
		    result = parseDateTime( "2025-01-02", "yyyy-mm-dd" );
		     """,
		    context );
		assertEquals( variables.getAsDateTime( result ).format( "yyyy-MM-dd" ), "2025-01-02" );

		runtime.executeSource(
		    """
		    result = parseDateTime( "20250102", "yyyymmdd" );
		     """,
		    context );
		assertEquals( variables.getAsDateTime( result ).format( "yyyy-MM-dd" ), "2025-01-02" );

		runtime.executeSource(
		    """
		    result = parseDateTime( "2024-01-14T00:00:01.001", "yyyy-mm-dd'T'HH:nn:ss.lll" );
		    """,
		    context );
		DateTime result = ( DateTime ) variables.get( Key.of( "result" ) );
		assertThat( result ).isInstanceOf( DateTime.class );
		assertThat( result.toString() ).isInstanceOf( String.class );
		assertThat( IntegerCaster.cast( result.format( "yyyy" ) ) ).isEqualTo( 2024 );
		assertThat( IntegerCaster.cast( result.format( "M" ) ) ).isEqualTo( 1 );
		assertThat( IntegerCaster.cast( result.format( "d" ) ) ).isEqualTo( 14 );
		assertThat( IntegerCaster.cast( result.format( "H" ) ) ).isEqualTo( 0 );
		assertThat( IntegerCaster.cast( result.format( "m" ) ) ).isEqualTo( 0 );
		assertThat( IntegerCaster.cast( result.format( "s" ) ) ).isEqualTo( 1 );
		assertThat( IntegerCaster.cast( result.format( "n" ) ) ).isEqualTo( 1000000 );
	}
}
