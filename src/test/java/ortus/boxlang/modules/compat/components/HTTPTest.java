package ortus.boxlang.modules.compat.components;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

@WireMockTest
public class HTTPTest extends BaseIntegrationTest {

	@DisplayName( "It can make HTTP call script" )
	@Test
	public void testCanMakeHTTPCallScript( WireMockRuntimeInfo wmRuntimeInfo ) {
		stubFor( get( "/posts/1" ).willReturn( aResponse().withBody( "Done" ).withStatus( 200 ) ) );

		String baseURL = wmRuntimeInfo.getHttpBaseUrl();

		runtime.executeSource( String.format( """
		                                       bx:http url="%s" {
		                                           bx:httpparam type="header" name="User-Agent" value="Mozilla";
		                                      }
		                                      result = bxhttp;
		                                       """, baseURL + "/posts/1" ),
		    context );

		assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );

		IStruct bxhttp = variables.getAsStruct( result );
		assertThat( bxhttp.get( Key.statusCode ) ).isEqualTo( "200 OK" );
		assertThat( bxhttp.get( Key.status_code ) ).isEqualTo( 200 );
		assertThat( bxhttp.get( Key.statusText ) ).isEqualTo( "OK" );
		assertThat( bxhttp.getAsString( Key.fileContent ).replaceAll( "\\s+", "" ) ).isEqualTo( "Done" );
	}

}
