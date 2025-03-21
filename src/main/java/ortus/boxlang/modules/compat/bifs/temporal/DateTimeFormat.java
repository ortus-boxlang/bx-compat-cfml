package ortus.boxlang.modules.compat.bifs.temporal;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;

@BoxBIF
@BoxBIF( alias = "DateFormat" )
@BoxBIF( alias = "TimeFormat" )
public class DateTimeFormat extends ortus.boxlang.runtime.bifs.global.temporal.DateTimeFormat {

	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		// Lucee and ACF will accept a empty string and return an empty string...
		Object formattable = arguments.get( Key.date );
		if ( formattable instanceof String && StringCaster.cast( formattable ).trim().isEmpty() ) {
			return formattable;
		}
		return super._invoke( context, arguments );

	}

}
