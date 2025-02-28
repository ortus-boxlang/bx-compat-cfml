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
package ortus.boxlang.modules.compat.bifs.system;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;

@BoxBIF
public class Throw extends ortus.boxlang.runtime.bifs.global.system.Throw {

	/**
	 * Constructor
	 */
	public Throw() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( false, "any", Key.message ),
		    new Argument( false, "String", Key.type, "Application" ),
		    new Argument( false, "String", Key.detail ),
		    new Argument( false, "String", Key.errorcode ),
		    new Argument( false, "any", Key.extendedinfo ),
		    new Argument( false, "Throwable", Key.object )
		};
	}

}
