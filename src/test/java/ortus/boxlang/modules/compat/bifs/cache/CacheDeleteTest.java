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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

public class CacheDeleteTest extends BaseCacheTest {

	@Test
	@DisplayName( "Can delete a key in the default cache" )
	public void candeleteDefaultCache() {
		runtime.executeSource(
		    """
		    result = cachedelete( "bdd" );
		    """,
		    context );

		assertThat( boxCache.lookup( "bdd" ) ).isFalse();
	}

	@Test
	@DisplayName( "Can delete a key in a specific cache" )
	public void candeleteSpecificCache() {
		runtime.executeSource(
		    """
		    result = cachedelete( "bdd", false, "default" );
		    """,
		    context );

		assertThat( boxCache.lookup( "bdd" ) ).isFalse();
	}

	@Test
	@DisplayName( "Can throw exception for a bogus key with throwOnError" )
	public void canThrowException() {
		assertThrows( BoxRuntimeException.class, () -> {
			runtime.executeSource(
			    """
			    result = cachedelete( "bogus", true );
			    """,
			    context );
		} );
	}

	@Test
	@DisplayName( "Will ignore exception for a bogus key without throwOnError" )
	public void willIgnoreException() {
		runtime.executeSource(
		    """
		    result = cachedelete( "bogus" );
		    """,
		    context );

		assertThat( boxCache.lookup( "bogus" ) ).isFalse();
	}

}
