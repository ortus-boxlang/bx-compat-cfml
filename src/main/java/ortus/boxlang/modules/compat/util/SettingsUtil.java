/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.modules.compat.util;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

/**
 * A utility class for accessing and managing module settings.
 */
public class SettingsUtil {

	/**
	 * Seed the module settings
	 */
	private static IStruct MODULE_SETTINGS = Struct.EMPTY;

	/**
	 * Get the module settings, caching them for future use.
	 *
	 * We use a getter over static initialization to ensure the module is fully initialized before we attempt to access it.
	 *
	 * @return Module settings struct
	 */
	public static IStruct getModuleSettings() {
		if ( MODULE_SETTINGS.isEmpty() ) {
			MODULE_SETTINGS = BoxRuntime.getInstance().getModuleService().getModuleSettings( KeyDictionary.moduleName );
		}
		return MODULE_SETTINGS;
	}

	/**
	 * Get a specific setting from the module settings, returning a default value if the setting is not found.
	 *
	 * @param key          The key of the setting to retrieve.
	 * @param defaultValue The default value to return if the setting is not found.
	 * 
	 * @return The value of the setting, or the default value if not found.
	 */
	public static Object getSetting( Key key, Object defaultValue ) {
		return getModuleSettings().getOrDefault( key, defaultValue );
	}

	/**
	 * Check if the current module compatibility mode is Lucee.
	 *
	 * @return True if the compatibility mode is Lucee, false otherwise.
	 */
	public static Boolean isLucee() {
		return BooleanCaster.cast( getModuleSettings().getOrDefault( KeyDictionary.isLucee, false ) );
	}

	/**
	 * Check if the current module compatibility mode is Adobe ColdFusion.
	 *
	 * @return True if the compatibility mode is Adobe ColdFusion, false otherwise.
	 */
	public static Boolean isAdobe() {
		return BooleanCaster.cast( getModuleSettings().getOrDefault( KeyDictionary.isAdobe, false ) );
	}

}
