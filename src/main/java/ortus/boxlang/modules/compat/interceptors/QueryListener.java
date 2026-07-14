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
import ortus.boxlang.modules.compat.util.SettingsUtil;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.events.BaseInterceptor;
import ortus.boxlang.runtime.events.InterceptionPoint;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Array;
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
		Boolean nullToEmpty = BooleanCaster.cast( SettingsUtil.getSetting( KeyDictionary.queryNullToEmpty, false ) );

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
		Boolean nullToEmpty = BooleanCaster.cast( SettingsUtil.getSetting( KeyDictionary.queryNullToEmpty, false ) );

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

	/**
	 * Listen for queries being serialized, and upper case the keys in the structs, if needed
	 *
	 * Incoming data:
	 * - query : The query object data which is about to be serialized.
	 *
	 * @param interceptData
	 */
	@InterceptionPoint
	public void onJSONQuerySerialize( IStruct interceptData ) {
		Boolean upperCaseKeys = BooleanCaster
		    .attempt( ( ( IStruct ) SettingsUtil.getSetting( KeyDictionary.transpiler, Struct.EMPTY ) ).get( KeyDictionary.upperCaseKeys ) )
		    .getOrDefault( false );
		System.out.println( "[QueryListener.onJSONQuerySerialize] entered; upperCaseKeys=" + upperCaseKeys + ", interceptKeys=" + interceptData.keySet() );
		if ( !upperCaseKeys ) {
			System.out.println( "[QueryListener.onJSONQuerySerialize] exiting early; upperCaseKeys=false" );
			return;
		}

		Object data = interceptData.get( Key.data );
		System.out.println(
		    "[QueryListener.onJSONQuerySerialize] dataType=" + ( data == null ? "null" : data.getClass().getName() ) + ", isStruct="
		        + ( data instanceof IStruct )
		        + ", isArray=" + ( data instanceof Array ) );
		// Upper case all top level keys of the struct
		if ( data instanceof IStruct sData ) {
			System.out.println( "[QueryListener.onJSONQuerySerialize] struct branch entered; keys(before)=" + sData.keySet() );
			// This applies to the outer keys of both row and column formats:
			// {"ROWCOUNT":1,"COLUMNS":["COL1","COL2","COLUMN3"],"DATA":{"COL1":["brad"],"COL2":["luis"],"COLUMN3":["jon"]}}
			// {"COLUMNS":["COL1","COL2","COLUMN3"],"DATA":[["brad","luis","jon"]]}
			Key[] keys = sData.keySet().toArray( new Key[ 0 ] );
			for ( Key key : keys ) {
				Object rowObj = sData.get( key );
				sData.remove( key );
				sData.put( Key.of( key.toString().toUpperCase() ), rowObj );
			}
			System.out.println( "[QueryListener.onJSONQuerySerialize] struct keys uppercased; keys(after)=" + sData.keySet() );
			// if data looks like this, then also upper case column names
			// {"COLUMNS":["COL1","COL2","COLUMN3"],"DATA":[["brad","luis","jon"]]}
			if ( sData.containsKey( Key.columns ) ) {
				Object columnsObj = sData.get( Key.columns );
				System.out.println(
				    "[QueryListener.onJSONQuerySerialize] columns branch entered; columnsType="
				        + ( columnsObj == null ? "null" : columnsObj.getClass().getName() ) );
				// Applies to row/column query JSON where COLUMNS may come through as a native String[]:
				// {"COLUMNS":["col1","COL2","CoLuMn3"],"DATA":[["brad","luis","jon"]]}
				// {"ROWCOUNT":1,"COLUMNS":["col1","COL2","CoLuMn3"],"DATA":{"col1":["brad"],"COL2":["luis"],"CoLuMn3":["jon"]}}
				Array columnsArray = null;
				if ( columnsObj instanceof Array castedColumnsArray ) {
					columnsArray = castedColumnsArray;
					System.out.println( "[QueryListener.onJSONQuerySerialize] columns recognized as BoxLang Array; size=" + columnsArray.size() );
				} else if ( columnsObj instanceof String[] nativeColumnsArray ) {
					columnsArray = new Array( nativeColumnsArray );
					System.out.println( "[QueryListener.onJSONQuerySerialize] columns recognized as native String[]; size=" + nativeColumnsArray.length );
				}

				if ( columnsArray != null ) {
					System.out.println( "[QueryListener.onJSONQuerySerialize] columns before uppercase=" + columnsArray );
					for ( int i = 0; i < columnsArray.size(); i++ ) {
						Object colNameObj = columnsArray.get( i );
						if ( colNameObj instanceof String colName ) {
							columnsArray.set( i, colName.toUpperCase() );
						}
					}
					System.out.println( "[QueryListener.onJSONQuerySerialize] columns after uppercase=" + columnsArray );
				} else {
					System.out.println( "[QueryListener.onJSONQuerySerialize] columns branch had unsupported type; no mutation" );
				}
			} else {
				System.out.println( "[QueryListener.onJSONQuerySerialize] columns branch skipped; COLUMNS key not present" );
			}

			// For column format, DATA is a struct keyed by column names:
			// {"ROWCOUNT":1,"COLUMNS":["col1","COL2","CoLuMn3"],"DATA":{"col1":["brad"],"COL2":["luis"],"CoLuMn3":["jon"]}}
			if ( sData.containsKey( Key.data ) && sData.get( Key.data ) instanceof IStruct sColumnData ) {
				System.out.println( "[QueryListener.onJSONQuerySerialize] column DATA branch entered; dataKeys(before)=" + sColumnData.keySet() );
				Key[] dataKeys = sColumnData.keySet().toArray( new Key[ 0 ] );
				for ( Key dataKey : dataKeys ) {
					Object colData = sColumnData.get( dataKey );
					sColumnData.remove( dataKey );
					sColumnData.put( Key.of( dataKey.getName().toUpperCase() ), colData );
				}
				System.out.println( "[QueryListener.onJSONQuerySerialize] column DATA keys uppercased; dataKeys(after)=" + sColumnData.keySet() );
			} else {
				System.out.println( "[QueryListener.onJSONQuerySerialize] column DATA branch skipped; DATA missing or not IStruct" );
			}
		} else {
			System.out.println( "[QueryListener.onJSONQuerySerialize] struct branch skipped; data is not IStruct" );
		}
		// If data looks like this, fix each struct key to be upper case
		// [{"COL1":"brad","COL2":"luis","COLUMN3":"jon"}]
		// This would perform better if the original struct was built with upper case keys in the first place.
		// We did it this way to keep the core "clean", but we can change this design and put a flagged behavior in the core
		// or add another intercption point to pre-process things like column names that the core announces and we use to influence.
		if ( data instanceof Array aData ) {
			System.out.println( "[QueryListener.onJSONQuerySerialize] array-of-structs branch entered; size=" + aData.size() );
			for ( int i = 0; i < aData.size(); i++ ) {
				Object rowObj = aData.get( i );
				System.out.println(
				    "[QueryListener.onJSONQuerySerialize] row index=" + i + ", rowType=" + ( rowObj == null ? "null" : rowObj.getClass().getName() ) );
				IStruct	sRow	= ( IStruct ) rowObj;
				Key[]	keys	= sRow.keySet().toArray( new Key[ 0 ] );
				for ( Key key : keys ) {
					Object value = sRow.get( key );
					sRow.remove( key );
					sRow.put( Key.of( key.getName().toUpperCase() ), value );
				}
				System.out.println( "[QueryListener.onJSONQuerySerialize] row index=" + i + " keys(after)=" + sRow.keySet() );
			}
		} else {
			System.out.println( "[QueryListener.onJSONQuerySerialize] array-of-structs branch skipped; data is not Array" );
		}
		System.out.println( "[QueryListener.onJSONQuerySerialize] complete" );
	}

}
