package ortus.boxlang.modules.compat.interceptors;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Query;

public class QueryListener extends BaseInterceptor {

	BoxRuntime runtime = BoxRuntime.getInstance();

	@InterceptionPoint
	public void onQueryBuild( IStruct interceptData ) {

	}

	/**
	 * Modify the query results before they are returned to the calling code.
	 *
	 * This is where we handle CFML compatibility features at the data level, such as:
	 *
	 * - converting null values to empty strings [BL-164](https://ortussolutions.atlassian.net/browse/BL-164)
	 * - converting time values to the time specified in the `timezone` query option [BL-116](https://ortussolutions.atlassian.net/browse/BL-116)
	 *
	 * Incoming data:
	 * - sql : The original, unmodified SQL string,
	 * - bindings : Parameter binding values,
	 * - executionTime : The query execution time,
	 * - data : The query results,
	 * - result : The return value from the statement execution.
	 * - pendingQuery : The BoxLang PendingQuery instance -
	 * https://s3.amazonaws.com/apidocs.ortussolutions.com/boxlang/1.0.0/ortus/boxlang/runtime/jdbc/PendingQuery.html
	 * - executedQuery : The BoxLang ExecutedQuery instance -
	 * https://s3.amazonaws.com/apidocs.ortussolutions.com/boxlang/1.0.0/ortus/boxlang/runtime/jdbc/ExecutedQuery.html
	 */
	@InterceptionPoint
	public void postQueryExecute( IStruct interceptData ) {
		IStruct	moduleSettings	= BoxRuntime.getInstance().getModuleService().getModuleSettings( KeyDictionary.moduleName );
		Boolean	nullToEmpty		= BooleanCaster.cast( moduleSettings.getOrDefault( KeyDictionary.queryNullToEmpty, false ) );

		if ( !nullToEmpty ) {
			return;
		}

		Query results = interceptData.getAsQuery( Key.data );

		results.intStream().forEach( rowIndex -> {
			Object[] rowData = results.getRow( rowIndex );
			for ( int i = 0; i < rowData.length; i++ ) {
				if ( rowData[ i ] == null ) {
					rowData[ i ] = "";
				}
			}
		} );

	}

}
