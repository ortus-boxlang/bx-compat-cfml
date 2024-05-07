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
import ortus.boxlang.runtime.types.IStruct;

public class CacheGetPropertiesTest extends BaseCacheTest {

	@Test
	@DisplayName( "Can get the default cache properties" )
	public void canGetCacheProperties() {
		runtime.executeSource(
		    """
		    result = CacheGetProperties();
		    """,
		    context );
		Array data = variables.getAsArray( result );
		assertThat( data.size() ).isGreaterThan( 0 );
	}

	@Test
	@DisplayName( "Can get the specific cache properties" )
	public void canGetSpecificCacheProperties() {
		runtime.executeSource(
		    """
		    result = CacheGetProperties( "default" );
		    """,
		    context );
		IStruct data = variables.getAsStruct( result );
		assertThat( data.get( "name" ) ).isEqualTo( "default" );
	}

}
