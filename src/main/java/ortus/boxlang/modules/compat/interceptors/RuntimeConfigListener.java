package ortus.boxlang.modules.compat.interceptors;

import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

/**
 * This interceptor is used to convert null values to empty strings in query results
 * for CFML compatibility.
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.28.0
 */
public class RuntimeConfigListener extends BaseInterceptor {

	/**
	 * Listen for onDatasourceConfigLoad and decrypt any encrypted datasource passwords.
	 *
	 * Incoming data:
	 * - name : The datasource name
	 * - datasource : Datasource configuration struct
	 *
	 * @param interceptData
	 */
	@InterceptionPoint
	public void onDatasourceConfigLoad( IStruct interceptData ) {
		String	datasourceName		= interceptData.getAsString( Key._name );
		IStruct	datasourceConfig	= interceptData.getAsStruct( Key.datasource );

		decryptDatasourcePasswords( datasourceConfig );
	}

	private void decryptDatasourcePasswords( IStruct datasourceConfig ) {
		// Implement your decryption logic here
	}
}
