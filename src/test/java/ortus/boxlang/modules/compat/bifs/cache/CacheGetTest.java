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

public class CacheGetTest extends BaseCacheTest {

	@Test
	@DisplayName( "Can get a key from the default cache" )
	public void canGetDefaultCache() {
		runtime.executeSource(
		    """
		    result = cacheGet( "tdd" );
		    """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( "rocks" );
	}

	@Test
	@DisplayName( "Can get a key from a specific default cache" )
	public void canGetSpecificCache() {
		runtime.executeSource(
		    """
		    result = cacheGet( "tdd", "default" );
		    """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( "rocks" );
	}

	@Test
	@DisplayName( "Can get a null from an invalid cache key" )
	public void canGetNullFromInvalidCacheKey() {
		runtime.executeSource(
		    """
		    result = cacheGet( "invalid" );
		    """,
		    context );

		assertThat( variables.get( "result" ) ).isNull();
	}

}
