package ortus.boxlang.modules.compat.bifs.temporal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.dynamic.casters.IntegerCaster;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.DateTime;
import ortus.boxlang.runtime.types.IStruct;

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

	@DisplayName( "It can parse zoned time" )
	@Test
	public void testCanParseAMPMTime() {
		runtime.executeSource(
		    """
		    result = parseDateTime( "03:00 PM", "HH:nn a" );
		     """,
		    context );
		assertEquals( variables.getAsDateTime( result ).format( "hh:mm a" ), "03:00 PM" );

	}

	@DisplayName( "It tests the speed of both masked and non-masked parsing" )
	@Test
	@Disabled( "Disabled for CI performance. Comment to test locally" )
	public void testSpeed() {
		// @formatter:off
		runtime.executeSource(
		    """
		    function parseIt( string dateString, string mask ){
			var iterations = 10000;
			var start = getTickCount();
			for( var i = 1; i <= iterations; i++ ){
				var result = !isNull( mask ) ? parseDateTime( dateString, mask ) : parseDateTime( dateString )
			}
			var totalTime = ( getTickCount() - start );
			return totalTime;
			}

			setLocale( "de_CH" );

			result = [
				"singleMillisTimestamp" : parseIt( "2025-11-12 00:45:00.0" ),
				"singleMillisTimestampWithMask" : parseIt( "2025-11-12 00:45:00.0", "yyyy-MM-dd HH:mm:ss.S" ),
				"isoTimestamp" : parseIt( "2024-04-02T21:01:00Z" ),
				"isoTimestampWMask" : parseIt( "2024-04-02T21:01:00Z", "yyyy-MM-dd'T'HH:mm:ssXXX" ),
				"mediumFormatZoned" : parseIt( "Nov 22, 2022 11:01:51 CET" ),
				"mediumFormatZonedWithMask" : parseIt( "Nov 22, 2022 11:01:51 CET", "MMM dd, yyyy HH:mm:ss zz" ),
				"euroFormatZoned" : parseIt( "01.04.2011" ),
				"euroFormatZonedWithMask" : parseIt( "01.04.2011", "dd.MM.yyyy" )
			];
			println( result );
		       """,
		    context );
			// @formatter:on

		IStruct	result			= variables.getAsStruct( Key.of( "result" ) );
		long	isoTimestamp	= IntegerCaster.cast( result.get( Key.of( "isoTimestamp" ) ) );
		assertThat( isoTimestamp ).isLessThan( 500 ); // less than 500ms for 10k parses
		long isoTimestampWMask = IntegerCaster.cast( result.get( Key.of( "isoTimestampWMask" ) ) );
		assertThat( isoTimestampWMask ).isLessThan( 100 ); // less than 5 seconds for 10k parses
		long mediumFormatZoned = IntegerCaster.cast( result.get( Key.of( "mediumFormatZoned" ) ) );
		assertThat( mediumFormatZoned ).isLessThan( 500 ); // less than 500ms for 10k parses
		long mediumFormatZonedWithMask = IntegerCaster.cast( result.get( Key.of( "mediumFormatZonedWithMask" ) ) );
		assertThat( mediumFormatZonedWithMask ).isLessThan( 100 ); // less than 100ms for 10k parses
		long euroFormatZoned = IntegerCaster.cast( result.get( Key.of( "euroFormatZoned" ) ) );
		assertThat( euroFormatZoned ).isLessThan( 500 ); // less than 500ms for 10k parses
		long euroFormatZonedWithMask = IntegerCaster.cast( result.get( Key.of( "euroFormatZonedWithMask" ) ) );
		assertThat( euroFormatZonedWithMask ).isLessThan( 100 ); // less than 100ms for 10k parses

	}
}
