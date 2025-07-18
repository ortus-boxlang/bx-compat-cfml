package ortus.boxlang.modules.compat.interceptors;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.util.FileSystemUtil;

public class RuntimeConfigListenerTest extends BaseIntegrationTest {

	@DisplayName( "It tests the datasource password decryption" )
	@Test
	public void testDatasourcePasswordDecryption() {
		IStruct eventData = Struct.of(
		    Key._name, "testDatasource",
		    Key.datasource, Struct.of(
		        Key.username, "testUser",
		        Key.password, "isnotencrypted"
		    )
		);
		runtime.announce( "onDatasourceConfigLoad", eventData );

		assertThat( eventData.getAsStruct( Key.datasource ).getAsString( Key.password ) ).isEqualTo( "isnotencrypted" );

		eventData = Struct.of(
		    Key._name, "testDatasource2",
		    Key.datasource, Struct.of(
		        Key.username, "testUser",
		        Key.password, "encrypted:xxx"
		    )
		);
		runtime.announce( "onDatasourceConfigLoad", eventData );
		assertThat( eventData.getAsStruct( Key.datasource ).getAsString( Key.password ) ).isEqualTo( "isDecrypted!" );
	}

}
