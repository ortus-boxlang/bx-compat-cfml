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

@DisplayName( "new Mail() Service Tests" )
public class MailServiceTest extends BaseIntegrationTest {

	@BeforeAll
	public static void setupMailTests() {
		assumeTrue( BoxRuntime.getInstance().getModuleService().hasModule( Key.of( "mail" ) ),
		    "bx-mail module is not installed — skipping Mail service tests" );
	}

	@DisplayName( "It can create a Mail service and set attributes via implicit setters" )
	@Test
	public void testImplicitSetters() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setTo( "test@example.com" );
		                       mailService.setFrom( "sender@example.com" );
		                       mailService.setSubject( "Test Subject" );
		                       myTo = mailService.getTo();
		                       myFrom = mailService.getFrom();
		                       mySubject = mailService.getSubject();
		                       """,
		    context );

		assertThat( variables.getAsString( Key.of( "myTo" ) ) ).isEqualTo( "test@example.com" );
		assertThat( variables.getAsString( Key.of( "myFrom" ) ) ).isEqualTo( "sender@example.com" );
		assertThat( variables.getAsString( Key.of( "mySubject" ) ) ).isEqualTo( "Test Subject" );
	}

	@DisplayName( "It can initialize attributes in the constructor" )
	@Test
	public void testConstructorAttributes() {
		runtime.executeSource( """
		                       mailService = new Mail( to="test@example.com", from="sender@example.com", subject="Hello" );
		                       attrs = mailService.getAttributes();
		                       hasTo = structKeyExists( attrs, "to" );
		                       hasFrom = structKeyExists( attrs, "from" );
		                       hasSubject = structKeyExists( attrs, "subject" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasTo" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasFrom" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasSubject" ) ) ).isTrue();
	}

	@DisplayName( "It can use setAttributes() to set multiple attributes at once" )
	@Test
	public void testSetAttributes() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setAttributes( to="test@example.com", from="sender@example.com", subject="Bulk Set" );
		                       attrs = mailService.getAttributes();
		                       """,
		    context );

		IStruct attrs = ( IStruct ) variables.get( Key.of( "attrs" ) );
		assertThat( attrs.containsKey( Key.of( "to" ) ) ).isTrue();
		assertThat( attrs.containsKey( Key.of( "from" ) ) ).isTrue();
		assertThat( attrs.containsKey( Key.of( "subject" ) ) ).isTrue();
	}

	@DisplayName( "It can use getAttributes() with a filtered list" )
	@Test
	public void testGetAttributesFiltered() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setTo( "test@example.com" );
		                       mailService.setFrom( "sender@example.com" );
		                       mailService.setSubject( "Hello" );
		                       filtered = mailService.getAttributes( "to,subject" );
		                       hasTo = structKeyExists( filtered, "to" );
		                       hasFrom = structKeyExists( filtered, "from" );
		                       hasSubject = structKeyExists( filtered, "subject" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasTo" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasFrom" ) ) ).isFalse();
		assertThat( variables.getAsBoolean( Key.of( "hasSubject" ) ) ).isTrue();
	}

	@DisplayName( "It can use clearAttributes() to remove specific attributes" )
	@Test
	public void testClearAttributes() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setTo( "test@example.com" );
		                       mailService.setFrom( "sender@example.com" );
		                       mailService.clearAttributes( "to" );
		                       attrs = mailService.getAttributes();
		                       hasTo = structKeyExists( attrs, "to" );
		                       hasFrom = structKeyExists( attrs, "from" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasTo" ) ) ).isFalse();
		assertThat( variables.getAsBoolean( Key.of( "hasFrom" ) ) ).isTrue();
	}

	@DisplayName( "It supports method chaining on setters" )
	@Test
	public void testMethodChaining() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setTo( "test@example.com" )
		                           .setFrom( "sender@example.com" )
		                           .setSubject( "Chained" );
		                       mySubject = mailService.getSubject();
		                       """,
		    context );

		assertThat( variables.getAsString( Key.of( "mySubject" ) ) ).isEqualTo( "Chained" );
	}

	@DisplayName( "It can addParam() for attachments and headers" )
	@Test
	public void testAddParam() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setTo( "test@example.com" );
		                       mailService.setFrom( "sender@example.com" );
		                       mailService.setSubject( "With Param" );
		                       mailService.addParam( name="X-Custom-Header", value="myvalue" );
		                       attrs = mailService.getAttributes();
		                       """,
		    context );

		// Just verifying it doesn't error — can't verify the param was sent
		IStruct attrs = ( IStruct ) variables.get( Key.of( "attrs" ) );
		assertThat( attrs.containsKey( Key.of( "to" ) ) ).isTrue();
	}

	@DisplayName( "It can addPart() for multipart messages" )
	@Test
	public void testAddPart() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setTo( "test@example.com" );
		                       mailService.setFrom( "sender@example.com" );
		                       mailService.setSubject( "Multipart" );
		                       mailService.addPart( type="text", body="Plain text version" );
		                       mailService.addPart( type="html", body="<h1>HTML version</h1>" );
		                       attrs = mailService.getAttributes();
		                       """,
		    context );

		IStruct attrs = ( IStruct ) variables.get( Key.of( "attrs" ) );
		assertThat( attrs.containsKey( Key.of( "subject" ) ) ).isTrue();
	}

	@DisplayName( "It can clearParams()" )
	@Test
	public void testClearParams() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.addParam( name="X-Header", value="val" );
		                       mailService.clearParams();
		                       attrs = mailService.getAttributes();
		                       """,
		    context );

		// Just verifying it doesn't error
		assertThat( variables.get( Key.of( "attrs" ) ) ).isNotNull();
	}

	@DisplayName( "It can clearParts()" )
	@Test
	public void testClearParts() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.addPart( type="text", body="text" );
		                       mailService.addPart( type="html", body="<b>html</b>" );
		                       mailService.clearParts();
		                       attrs = mailService.getAttributes();
		                       """,
		    context );

		assertThat( variables.get( Key.of( "attrs" ) ) ).isNotNull();
	}

	@DisplayName( "It can use clear() to reset everything" )
	@Test
	public void testClear() {
		runtime.executeSource( """
		                       mailService = new Mail();
		                       mailService.setTo( "test@example.com" );
		                       mailService.setFrom( "sender@example.com" );
		                       mailService.addParam( name="X-Header", value="val" );
		                       mailService.addPart( type="text", body="text" );
		                       mailService.clear();
		                       attrs = mailService.getAttributes();
		                       attrCount = structCount( attrs );
		                       """,
		    context );

		assertThat( ( ( Number ) variables.get( Key.of( "attrCount" ) ) ).intValue() ).isEqualTo( 0 );
	}

}
