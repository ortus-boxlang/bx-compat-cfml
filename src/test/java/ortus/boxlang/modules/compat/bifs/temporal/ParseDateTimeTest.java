package ortus.boxlang.modules.compat.bifs.temporal;

import static com.google.common.truth.Truth.assertThat;
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
import ortus.boxlang.runtime.dynamic.casters.IntegerCaster;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.types.DateTime;

public class ParseDateTimeTest {

	static BoxRuntime	instance;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= new Key( "result" );

	@BeforeAll
	public static void setUp() {
		instance = BoxRuntime.getInstance( true );
		BIFDescriptor descriptor = instance.getFunctionService().getGlobalFunction( "createDateTime" );
		assertEquals( "ortus.boxlang.modules.compat.bifs.temporal.CreateDateTime", descriptor.BIFClass.getName() );
	}

	@AfterAll
	public static void teardown() {

	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext( instance.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@DisplayName( "It can parse legacy compat date masks" )
	@Test
	public void testCanConvertToMillenium() {
		instance.executeSource(
		    """
		    result = parseDateTime( "2025-01-02", "yyyy-mm-dd" );
		     """,
		    context );
		assertEquals( variables.getAsDateTime( result ).format( "yyyy-MM-dd" ), "2025-01-02" );

		instance.executeSource(
		    """
		    result = parseDateTime( "20250102", "yyyymmdd" );
		     """,
		    context );
		assertEquals( variables.getAsDateTime( result ).format( "yyyy-MM-dd" ), "2025-01-02" );

		instance.executeSource(
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
