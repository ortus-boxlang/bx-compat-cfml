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
package ortus.boxlang.modules.compat.bifs.query;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Query;

public class QuerySetCellTest extends BaseIntegrationTest {

	@DisplayName( "queryNullToEmpty: Coerces empty strings to nulls on non-string columns" )
	@Test
	public void testQueryNullToEmptyCompatSetting() {
		assertThat( Query.queryNullToEmpty ).isTrue();
		runtime.executeSource(
		    """
		    result = queryNew("name,createdDate","string,date");
		    queryAddRow(result);
		    querySetCell(result, "name", "", 1);
		    querySetCell(result, "createdDate", "", 1);
		    """,
		    context
		);

		Query query = variables.getAsQuery( result );
		assertThat( query.getCell( Key.of( "name" ), 0 ) ).isEqualTo( "" );
		assertThat( query.getCell( Key.of( "createdDate" ), 0 ) ).isEqualTo( null );
	}
}
