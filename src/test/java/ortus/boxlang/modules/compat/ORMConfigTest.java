package ortus.boxlang.modules.compat;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.services.InterceptorService;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

public class ORMConfigTest extends BaseIntegrationTest {

	@Test
	public void testORMConfigDefaults() {
		InterceptorService interceptorService = runtime.getInterceptorService();
		interceptorService.registerInterceptionPoint( KeyDictionary.EVENT_ORM_PRE_CONFIG_LOAD );

		// the wind-up
		IStruct properties = new Struct();
		properties.put( KeyDictionary.cfclocation, "models/orm" );

		// and the pitch...
		interceptorService.announce( KeyDictionary.EVENT_ORM_PRE_CONFIG_LOAD, Struct.of(
		    Key.properties, properties,
		    "context", context
		) );

		assertThat( properties ).containsKey( KeyDictionary.autoManageSession );
		assertThat( properties ).containsKey( KeyDictionary.flushAtRequestEnd );
		assertThat( properties ).containsKey( KeyDictionary.ignoreParseErrors );
		assertThat( properties ).containsKey( KeyDictionary.entityPaths );

		assertThat( properties.getAsBoolean( KeyDictionary.autoManageSession ) ).isTrue();
		assertThat( properties.getAsBoolean( KeyDictionary.flushAtRequestEnd ) ).isTrue();
		assertThat( properties.getAsBoolean( KeyDictionary.ignoreParseErrors ) ).isTrue();
		assertThat( properties.get( KeyDictionary.entityPaths ) ).isEqualTo( "models/orm" );
	}
}
