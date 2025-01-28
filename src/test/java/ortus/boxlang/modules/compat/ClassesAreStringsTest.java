package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.compiler.parser.BoxSourceType;
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

	@DisplayName( "It can use string BIF" )
	@Test
	void testItCanUseStringBIF() {
		runtime.executeSource( """
		                       	result = listLast( "Hello, World".getClass(), '.' );
		                       	result2 = "Hello, World".getClass().listLast( '.' );
		                       	result3 = "foo #'brad'.getClass()# bar";
		                       	result4 = "foo " & 'brad'.getClass() & " bar";
		                       """,
		    context, BoxSourceType.CFSCRIPT );
		assertThat( variables.get( result ) ).isEqualTo( "String" );
		assertThat( variables.get( "result2" ) ).isEqualTo( "String" );
		assertThat( variables.get( "result3" ) ).isEqualTo( "foo java.lang.String bar" );
		assertThat( variables.get( "result4" ) ).isEqualTo( "foo java.lang.String bar" );
	}

}
