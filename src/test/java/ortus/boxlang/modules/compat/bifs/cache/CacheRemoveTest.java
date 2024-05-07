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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

public class CacheRemoveTest extends BaseCacheTest {

	@Test
	@DisplayName( "Can get a key from the default cache" )
	public void canGetDefaultCache() {
		runtime.executeSource(
		    """
		    cacheRemove( "tdd" );
		    """,
		    context );

		assertThat( boxCache.lookup( "tdd" ) ).isFalse();
	}

	@Test
	@DisplayName( "Can get a key from a specific cache" )
	public void canGetSpecificCache() {
		runtime.executeSource(
		    """
		    cacheRemove( "tdd", false, "default" );
		    """,
		    context );

		assertThat( boxCache.lookup( "tdd" ) ).isFalse();
	}

	@Test
	@DisplayName( "Can remove an invalid key without throwing an error" )
	public void canRemoveInvalidKey() {
		assertDoesNotThrow( () -> {
			runtime.executeSource(
			    """
			    cacheRemove( "bogus" );
			    """,
			    context );
		} );
	}

	@Test
	@DisplayName( "Can remove an invalid key by throwing an error" )
	public void canRemoveInvalidKeyThrowingError() {
		assertThrows( BoxRuntimeException.class, () -> {
			runtime.executeSource(
			    """
			    cacheRemove( "bogus", true );
			    """,
			    context );
		} );
	}

	@Test
	@DisplayName( "Can remove an array of keys " )
	public void canRemoveArrayOfKeys() {
		runtime.executeSource(
		    """
		    cacheRemove( [ "tdd", "bdd" ] );
		    """,
		    context );

		assertThat( boxCache.lookup( "tdd" ) ).isFalse();
		assertThat( boxCache.lookup( "bdd" ) ).isFalse();
	}

}
