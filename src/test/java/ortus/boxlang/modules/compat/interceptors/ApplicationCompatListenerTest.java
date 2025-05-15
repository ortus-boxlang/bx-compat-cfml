package ortus.boxlang.modules.compat.interceptors;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;
import ortus.boxlang.runtime.util.FileSystemUtil;

public class ApplicationCompatListenerTest extends BaseIntegrationTest {

	static String	good			= "src/test/resources/tmp/ApplicationCompatListenerTest/start.txt";
	static String	bad				= "src/test/resources/tmp/ApplicationCompatListenerTest/end.exe";
	static String	tmpDirectory	= "src/test/resources/tmp/ApplicationCompatListenerTest";

	@DisplayName( "It tests the BIF FileMove security" )
	@Test
	public void testBifSecurity() {
		variables.put( Key.of( "targetFile" ), Path.of( good ).toAbsolutePath().toString() );
		variables.put( Key.of( "destinationFile" ), Path.of( bad ).toAbsolutePath().toString() );
		if ( !FileSystemUtil.exists( tmpDirectory ) ) {
			FileSystemUtil.createDirectory( tmpDirectory );
		}
		try {
			assertThrows(
			    BoxRuntimeException.class,
			    () -> runtime.executeSource(
			        """
			        bx:application name="myApp-with-legacy-fileUploadSetting" blockedExtForFileUpload="exe";
			        fileWrite( targetFile, "blah", true );
			        fileMove( targetFile, "blah.exe" );
			        		""",
			        context )
			);
		} finally {
			FileSystemUtil.deleteDirectory( tmpDirectory, true );
		}
	}

}
