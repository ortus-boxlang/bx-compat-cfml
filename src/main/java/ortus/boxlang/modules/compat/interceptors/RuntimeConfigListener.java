package ortus.boxlang.modules.compat.interceptors;

import ortus.boxlang.modules.compat.util.BlowFishEasy;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

/**
 * This interceptor is used to convert null values to empty strings in query results
 * for CFML compatibility.
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.28.0
 */
public class RuntimeConfigListener extends BaseInterceptor {

	private static final String LUCEE_ENCRYPTION_KEY = "sdfsdfs";

	/**
	 * Listen for onDatasourceConfigLoad and decrypt any encrypted datasource passwords.
	 *
	 * Incoming data:
	 * - name : The datasource name
	 * - properties : Datasource configuration struct
	 *
	 * @param interceptData
	 */
	@InterceptionPoint
	public void onDatasourceConfigLoad( IStruct interceptData ) {
		String	datasourceName		= interceptData.getAsString( Key._name );
		IStruct	datasourceConfig	= interceptData.getAsStruct( Key.properties );

		decryptDatasourcePasswords( datasourceName, datasourceConfig );
	}

	/**
	 * Perform the decryption of any encrypted passwords in the datasource config.
	 * If the password is not encrypted, it is left as-is.
	 * If the password begins with "encrypted:" but the decryption fails, an exception is thrown.
	 * 
	 * @param datasourceName   Datasource name for error reporting
	 * @param datasourceConfig Datasource configuration struct - the `password` value will be modified in cases of successful decryption
	 */
	private void decryptDatasourcePasswords( String datasourceName, IStruct datasourceConfig ) {
		String password = datasourceConfig.getAsString( Key.password );
		if ( password != null && password.startsWith( "encrypted:" ) ) {
			// Decrypt the password
			String decryptedPassword = new BlowFishEasy( LUCEE_ENCRYPTION_KEY )
			    .decryptString( password.substring( "encrypted:".length() ) );
			if ( decryptedPassword == null ) {
				throw new BoxRuntimeException( String.format( "Failed to decrypt datasource password on [%s]", datasourceName ) );
			}
			datasourceConfig.put( Key.password, decryptedPassword );
		}
	}
}
