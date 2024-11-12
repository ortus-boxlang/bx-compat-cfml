package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.types.exceptions.KeyNotFoundException;

/**
 * This loads the module and runs an integration test on the module.
 */
public class NullIsUndefinedTest extends BaseIntegrationTest {

	@DisplayName( "Test null is undefined" )
	@Test
	public void testNullIsUndefined() {
		loadModule();

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
		loadModule();

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
		loadModule();

		// @formatter:off
		runtime.executeSource(
		    """
		    myStruct={ "foo" : null };
		    result = myStruct.keyExists( "foo" );
		    """,
		    context );
		assertThat( variables.getAsBoolean( result ) ).isFalse();
	}

	@DisplayName( "It resets nulls in the local scope between invocations" )
	@Test
	public void testNullResets() {
		loadModule();

		// @formatter:off
		runtime.executeSource(
		    """
				function foo( callback ) {
					var bar = callback();
					if ( isNull( bar ) ) {
						bar = "baz";
					}
					return isNull( bar ) ? javacast( "null", "" ) : bar;
				}

				foo( function() {} );
				result = foo( function() {} );
		    """,
		    context );
		assertThat( variables.get( result ) ).isNull();
	}
}
