/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ortus.boxlang.modules.compat.bifs.conversion;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class JSONSerializeTest extends BaseIntegrationTest {

	@DisplayName( "Queries serialize as row by default" )
	@Test
	public void testQueriesSerializeAsRowByDefault() {
		runtime.executeSource(
		    """
		       myQuery = queryNew( "col1", "varchar", { col1: "Grant" } );
		    result = JSONSerialize( myQuery )
		       """,
		    context );
		String myResult = variables.getAsString( result );
		assertThat( myResult ).startsWith( "{" );
		assertThat( myResult ).contains( "\"COLUMNS\"" );
		assertThat( myResult ).contains( "\"COL1\"" );
		assertThat( myResult ).contains( "\"DATA\"" );
		assertThat( myResult ).contains( "\"Grant\"" );
	}

	@DisplayName( "Query toJSON row mode uppercases columns" )
	@Test
	public void testQueryToJSONRowUppercaseColumns() {
		runtime.executeSource(
		    """
		       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "row" )
		    result = q
		       """,
		    context );

		String myResult = variables.getAsString( result );
		assertThat( myResult ).isEqualTo( "{\"COLUMNS\":[\"COL1\",\"COL2\",\"COLUMN3\"],\"DATA\":[[\"brad\",\"luis\",\"jon\"]]}" );
	}

	@DisplayName( "Query toJSON column mode uppercases columns" )
	@Test
	public void testQueryToJSONColumnUppercaseColumns() {
		runtime.executeSource(
		    """
		       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "column" )
		    result = q
		       """,
		    context );

		String myResult = variables.getAsString( result );
		assertThat( myResult ).isEqualTo(
		    "{\"ROWCOUNT\":1,\"COLUMNS\":[\"COL1\",\"COL2\",\"COLUMN3\"],\"DATA\":{\"COL1\":[\"brad\"],\"COL2\":[\"luis\"],\"COLUMN3\":[\"jon\"]}}" );
	}

	@DisplayName( "Query toJSON struct mode uppercases keys" )
	@Test
	public void testQueryToJSONStructUppercaseColumns() {
		runtime.executeSource(
		    """
		       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "struct" )
		    result = q
		       """,
		    context );

		String myResult = variables.getAsString( result );
		assertThat( myResult ).isEqualTo( "[{\"COL1\":\"brad\",\"COL2\":\"luis\",\"COLUMN3\":\"jon\"}]" );
	}
}
