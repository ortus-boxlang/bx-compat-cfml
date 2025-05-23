package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.dynamic.casters.StructCaster;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

/**
 * This loads the module and runs an integration test on the module.
 */
public class ServerScopeTest extends BaseIntegrationTest {

	@Test
	@Disabled( "Until we can figure out why the bx interceptions are not firing in the test" )
	public void testServerScope() {
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();
		assertThat( cacheService.hasCache( KeyDictionary.bxClients ) ).isTrue();

		// @formatter:off
		runtime.executeSource(
		    """
				result = server.coldfusion;
			""",
		    context
		);
		// @formatter:on

		assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );
		IStruct server = StructCaster.cast( variables.get( Key.of( "result" ) ) );
		assertThat( server.containsKey( Key.of( "productlevel" ) ) ).isTrue();
		assertThat( server.containsKey( Key.of( "productname" ) ) ).isTrue();
		assertThat( server.containsKey( Key.of( "productversion" ) ) ).isTrue();
		assertThat( server.containsKey( Key.of( "supportedLocales" ) ) ).isTrue();
	}

}
