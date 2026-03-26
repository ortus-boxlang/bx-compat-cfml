package ortus.boxlang.modules.compat.components;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

@DisplayName( "new ftp() Service Tests" )
public class FTPServiceTest extends BaseIntegrationTest {

	@BeforeAll
	public static void setupFTPTests() {
		assumeTrue( BoxRuntime.getInstance().getModuleService().hasModule( Key.of( "ftp" ) ),
		    "bx-ftp module is not installed — skipping FTP service tests" );
	}

	@DisplayName( "It can create an FTP service and set attributes via implicit setters" )
	@Test
	public void testImplicitSetters() {
		runtime.executeSource( """
		                       ftpService = new ftp();
		                       ftpService.setServer( "ftp.example.com" );
		                       ftpService.setUsername( "testuser" );
		                       ftpService.setPassword( "testpass" );
		                       myServer = ftpService.getServer();
		                       myUser = ftpService.getUsername();
		                       """,
		    context );

		assertThat( variables.getAsString( Key.of( "myServer" ) ) ).isEqualTo( "ftp.example.com" );
		assertThat( variables.getAsString( Key.of( "myUser" ) ) ).isEqualTo( "testuser" );
	}

	@DisplayName( "It can initialize attributes in the constructor" )
	@Test
	public void testConstructorAttributes() {
		runtime.executeSource( """
		                       ftpService = new ftp( server="ftp.example.com", username="user", password="pass" );
		                       attrs = ftpService.getAttributes();
		                       hasServer = structKeyExists( attrs, "server" );
		                       hasUsername = structKeyExists( attrs, "username" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasServer" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasUsername" ) ) ).isTrue();
	}

	@DisplayName( "It can use setAttributes() for bulk configuration" )
	@Test
	public void testSetAttributes() {
		runtime.executeSource( """
		                       ftpService = new ftp();
		                       ftpService.setAttributes( server="ftp.example.com", username="user", password="pass", port=21 );
		                       attrs = ftpService.getAttributes();
		                       hasPort = structKeyExists( attrs, "port" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasPort" ) ) ).isTrue();
	}

	@DisplayName( "It can use getAttributes() with a filtered list" )
	@Test
	public void testGetAttributesFiltered() {
		runtime.executeSource( """
		                       ftpService = new ftp();
		                       ftpService.setServer( "ftp.example.com" );
		                       ftpService.setUsername( "user" );
		                       ftpService.setPassword( "pass" );
		                       filtered = ftpService.getAttributes( "server,username" );
		                       hasServer = structKeyExists( filtered, "server" );
		                       hasPassword = structKeyExists( filtered, "password" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasServer" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasPassword" ) ) ).isFalse();
	}

	@DisplayName( "It can use clearAttributes() and clear()" )
	@Test
	public void testClearAndClearAttributes() {
		runtime.executeSource( """
		                       ftpService = new ftp();
		                       ftpService.setServer( "ftp.example.com" );
		                       ftpService.setUsername( "user" );
		                       ftpService.clearAttributes( "server" );
		                       attrs1 = ftpService.getAttributes();
		                       hasServer = structKeyExists( attrs1, "server" );
		                       hasUsername = structKeyExists( attrs1, "username" );

		                       ftpService.clear();
		                       attrs2 = ftpService.getAttributes();
		                       attrCount = structCount( attrs2 );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasServer" ) ) ).isFalse();
		assertThat( variables.getAsBoolean( Key.of( "hasUsername" ) ) ).isTrue();
		assertThat( ( ( Number ) variables.get( Key.of( "attrCount" ) ) ).intValue() ).isEqualTo( 0 );
	}

	@DisplayName( "It supports method chaining on setters" )
	@Test
	public void testMethodChaining() {
		runtime.executeSource( """
		                       ftpService = new ftp();
		                       ftpService.setServer( "ftp.example.com" )
		                           .setUsername( "user" )
		                           .setPassword( "pass" );
		                       myServer = ftpService.getServer();
		                       """,
		    context );

		assertThat( variables.getAsString( Key.of( "myServer" ) ) ).isEqualTo( "ftp.example.com" );
	}

}
