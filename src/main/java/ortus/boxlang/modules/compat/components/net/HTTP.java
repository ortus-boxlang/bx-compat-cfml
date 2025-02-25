package ortus.boxlang.modules.compat.components.net;

import ortus.boxlang.runtime.components.BoxComponent;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.ExpressionInterpreter;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.dynamic.casters.StructCaster;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

@BoxComponent( allowsBody = true )
public class HTTP extends ortus.boxlang.runtime.components.net.HTTP {

	/**
	 * Overload to the core component to supply compatibility
	 *
	 * @param context        The context in which the Component is being invoked
	 * @param attributes     The attributes to the Component
	 * @param body           The body of the Component
	 * @param executionState The execution state of the Component
	 *
	 */
	public BodyResult _invoke( IBoxContext context, IStruct attributes, ComponentBody body, IStruct executionState ) {
		String		variableName	= attributes.getAsString( Key.result );
		BodyResult	returnedBody	= super._invoke( context, attributes, body, executionState );

		Object		httpObject		= ExpressionInterpreter.getVariable( context, variableName, true );

		if ( httpObject != null ) {
			IStruct	result				= StructCaster.cast( httpObject );
			// Add our status text to the statusCode
			Object	statusCodeReturned	= result.get( Key.statusCode );
			if ( statusCodeReturned != null ) {
				String	statusCode	= StringCaster.cast( statusCodeReturned );
				String	statusText	= result.getAsString( Key.statusText );
				result.put( Key.statusCode, statusCode + " " + statusText );
			}
		}

		return returnedBody;

	}

}