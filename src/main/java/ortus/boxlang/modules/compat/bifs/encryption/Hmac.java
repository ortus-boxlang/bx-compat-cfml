package ortus.boxlang.modules.compat.bifs.encryption;

import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.STRING_STRICT, name = "Hmac" )
public class Hmac extends ortus.boxlang.runtime.bifs.global.encryption.Hmac {

	/**
	 * Creates an algorithmic hash of an object and returns it in the CFML compat upper case format
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.input The item to be hashed
	 *
	 * @argument.algorithm The supported {@link java.security.MessageDigest } algorithm (case-insensitive)
	 *
	 * @argument.encoding Applicable to strings ( default "utf-8" )
	 *
	 * @argument.iterations The number of iterations to re-digest the object ( default 1 );
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		return StringCaster.cast( super._invoke( context, arguments ) ).toUpperCase();
	}

}
