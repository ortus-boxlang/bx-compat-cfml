/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.modules.compat.interceptors;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.application.BaseApplicationListener;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

/**
 * This interceptor is used to set the disallowed file operation extensions for the application listener.
 * It checks for the Lucee environment variables and the ACF/Lucee application setting.
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.27.0
 *
 * @see <a href="https://docs.lucee.org/reference/functions/getapplicationsettings.html">Lucee Application Settings</a>
 */
public class ApplicationCompatListener extends BaseInterceptor {

	@InterceptionPoint
	public void onApplicationDefined( IStruct data ) {
		BaseApplicationListener	listener	= ( BaseApplicationListener ) data.get( "listener" );
		IStruct					env			= Struct.fromMap( System.getenv() );

		// Extension settings
		// First check for Lucee's environment variables
		if ( env.containsKey( KeyDictionary.LUCEE_UPLOAD_BLOCKLIST ) ) {
			listener
			    .getSettings()
			    .put( Key.disallowedFileOperationExtensions, env.get( KeyDictionary.LUCEE_UPLOAD_BLOCKLIST ) );
		}

		// Then check for the ACF/Lucee application setting
		if ( listener.getSettings().containsKey( KeyDictionary.blockedExtForFileUpload ) ) {
			listener
			    .getSettings()
			    .put(
			        Key.disallowedFileOperationExtensions,
			        listener.getSettings().get( KeyDictionary.blockedExtForFileUpload )
			    );
		}

	}

}
