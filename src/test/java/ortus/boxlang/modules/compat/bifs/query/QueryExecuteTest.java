/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.modules.compat.bifs.query;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Query;

public class QueryExecuteTest extends BaseIntegrationTest {

	@DisplayName( "It tests that null query values are empty strings" )
	@Test
	void testsNullQueryRowValuesAreStrings() {
		//@formatter:off
		runtime.executeSource(
		    """
		    qryEmployees = queryNew(
		      "name,age,dept,supervisor",
		      "varchar,integer,varchar,varchar",
		      [
		      	["luis",43,"Exec","luis"],
		      	["brad",44,"IT","luis"],
		      	["Jon",nullValue(),"HR","luis"]
		      	]
		      )
		                         q = queryExecute( "
		           SELECT * from qryEmployees where name = 'Jon'
		                ",
		                      	[],
		                      	{ dbType : "query" }
		                      );
		    result = q.age ?: nullValue();
		                      """,
		    context );
		//@formatter:on
		assertThat( variables.get( result ) ).isEqualTo( "" );
	}

	@DisplayName( "It tests that null query values are empty strings" )
	@Test
	void testsQueryNewAddRowColumnDefault() {
		//@formatter:off
		runtime.executeSource(
		    """
			result = queryNew( "alpha,bravo" );
			result.addRow( 1 );

			isColumnNull = isNull( result.alpha );
		                      """,
		    context );
		//@formatter:on

		Query data = variables.getAsQuery( result );
		assertThat( data.size() ).isEqualTo( 1 );

		IStruct row = data.getRowAsStruct( 0 );
		assertThat( row.get( Key.of( "alpha" ) ) ).isEqualTo( "" );
		assertThat( row.get( Key.of( "bravo" ) ) ).isEqualTo( "" );

		assertThat( variables.getAsBoolean( Key.of( "isColumnNull" ) ) ).isFalse();
	}

}
