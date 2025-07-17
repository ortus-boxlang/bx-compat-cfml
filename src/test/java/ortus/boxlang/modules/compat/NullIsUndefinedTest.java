package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.compiler.parser.BoxSourceType;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.exceptions.KeyNotFoundException;

/**
 * This loads the module and runs an integration test on the module.
 */
public class NullIsUndefinedTest extends BaseIntegrationTest {

	ByteArrayOutputStream baos;

	@DisplayName( "Test null is undefined" )
	@Test
	public void testNullIsUndefined() {
		// @formatter:off
		Throwable t = assertThrows( KeyNotFoundException.class, () -> runtime.executeSource( """
			foo = null;
			result = foo;
			"""
			, context ) );
		// @formatter:on
		assertThat( t.getMessage() ).contains( "The requested key [foo] was not located" );
	}

	@DisplayName( "Test null scope lookup order" )
	@Test
	public void testNullScopeLookupOrder() {
		// @formatter:off
		runtime.executeSource( """
			function testMe( string foo ) {
				local.foo = null;
				// local scope is checked first, but since local is null, we'll ignore it
				return foo;
			}
			result = testMe( "arguments" );
			"""
			, context );
		// @formatter:on
		assertThat( variables.get( result ) ).isEqualTo( "arguments" );
	}

	@DisplayName( "It tests that null exists" )
	@Test
	public void testNull() {
		// @formatter:off
		runtime.executeSource(
		    """
		    myStruct={ "foo" : null };
		    result = myStruct.keyExists( "foo" );
		    """,
		    context );
		assertThat( variables.getAsBoolean( result ) ).isFalse();
	}


	@DisplayName( "CF transpile structKeyExists" )
	@Test
	public void testCFTranspileStructKeyExists() {
		runtime.executeSource(
		    """
		       str = {
		    	foo : 'bar',
		    	baz : null
		    };
		    result = structKeyExists( str, "foo" )
		    result2 = structKeyExists( str, "baz" )
		    	 """,
		    context, BoxSourceType.CFSCRIPT );

		assertThat( variables.getAsBoolean( result ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "result2" ) ) ).isFalse();

	}

	@DisplayName( "It still sets variables in the local scope even if they are set to null." )
	@Test
	public void testNullStillInLocalScope() {
		// @formatter:off
		runtime.executeSource(
			"""
				function returnsNull() {
					return;
				}

				function doesStuff() {
					var inner = returnsNull();
					if ( !isNull( inner ) ) {
						return inner;
					}
					inner = "local value leaked to variables";
					return "set this time";
				}
				result = doesStuff();
				result = doesStuff();
			""",
			context, BoxSourceType.CFSCRIPT );
		assertThat( variables.getAsString( result ) ).isEqualTo( "set this time" );
	}

	@DisplayName( "It can dump an array list " )
	@Test
	public void testCanDumpAnArrayInHTML() {
		// @formatter:off
		runtime.executeSource(
		    """
				val = [1,2,3,null,5];
				dump( var = val, format = "html" );
		    """,
		    context );
			assertThat( baos.toString().replaceAll( "[ \\t\\r\\n]", "" ) ).contains( "Array:5" );
			// @formatter:on
	}

}
