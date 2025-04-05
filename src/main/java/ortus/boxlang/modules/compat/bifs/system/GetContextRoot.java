package ortus.boxlang.modules.compat.bifs.system;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;

@BoxBIF
public class GetContextRoot extends ortus.boxlang.runtime.bifs.global.system.GetContextRoot {

	/**
	 * Constructor
	 */
	public GetContextRoot() {
		super();
	}

	/**
	 * Gets the context root. If BoxLang is not running as a servlet-based web server, this always returns "/".
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		String root = StringCaster.cast( super._invoke( context, arguments ) );
		if ( root.substring( 0, 1 ).equals( "/" ) ) {
			return root.substring( 1 );
		} else {
			return root;
		}
	}
}
