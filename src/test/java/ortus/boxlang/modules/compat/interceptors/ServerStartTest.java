package ortus.boxlang.modules.compat.interceptors;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.events.BoxEvent;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

/**
 * The runtime is already loaded from the base integration test and the module
 * is loaded AFTER the runtime loads in our test suite
 * So we'll just spoof the interceptor call ourselves
 */
public class ServerStartTest extends BaseIntegrationTest {

	public static boolean serverStarted = false;

	@DisplayName( "It can invoke server start from dot path" )
	@Test
	public void testServerStartDotPath() {
		serverStarted = false;
		IStruct moduleSettings = runtime.getModuleService().getModuleSettings( KeyDictionary.moduleName );
		moduleSettings.put( KeyDictionary.serverStartEnabled, true );
		moduleSettings.put( KeyDictionary.serverStartPath, "src.test.java.ortus.boxlang.modules.compat.interceptors.ServerStartTestClass" );
		runtime.announce( BoxEvent.ON_RUNTIME_START, Struct.EMPTY );
		assertThat( serverStarted ).isTrue();
	}

	@DisplayName( "It obeys enabled setting" )
	@Test
	public void testServerStartEnabledSetting() {
		serverStarted = false;
		IStruct moduleSettings = runtime.getModuleService().getModuleSettings( KeyDictionary.moduleName );
		moduleSettings.put( KeyDictionary.serverStartEnabled, false );
		moduleSettings.put( KeyDictionary.serverStartPath, "src.test.java.ortus.boxlang.modules.compat.interceptors.ServerStartTestClass" );
		runtime.announce( BoxEvent.ON_RUNTIME_START, Struct.EMPTY );
		assertThat( serverStarted ).isFalse();
	}

	@DisplayName( "It can invoke server start from absolute file path" )
	@Test
	public void testServerStartAbsoluteFilePath() {
		serverStarted = false;
		IStruct moduleSettings = runtime.getModuleService().getModuleSettings( KeyDictionary.moduleName );
		moduleSettings.put( KeyDictionary.serverStartEnabled, true );
		moduleSettings.put( KeyDictionary.serverStartPath,
		    Paths.get( "src/test/java/ortus/boxlang/modules/compat/interceptors/ServerStartTestClass.bx" ).toAbsolutePath().toString() );
		runtime.announce( BoxEvent.ON_RUNTIME_START, Struct.EMPTY );
		assertThat( serverStarted ).isTrue();
	}

	@DisplayName( "It can invoke server start from relative file path" )
	@Test
	public void testServerStartRelativeFilePath() {
		serverStarted = false;
		IStruct moduleSettings = runtime.getModuleService().getModuleSettings( KeyDictionary.moduleName );
		moduleSettings.put( KeyDictionary.serverStartEnabled, true );
		moduleSettings.put( KeyDictionary.serverStartPath, "src/test/java/ortus/boxlang/modules/compat/interceptors/ServerStartTestClass.bx" );
		runtime.announce( BoxEvent.ON_RUNTIME_START, Struct.EMPTY );
		assertThat( serverStarted ).isTrue();
	}

	@DisplayName( "It errors on class missing the method" )
	@Test
	public void testServerStartClassMissingMethod() {
		serverStarted = false;
		IStruct moduleSettings = runtime.getModuleService().getModuleSettings( KeyDictionary.moduleName );
		moduleSettings.put( KeyDictionary.serverStartEnabled, true );
		moduleSettings.put( KeyDictionary.serverStartPath, "src.test.java.ortus.boxlang.modules.compat.interceptors.InvalidServerStartTestClass" );
		Throwable t = assertThrows( BoxRuntimeException.class, () -> runtime.announce( BoxEvent.ON_RUNTIME_START, Struct.EMPTY ) );
		assertThat( t.getMessage() ).contains( "Method 'onServerStart' not found" );
		assertThat( serverStarted ).isFalse();
	}

	@DisplayName( "It errors on missing class" )
	@Test
	public void testServerStartMissingClass() {
		serverStarted = false;
		IStruct moduleSettings = runtime.getModuleService().getModuleSettings( KeyDictionary.moduleName );
		moduleSettings.put( KeyDictionary.serverStartEnabled, true );
		moduleSettings.put( KeyDictionary.serverStartPath, "src.test.java.ortus.boxlang.modules.compat.interceptors.sdfsdf" );
		Throwable t = assertThrows( BoxRuntimeException.class, () -> runtime.announce( BoxEvent.ON_RUNTIME_START, Struct.EMPTY ) );
		assertThat( t.getMessage() ).contains( "has not been located" );
		assertThat( serverStarted ).isFalse();

		serverStarted = false;
		moduleSettings.put( KeyDictionary.serverStartEnabled, true );
		moduleSettings.put( KeyDictionary.serverStartPath,
		    Paths.get( "src/test/java/ortus/boxlang/modules/compat/interceptors/sdfsdf.bx" ).toAbsolutePath().toString() );
		t = assertThrows( BoxRuntimeException.class, () -> runtime.announce( BoxEvent.ON_RUNTIME_START, Struct.EMPTY ) );
		assertThat( t.getMessage() ).contains( "Server Start Path file does not exist" );
		assertThat( serverStarted ).isFalse();
	}

}
