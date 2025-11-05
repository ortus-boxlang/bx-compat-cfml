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

import java.nio.file.Path;
import java.nio.file.Paths;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.modules.compat.util.SettingsUtil;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.loader.ClassLocator;
import ortus.boxlang.runtime.runnables.IClassRunnable;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.util.FileSystemUtil;

/**
 * Listens to when the runtime starts
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.0.0
 */
public class ServerStartListener extends BaseInterceptor {

	private static final String tempMappingName = "bx_server_start_auto_generated_mapping";

	/**
	 * Intercept the Runtime start
	 *
	 * @param interceptData Nothing here
	 */
	@InterceptionPoint
	public void onRuntimeStart( IStruct interceptData ) {
		// In our test suite, the interceptor gets picked up from the class path befor the module is actually loaded
		if ( !getRuntime().getModuleService().hasModule( KeyDictionary.moduleName ) ) {
			return;
		}

		if ( BooleanCaster.cast( SettingsUtil.getSetting( KeyDictionary.serverStartEnabled, false ) ) ) {
			String path = StringCaster.cast( SettingsUtil.getSetting( KeyDictionary.serverStartPath, "" ) ).trim();
			if ( !path.isEmpty() ) {
				IBoxContext	context		= getRuntime().getRuntimeContext();
				String		dotPath		= path;
				String		lowerPath	= path.toLowerCase();

				// if this is a file path, create a mapping on-the-fly for it and turn it into a dot path
				if ( path.contains( "/" ) || path.contains( "\\" ) || lowerPath.endsWith( ".bx" ) || lowerPath.endsWith( ".cfc" ) ) {
					Path pathPath = Paths.get( path );
					if ( !pathPath.isAbsolute() ) {
						pathPath = FileSystemUtil.expandPath( context, path ).absolutePath();
					}
					// Let's blow up if the file doesn't exist
					if ( !pathPath.toFile().exists() ) {
						throw new RuntimeException( "Server Start Path file does not exist: " + pathPath.toString() );
					}
					// separate out the filename and the folder it lives in
					String	filename	= pathPath.getFileName().toString();
					Path	folderPath	= pathPath.getParent();

					// Add an on-the-fly mapping for this folder since class creation requires a mapping
					getRuntime().getConfiguration().registerMapping( "/" + tempMappingName, folderPath, false );
					dotPath = tempMappingName + "." + filename.substring( 0, filename.lastIndexOf( '.' ) );
				}

				// Assert, now the dotPath is populated

				// Load up our serve start class and instantiate it
				IClassRunnable serverStartClass = ( IClassRunnable ) getRuntime()
				    .getClassLocator()
				    .load(
				        context,
				        dotPath,
				        ClassLocator.BX_PREFIX,
				        true
				    )
				    .invokeConstructor( context, Key.noInit )
				    .unWrapBoxLangClass();

				// Fire it! If the method doesn't exist, this will blow up which I prefer.
				serverStartClass.dereferenceAndInvoke( context, KeyDictionary.onServerStart, new Object[] {}, false );

				// Clean up our temp mapping
				getRuntime().getConfiguration().unregisterMapping( "/" + tempMappingName );
			}
		}
	}

}
