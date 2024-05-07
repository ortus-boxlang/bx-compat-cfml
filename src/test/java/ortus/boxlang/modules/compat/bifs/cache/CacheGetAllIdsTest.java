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

import ortus.boxlang.runtime.types.Array;

public class CacheGetAllIdsTest extends BaseCacheTest {

	@Test
	@DisplayName( "Can get all ids from the default cache" )
	public void canGetDefaultCache() {
		runtime.executeSource(
		    """
		    result = cacheGetAllIds();
		    """,
		    context );
		Array results = ( Array ) variables.get( "result" );
		assertThat( results.size() ).isEqualTo( 2 );
	}

	@Test
	@DisplayName( "Can get all ids with a filter and default cache" )
	public void canGetDefaultCacheWithFilter() {
		runtime.executeSource(
		    """
		    result = cacheGetAllIds( "t*" );
		    """,
		    context );
		Array results = ( Array ) variables.get( "result" );
		assertThat( results.size() ).isEqualTo( 1 );
	}

	@Test
	@DisplayName( "Can get all with a filter and specific cache" )
	public void canGetSpecificCacheWithFilter() {
		runtime.executeSource(
		    """
		    result = cacheGetAllIds( "t*", "default" );
		    """,
		    context );
		Array results = ( Array ) variables.get( "result" );
		assertThat( results.size() ).isEqualTo( 1 );
	}

}
