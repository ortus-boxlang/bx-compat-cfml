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
package ortus.boxlang.modules.compat.bifs.cache;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CacheIdExistsTest extends BaseCacheTest {

	@Test
	@DisplayName( "Can validate a key exists in the default cache" )
	public void canValidateACacheExists() {
		runtime.executeSource(
		    """
		    result = CacheIdExists( "tdd" );
		    """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( true );
	}

	@Test
	@DisplayName( "Can validate a key exists in a specific cache" )
	public void canValidateACacheExistsInSpecificCache() {
		runtime.executeSource(
		    """
		    result = CacheIdExists( "tdd", "default" );
		    """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( true );
	}

	@Test
	@DisplayName( "Can validate a key does not exist in the default cache" )
	public void canValidateACacheDoesNotExist() {
		runtime.executeSource(
		    """
		    result = CacheIdExists( "doesnotexist" );
		    """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( false );
	}

}
