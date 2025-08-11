package ortus.boxlang.modules.compat.interceptors;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.IStruct;

public class RuntimeConfigListenerTest extends BaseIntegrationTest {

	@DisplayName( "It decrypts encrypted passwords" )
	@Test
	public void testDatasourcePasswordDecryption() {
		IStruct eventData = Struct.of(
		    Key._name, "testDatasource1",
		    Key.datasource, Struct.of(
		        Key.username, "testUser",
		        Key.password, "encrypted:d3522dc6a54f43af66386db4d7392d24687cbddef0255617386fdb776b704176"
		    )
		);
		runtime.announce( "onDatasourceConfigLoad", eventData );
		assertThat( eventData.getAsStruct( Key.datasource ).getAsString( Key.password ) ).isEqualTo( "password123" );
	}

	@DisplayName( "It ignores unencrypted passwords" )
	@Test
	public void testDatasourcePasswordIgnore() {
		IStruct eventData = Struct.of(
		    Key._name, "testDatasource2",
		    Key.datasource, Struct.of(
		        Key.username, "testUser",
		        Key.password, "isnotencrypted"
		    )
		);
		runtime.announce( "onDatasourceConfigLoad", eventData );

		assertThat( eventData.getAsStruct( Key.datasource ).getAsString( Key.password ) ).isEqualTo( "isnotencrypted" );
	}

	@DisplayName( "It throws on invalid encryption values" )
	@Test
	public void testDatasourcePasswordFailedDecryption() {
		IStruct eventData = Struct.of(
		    Key._name, "testDatasource3",
		    Key.datasource, Struct.of(
		        Key.username, "testUser",
		        Key.password, "encrypted:xxx"
		    )
		);

		assertThrows( BoxRuntimeException.class, () -> {
			runtime.announce( "onDatasourceConfigLoad", eventData );
		}, "Failed to decrypt datasource password on [testDatasource]" );
	}

}
