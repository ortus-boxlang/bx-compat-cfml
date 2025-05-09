package ortus.boxlang.modules.compat.bifs.temporal;

import ortus.boxlang.modules.compat.interceptors.DateTimeMaskCompat;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.Struct;

@BoxBIF
@BoxMember( type = BoxLangType.STRING_STRICT, name = "parseDateTime" )
@BoxMember( type = BoxLangType.STRING_STRICT, name = "toDateTime" )
public class ParseDateTime extends ortus.boxlang.runtime.bifs.global.temporal.ParseDateTime {

	/**
	 * Parses a locale-specific datetime string or object
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.date the date, datetime string or an object
	 *
	 * @argument.format the format mask to use in parsing
	 *
	 * @argument.timezone the timezone to apply to the parsed datetime
	 *
	 * @argument.locale optional ISO locale string ( e.g. en-US, en_US, es-SA, es_ES, ru-RU, etc ) used to parse localized formats
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		interceptorService.announce(
		    DateTimeMaskCompat.ON_LEGACY_DATE_FORMAT_REQUEST,
		    Struct.of(
		        Key.context, context,
		        Key.arguments, arguments
		    )
		);
		return super._invoke( context, arguments );
	}

}
