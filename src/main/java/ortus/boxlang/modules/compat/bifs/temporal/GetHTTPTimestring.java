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
package ortus.boxlang.modules.compat.bifs.temporal;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.DateTimeCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.DateTime;

@BoxBIF
public class GetHTTPTimestring extends BIF {

	/**
	 * Constructor
	 */
	public GetHTTPTimestring() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.date )
		};
	}

	/**
	 * Returns the legacy HTTP TimeString as specified for the now-obsolete RFC 1123/RCF 822.
	 *
	 * This method should be used for legacy compatibility with older CFML engines and is not recommended for new code.
	 * The updated specification, [RFC 2822](https://www.rfc-editor.org/rfc/rfc2822) should be used for new implemenations.
	 *
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.date The date value to format
	 *
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		DateTime dateValue = DateTimeCaster.cast( arguments.get( Key.date ) );
		return dateValue.format( "EEE, dd MMMM HH:mm:ss z" );
	}

}
