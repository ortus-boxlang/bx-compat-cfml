package ortus.boxlang.modules.compat.interceptors;

import ortus.boxlang.runtime.application.BaseApplicationListener;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

public class ApplicationCompatListener extends BaseInterceptor {

	@InterceptionPoint
	public void onApplicationDefined( IStruct data ) {
		BaseApplicationListener	listener	= ( BaseApplicationListener ) data.get( "listener" );
		IStruct					env			= Struct.fromMap( System.getenv() );

		// Extension settings
		// First check for Lucee's environment variables
		if ( env.containsKey( Key.of( "LUCEE_UPLOAD_BLOCKLIST" ) ) ) {
			listener.updateSettings(
			    Struct.of(
			        "disallowedFileOperationExtensions", env.get( Key.of( "LUCEE_UPLOAD_BLOCKLIST" ) )
			    )
			);
		}
		// Then check for the ACF/Lucee application setting
		if ( listener.getSettings().containsKey( Key.of( "blockedExtForFileUpload" ) ) ) {
			listener.updateSettings( Struct.of(
			    "disallowedFileOperationExtensions", listener.getSettings().get( Key.of( "blockedExtForFileUpload" ) )
			) );
		}

	}

}
