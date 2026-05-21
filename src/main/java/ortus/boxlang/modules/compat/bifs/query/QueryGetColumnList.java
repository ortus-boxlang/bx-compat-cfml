package ortus.boxlang.modules.compat.bifs.query;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.QUERY )
public class QueryGetColumnList extends BIF {

	/**
	 * Constructor
	 */
	public QueryGetColumnList() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "query", Key.query )
		};
	}

	/**
	 * Gets array of column names from query. This overrides the baked in Java method of the same name
	 * in our Query object which returns a string list of column names. This is only for CF compat.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.query The query object from which to extract column names.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		return arguments.getAsQuery( Key.query ).getColumnArray();
	}

}
