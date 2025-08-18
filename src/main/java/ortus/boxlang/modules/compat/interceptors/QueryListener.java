/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.modules.compat.interceptors;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Query;
import ortus.boxlang.runtime.types.Struct;

/**
 * This interceptor is used to convert null values to empty strings in query results
 * for CFML compatibility.
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.0.0
 */
public class QueryListener extends BaseInterceptor {

	/**
	 * Seed the module settings
	 */
	private static IStruct MODULE_SETTINGS = Struct.EMPTY;

	/**
	 * Get the module settings, caching them for future use.
	 * 
	 * We use a getter over static initialization to ensure the module is fully initialized before we attempt to access it.
	 *
	 * @return Module settings struct
	 */
	private IStruct getModuleSettings() {
		if ( MODULE_SETTINGS.isEmpty() ) {
			MODULE_SETTINGS = BoxRuntime.getInstance().getModuleService().getModuleSettings( KeyDictionary.moduleName );
		}
		return MODULE_SETTINGS;
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
		Boolean nullToEmpty = BooleanCaster.cast( getModuleSettings().getOrDefault( KeyDictionary.queryNullToEmpty, false ) );

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

	/**
	 * Listen for queryAddRow and manipulate the row data for CFML compatibility.
	 *
	 * Incoming data:
	 * - query : The query object to which the row is being added.
	 * - row : Row of data to be added, whether it be a struct or array.
	 *
	 * @param interceptData
	 */
	@InterceptionPoint
	public void queryAddRow( IStruct interceptData ) {
		Boolean nullToEmpty = BooleanCaster.cast( getModuleSettings().getOrDefault( KeyDictionary.queryNullToEmpty, false ) );

		if ( !nullToEmpty ) {
			return;
		}

		// Query query = interceptData.getAsQuery( Key.query );
		Object[] rowData = ( Object[] ) interceptData.get( Key.row );
		for ( int i = 0; i < rowData.length; i++ ) {
			if ( rowData[ i ] == null ) {
				rowData[ i ] = "";
			}
		}
	}
}
