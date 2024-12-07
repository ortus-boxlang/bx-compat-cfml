package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.runtime.context.ClientScope;
import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.scopes.ApplicationScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Array;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.util.ListUtil;

/**
 * This loads the module and runs an integration test on the module.
 */
public class ClientScopeTest extends BaseIntegrationTest {

	@Test
	public void testClientScope() {
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();
		assertThat( cacheService.hasCache( KeyDictionary.bxClients ) ).isTrue();

		// @formatter:off
		runtime.executeSource(
		    """
				application name="myApp-with-client-scope-on" clientManagement="true";

				result = application;
				result2 = client;
				startTime = ApplicationStartTime()
			""",
		    context
		);
		// @formatter:on

		assertThat( variables.get( result ) ).isInstanceOf( ApplicationScope.class );
		assertThat( variables.get( Key.of( "result2" ) ).getClass().getName() ).isEqualTo( ClientScope.class.getName() );
		IStruct client = ( IStruct ) variables.get( Key.of( "result2" ) );
		assertThat( client.containsKey( Key.cfid ) ).isTrue();
		assertThat( client.containsKey( Key.cftoken ) ).isTrue();
		assertThat( client.containsKey( Key.urlToken ) ).isTrue();
		assertThat( client.containsKey( KeyDictionary.hitCount ) ).isTrue();
		assertThat( client.containsKey( Key.lastVisit ) ).isTrue();
		assertThat( client.containsKey( Key.timeCreated ) ).isTrue();
	}

	@Test
	public void testGetClientVariablesList() {
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();
		assertThat( cacheService.hasCache( KeyDictionary.bxClients ) ).isTrue();

		// @formatter:off
		runtime.executeSource(
		    """
				application name="myApp-with-client-scope-variables" clientManagement="true";

				client.foo = "bar";
				result = getClientVariablesList();
			""",
		    context
		);
		// @formatter:on

		String clientVariablesList = ( String ) variables.get( result );
		assertThat( clientVariablesList ).isInstanceOf( String.class );
		Array clientVariables = ListUtil.asList( clientVariablesList, "," );
		assertThat( clientVariables.size() ).isEqualTo( 1 );
		assertThat( clientVariables.get( 0 ) ).isEqualTo( "foo" );
	}
}
