package ortus.boxlang.modules.compat.bifs.temporal;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.DATETIME, name = "compare" )
@BoxMember( type = BoxLangType.DATETIME, name = "compareTo" )
public class DateCompare extends ortus.boxlang.runtime.bifs.global.temporal.DateCompare {

	/**
	 * Constructor
	 */
	public DateCompare() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.date1 ),
		    new Argument( true, "any", Key.date2 ),
		    new Argument( false, "string", Key.datepart, "s" )
		};
	}

}
