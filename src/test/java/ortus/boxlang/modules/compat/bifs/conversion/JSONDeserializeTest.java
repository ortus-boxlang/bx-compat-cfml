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
import ortus.boxlang.runtime.types.IStruct;

public class JSONDeserializeTest extends BaseIntegrationTest {

	@DisplayName( "It can deserialize single quoted keys" )
	@Test
	public void testCanDeserializeSingleQuoteKey() {
		runtime.executeSource(
		    """
		    result = JSONDeserialize( "{ 'a' : 1, 'b' : 2, 'c' : 3 }" )
		         """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );
	}

	@DisplayName( "It can deserialize Unquoted keys" )
	@Test
	public void testCanDeserializeUnQuoteKey() {
		runtime.executeSource(
		    """
		    result = JSONDeserialize( "{ a : 1, b : 2, c : 3 }" )
		         """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );
	}

	@DisplayName( "It can deserialize JSON with trailing commas" )
	@Test
	public void testCanDeserializeTrailingCommas() {
		runtime.executeSource(
		    """
		    result = JSONDeserialize( '{ "foo" : "bar", }' )
		         """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );
	}

	@DisplayName( "It can deserialize JSON with leading numeric zeroes" )
	@Test
	public void testCanDeserializeLeadingZeroes() {
		runtime.executeSource(
		    """
		    result = JSONDeserialize( '{ "foo" : 01 }' )
		         """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );
	}

}
