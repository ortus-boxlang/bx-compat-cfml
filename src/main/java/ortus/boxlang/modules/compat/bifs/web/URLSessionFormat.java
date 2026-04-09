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
package ortus.boxlang.modules.compat.bifs.web;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.types.Argument;

@BoxBIF
public class URLSessionFormat extends BIF {

	/**
	 * Constructor
	 */
	public URLSessionFormat() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.ANY, KeyDictionary.requesturl )
		};
	}

	/**
	 * If the client does not accept cookies appends session identifiers CFID, CFTOKEN and / or JSESSIONID to the URL.
	 * This BIF is just for compat and doesn't actually modify the URL in any way. The old behavior is actually bad security.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.requesturl The request URl to format.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		// NO IMPLEMENTATION. This dummy method is just a pass thru for legacy compat.
		return arguments.get( KeyDictionary.requesturl );
	}

}
