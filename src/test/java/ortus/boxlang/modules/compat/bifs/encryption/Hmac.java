package ortus.boxlang.modules.compat.bifs.encryption;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.util.EncryptionUtil;

public class Hmac extends BaseIntegrationTest {

	@DisplayName( "It returns a hmac with all uppercase letters" )
	@Test
	void testHmacWillUppercase() {
		String referenceMac = EncryptionUtil.hmac( "Hmac me baby!", "foo", "hmacmd5", "utf-8" );
		runtime.executeSource(
		    """
		    result = Hmac( "Hmac me baby!", "foo" );
		    """,
		    context );
		var result = variables.get( Key.of( "result" ) );
		assertThat( result ).isInstanceOf( String.class );
		assertThat( variables.getAsString( Key.of( "result" ) ).length() ).isEqualTo( referenceMac.length() );
		assertEquals( variables.getAsString( Key.of( "result" ) ), "48bfb8004f92d6c9e9eac9728c5d919c".toUpperCase() );
	}

}
