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

@DisplayName( "new feed() Service Tests" )
public class FeedServiceTest extends BaseIntegrationTest {

	@BeforeAll
	public static void setupFeedTests() {
		assumeTrue( BoxRuntime.getInstance().getModuleService().hasModule( Key.of( "rss" ) ),
		    "bx-rss module is not installed — skipping Feed service tests" );
	}

	@DisplayName( "It can create a Feed service and set attributes via implicit setters" )
	@Test
	public void testImplicitSetters() {
		runtime.executeSource( """
		                       feedService = new feed();
		                       feedService.setSource( "http://feeds.bbci.co.uk/news/rss.xml" );
		                       mySource = feedService.getSource();
		                       """,
		    context );

		assertThat( variables.getAsString( Key.of( "mySource" ) ) ).isEqualTo( "http://feeds.bbci.co.uk/news/rss.xml" );
	}

	@DisplayName( "It can initialize attributes in the constructor" )
	@Test
	public void testConstructorAttributes() {
		runtime.executeSource( """
		                       feedService = new feed( source="http://feeds.bbci.co.uk/news/rss.xml" );
		                       attrs = feedService.getAttributes();
		                       hasSource = structKeyExists( attrs, "source" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasSource" ) ) ).isTrue();
	}

	@DisplayName( "It can use setAttributes() for bulk configuration" )
	@Test
	public void testSetAttributes() {
		runtime.executeSource( """
		                       feedService = new feed();
		                       feedService.setAttributes( source="http://feeds.bbci.co.uk/news/rss.xml", timeout=30 );
		                       attrs = feedService.getAttributes();
		                       hasSource = structKeyExists( attrs, "source" );
		                       hasTimeout = structKeyExists( attrs, "timeout" );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasSource" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasTimeout" ) ) ).isTrue();
	}

	@DisplayName( "It can use clearAttributes() and clear()" )
	@Test
	public void testClearAndClearAttributes() {
		runtime.executeSource( """
		                       feedService = new feed();
		                       feedService.setSource( "http://example.com/feed.xml" );
		                       feedService.setTimeout( 30 );
		                       feedService.clearAttributes( "source" );
		                       attrs1 = feedService.getAttributes();
		                       hasSource = structKeyExists( attrs1, "source" );
		                       hasTimeout = structKeyExists( attrs1, "timeout" );

		                       feedService.clear();
		                       attrs2 = feedService.getAttributes();
		                       attrCount = structCount( attrs2 );
		                       """,
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasSource" ) ) ).isFalse();
		assertThat( variables.getAsBoolean( Key.of( "hasTimeout" ) ) ).isTrue();
		assertThat( ( ( Number ) variables.get( Key.of( "attrCount" ) ) ).intValue() ).isEqualTo( 0 );
	}

	@DisplayName( "It supports method chaining on setters" )
	@Test
	public void testMethodChaining() {
		runtime.executeSource( """
		                       feedService = new feed();
		                       feedService.setSource( "http://example.com/feed.xml" )
		                           .setTimeout( 30 );
		                       mySource = feedService.getSource();
		                       """,
		    context );

		assertThat( variables.getAsString( Key.of( "mySource" ) ) ).isEqualTo( "http://example.com/feed.xml" );
	}

}
