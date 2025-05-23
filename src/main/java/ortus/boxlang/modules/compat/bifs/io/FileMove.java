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

package ortus.boxlang.modules.compat.bifs.io;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;

@BoxBIF

public class FileMove extends ortus.boxlang.runtime.bifs.global.io.FileMove {

	/**
	 * Constructor
	 */
	public FileMove() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, Key.source ),
		    new Argument( true, Argument.STRING, Key.destination ),
		    new Argument( true, Argument.BOOLEAN, Key.overwrite, true )
		};
	}

}
