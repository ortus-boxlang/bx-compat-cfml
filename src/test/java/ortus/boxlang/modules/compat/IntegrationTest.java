package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This loads the module and runs an integration test on the module.
 */
public class IntegrationTest extends BaseIntegrationTest {

	@DisplayName( "Test the module loads in BoxLang" )
	@Test
	public void testModuleLoads() {
		// Given
		loadModule();

		// Then
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();

		// Verify things got registered
		// assertThat( datasourceService.hasDriver( Key.of( "derby" ) ) ).isTrue();

		// Register a named datasource
		// runtime.getConfiguration().runtime.datasources.put(
		// Key.of( "derby" ),
		// DatasourceConfig.fromStruct( Struct.of(
		// "name", "derby",
		// "driver", "derby",
		// "properties", Struct.of(
		// "database", "testDB",
		// "protocol", "memory"
		// )
		// ) )
		// );

		// @formatter:off
		runtime.executeSource(
		    """
			// Testing code here
			""",
		    context
		);
		// @formatter:on

		// Asserts here

	}
}
