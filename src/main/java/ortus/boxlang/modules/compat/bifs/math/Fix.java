package ortus.boxlang.modules.compat.bifs.math;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.NumberCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.NUMERIC )
public class Fix extends ortus.boxlang.runtime.bifs.global.math.Fix {

	/**
	 * Constructor
	 */
	public Fix() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.number )
		};
	}

	/**
	 * Converts a real number to an integer
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.number The number to convert to an integer
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		arguments.put( Key.number, NumberCaster.cast( true, arguments.get( Key.number ) ) );
		return super._invoke( context, arguments );
	}

}
