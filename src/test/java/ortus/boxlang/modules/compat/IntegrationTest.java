package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.scopes.Key;

/**
 * This loads the module and runs an integration test on the module.
 */
public class IntegrationTest extends BaseIntegrationTest {

	@DisplayName( "Test the module loads in BoxLang" )
	@Test
	public void testModuleLoads() {
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();

		// Verify that the CFIDE mapping was registered
		assertThat(
		    runtime.getConfiguration().hasMapping( Key.of( "CFIDE" ) )
		).isTrue();
	}
}
