package ortus.boxlang.modules.compat.bifs.encryption;

import static com.google.common.truth.Truth.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class HashTest extends BaseIntegrationTest {

	@DisplayName( "It returns a hash with all uppercase letters" )
	@Test
	void testHashWillUppercase() {
		runtime.executeSource(
		    """
		    result = hash( "foo", "md5" );
		    """,
		    context );
		Pattern	pattern	= Pattern.compile( "[a-z]" );
		Matcher	matcher	= pattern.matcher( variables.getAsString( result ) );
		assertThat( matcher.matches() ).isFalse();
		assertThat( variables.getAsString( result ) ).isEqualTo( "ACBD18DB4CC2F85CEDEF654FCCC4A4D8" );
	}

	@DisplayName( "It tests for the default lucee compat iterations result" )
	@Test
	void testIterationsResult() {
		runtime.executeSource(
		    """
		    result = hash( "foo", "md5", "UTF-8", 10 );
		    """,
		    context );
		Pattern	pattern	= Pattern.compile( "[a-z]" );
		Matcher	matcher	= pattern.matcher( variables.getAsString( result ) );
		assertThat( matcher.matches() ).isFalse();
		assertThat( variables.getAsString( result ) ).isEqualTo( "97A37D4A5D9470A31855436DE0A1F2A5" );
	}

}
