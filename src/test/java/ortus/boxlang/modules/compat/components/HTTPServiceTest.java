package ortus.boxlang.modules.compat.components;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

@WireMockTest
public class HTTPServiceTest extends BaseIntegrationTest {

	@DisplayName( "It can create HTTP service and make a basic GET request" )
	@Test
	public void testBasicGet( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/test" ).willReturn( aResponse().withBody( "Hello World" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      httpService.setUrl( "%s" );
		                                      httpService.setMethod( "GET" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/test" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
		assertThat( prefix.getAsString( Key.of( "fileContent" ) ).trim() ).isEqualTo( "Hello World" );
	}

	@DisplayName( "It can initialize attributes in the constructor" )
	@Test
	public void testConstructorAttributes( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/init" ).willReturn( aResponse().withBody( "Initialized" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="GET" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/init" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
		assertThat( prefix.getAsString( Key.of( "fileContent" ) ).trim() ).isEqualTo( "Initialized" );
	}

	@DisplayName( "It can pass attributes on send()" )
	@Test
	public void testPassAttributesOnSend( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/send-attrs" ).willReturn( aResponse().withBody( "Send Attrs" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      result = httpService.send( url="%s", method="GET" );
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/send-attrs" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
		assertThat( prefix.getAsString( Key.of( "fileContent" ) ).trim() ).isEqualTo( "Send Attrs" );
	}

	@DisplayName( "It can add header params" )
	@Test
	public void testAddHeaderParam( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/headers" )
		    .withHeader( "X-Custom-Header", equalTo( "myvalue" ) )
		    .willReturn( aResponse().withBody( "Got Header" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="GET" );
		                                      httpService.addParam( type="header", name="X-Custom-Header", value="myvalue" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/headers" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
		assertThat( prefix.getAsString( Key.of( "fileContent" ) ).trim() ).isEqualTo( "Got Header" );
	}

	@DisplayName( "It can make a POST request with body" )
	@Test
	public void testPostWithBody( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( post( "/api/post" ).willReturn( aResponse().withBody( "Posted" ).withStatus( 201 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="POST" );
		                                      httpService.addParam( type="body", value="test body content" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/post" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "201" );

		verify( postRequestedFor( urlEqualTo( "/api/post" ) ) );
	}

	@DisplayName( "It can add URL params" )
	@Test
	public void testAddUrlParam( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/search?q=boxlang" ).willReturn( aResponse().withBody( "Found" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="GET" );
		                                      httpService.addParam( type="url", name="q", value="boxlang" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/search" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
	}

	@DisplayName( "It returns the result object with getPrefix()" )
	@Test
	public void testResultObjectStructure( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/result" ).willReturn( aResponse().withBody( "Result" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="GET" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      hasFileContent = structKeyExists( prefix, "fileContent" );
		                                      hasStatusCode = structKeyExists( prefix, "status_code" );
		                                      hasStatusText = structKeyExists( prefix, "status_text" );
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/result" ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasFileContent" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasStatusCode" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasStatusText" ) ) ).isTrue();
	}

	@DisplayName( "It supports method chaining" )
	@Test
	public void testMethodChaining( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/chain" )
		    .withHeader( "X-Test", equalTo( "chained" ) )
		    .willReturn( aResponse().withBody( "Chained" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      result = new http()
		                                          .setUrl( "%s" )
		                                          .setMethod( "GET" )
		                                          .addParam( type="header", name="X-Test", value="chained" )
		                                          .send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/chain" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
		assertThat( prefix.getAsString( Key.of( "fileContent" ) ).trim() ).isEqualTo( "Chained" );
	}

	@DisplayName( "It handles non-200 status codes" )
	@Test
	public void testNon200StatusCode( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/notfound" ).willReturn( aResponse().withBody( "Not Found" ).withStatus( 404 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="GET" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/notfound" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "404" );
	}

	@DisplayName( "It can send multiple params" )
	@Test
	public void testMultipleParams( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/multi" )
		    .withHeader( "X-One", equalTo( "1" ) )
		    .withHeader( "X-Two", equalTo( "2" ) )
		    .willReturn( aResponse().withBody( "Multi" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="GET" );
		                                      httpService.addParam( type="header", name="X-One", value="1" );
		                                      httpService.addParam( type="header", name="X-Two", value="2" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/multi" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
	}

	@DisplayName( "It can use setAttributes to set multiple attributes at once" )
	@Test
	public void testSetAttributes( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/attrs" ).willReturn( aResponse().withBody( "Attrs" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      httpService.setAttributes( url="%s", method="GET" );
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/attrs" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
	}

	@DisplayName( "It can clear params" )
	@Test
	public void testClearParams( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/api/clear" ).willReturn( aResponse().withBody( "Cleared" ).withStatus( 200 ) ) );

		runtime.executeSource( String.format( """
		                                      httpService = new http( url="%s", method="GET" );
		                                      httpService.addParam( type="header", name="X-Remove", value="gone" );
		                                      httpService.clearParams();
		                                      result = httpService.send();
		                                      prefix = result.getPrefix();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/clear" ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsString( Key.of( "status_code" ) ) ).isEqualTo( "200" );
	}

	@DisplayName( "It supports implicit getters like getUrl() and getMethod()" )
	@Test
	public void testImplicitGetters( WireMockRuntimeInfo wmRuntimeInfo ) {
		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      httpService.setUrl( "%s" );
		                                      httpService.setMethod( "GET" );
		                                      myUrl = httpService.getUrl();
		                                      myMethod = httpService.getMethod();
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/test" ),
		    context );

		assertThat( variables.getAsString( Key.of( "myUrl" ) ) ).isEqualTo( wmRuntimeInfo.getHttpBaseUrl() + "/api/test" );
		assertThat( variables.getAsString( Key.of( "myMethod" ) ) ).isEqualTo( "GET" );
	}

	@DisplayName( "It can use getAttributes() to retrieve all attributes" )
	@Test
	public void testGetAttributes( WireMockRuntimeInfo wmRuntimeInfo ) {
		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      httpService.setUrl( "%s" );
		                                      httpService.setMethod( "GET" );
		                                      attrs = httpService.getAttributes();
		                                      hasUrl = structKeyExists( attrs, "url" );
		                                      hasMethod = structKeyExists( attrs, "method" );
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/test" ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasUrl" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasMethod" ) ) ).isTrue();
	}

	@DisplayName( "It can use getAttributes() with a filtered list" )
	@Test
	public void testGetAttributesFiltered( WireMockRuntimeInfo wmRuntimeInfo ) {
		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      httpService.setUrl( "%s" );
		                                      httpService.setMethod( "GET" );
		                                      httpService.setTimeout( 30 );
		                                      filtered = httpService.getAttributes( "url,method" );
		                                      hasUrl = structKeyExists( filtered, "url" );
		                                      hasTimeout = structKeyExists( filtered, "timeout" );
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/test" ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasUrl" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasTimeout" ) ) ).isFalse();
	}

	@DisplayName( "It can use clearAttributes() to remove specific attributes" )
	@Test
	public void testClearAttributes( WireMockRuntimeInfo wmRuntimeInfo ) {
		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      httpService.setUrl( "%s" );
		                                      httpService.setMethod( "GET" );
		                                      httpService.clearAttributes( "method" );
		                                      attrs = httpService.getAttributes();
		                                      hasUrl = structKeyExists( attrs, "url" );
		                                      hasMethod = structKeyExists( attrs, "method" );
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/test" ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasUrl" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasMethod" ) ) ).isFalse();
	}

	@DisplayName( "It can use clear() to reset all attributes and params" )
	@Test
	public void testClear( WireMockRuntimeInfo wmRuntimeInfo ) {
		runtime.executeSource( String.format( """
		                                      httpService = new http();
		                                      httpService.setUrl( "%s" );
		                                      httpService.setMethod( "GET" );
		                                      httpService.addParam( type="header", name="X-Test", value="val" );
		                                      httpService.clear();
		                                      attrs = httpService.getAttributes();
		                                      attrCount = structCount( attrs );
		                                      """, wmRuntimeInfo.getHttpBaseUrl() + "/api/test" ),
		    context );

		assertThat( ( ( Number ) variables.get( Key.of( "attrCount" ) ) ).intValue() ).isEqualTo( 0 );
	}

}
