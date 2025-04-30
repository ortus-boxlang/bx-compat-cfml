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
	void testItFormatsPositiveNumberAsDollarString() {
		runtime.executeSource(
		    """
		    result = hash( "foo", "md5" );
		    """,
		    context );
		Pattern	pattern	= Pattern.compile( "[a-z]" );
		Matcher	matcher	= pattern.matcher( variables.getAsString( result ) );
		assertThat( matcher.matches() ).isFalse();
	}

}
