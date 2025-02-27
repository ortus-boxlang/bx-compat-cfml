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
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.DateTimeCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.DateTime;

@BoxMember( type = BoxLangType.DATETIME, name = "equals" )
public class DateEquality extends BIF {

	/**
	 * Constructor
	 */
	public DateEquality() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.date1 ),
		    new Argument( true, "any", Key.date2 )
		};
	}

	/**
	 * Provides an overload to the native `equals` method for comparing two date/time values
	 *
	 * This method providers a looser comparison than the native `equals` method, which also adds timezone comparison, in addition to the instant
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.date1 The initial date
	 * 
	 * @argument.date2 The date for comparison
	 *
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		DateTime	dateOne	= DateTimeCaster.cast( arguments.get( Key.date1 ) );
		DateTime	dateTwo	= DateTimeCaster.cast( arguments.get( Key.date2 ) );
		return dateOne.isEqual( dateTwo );
	}
}
