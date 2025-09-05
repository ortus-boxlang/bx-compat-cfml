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

package ortus.boxlang.modules.compat.bifs.decision;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class IsJSONTest extends BaseIntegrationTest {

	@DisplayName( "Tests with no quoted keys and lenient parsing disabled" )
	@Test
	void testIsJSONNoQuoteKeys() {
		runtime.executeSource(
		    """
		    result = isJSON( "{ a : 1, b : 2, c : 3 }" );
		    """,
		    context );
		assertThat( variables.getAsBoolean( result ) ).isTrue();
	}

	@DisplayName( "Tests with single quoted keys and lenient parsing disabled" )
	@Test
	void testIsJSONSingleQuoteKeys() {
		runtime.executeSource(
		    """
		    result = isJSON( "{ 'a' : 1, 'b' : 2, 'c' : 3 }" );
		    """,
		    context );
		assertThat( variables.getAsBoolean( result ) ).isTrue();
	}

	@DisplayName( "Tests with trailing commas and lenient parsing disabled" )
	@Test
	void testIsJSONTrailingCommas() {
		runtime.executeSource(
		    """
		    result = isJSON( '{ "foo" : "bar", }' );
		    """,
		    context );
		assertThat( variables.getAsBoolean( result ) ).isTrue();
	}

	@DisplayName( "Tests with leading numeric zeroes and lenient parsing disabled" )
	@Test
	void testIsJSONLeadingZero() {
		runtime.executeSource(
		    """
		    result = isJSON( '{ "foo" : 01 }' );
		    """,
		    context );
		assertThat( variables.getAsBoolean( result ) ).isTrue();
	}

}