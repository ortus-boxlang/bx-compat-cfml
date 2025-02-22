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

public class CachePutTest extends BaseCacheTest {

	@Test
	@DisplayName( "Can get set a key to the default cache" )
	public void canPutASimpleObject() {
		runtime.executeSource(
		    """
		     cachePut( "foo", "bar" );
		       result = cacheGet( "foo" );
		    cacheClear( "foo" );
		       """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( "bar" );
	}

	@Test
	@DisplayName( "Can set a key to the default cache using a timespan duration" )
	public void canPutDuration() {
		runtime.executeSource(
		    """
		     cachePut( "canPutDuration", "bar", createTimeSpan( 0, 0, 2, 0 ), createTimeSpan( 0, 0, 2, 0 ) );
		       result = cacheGet( "canPutDuration" );
		    cacheClear( "canPutDuration" );
		       """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( "bar" );
	}

	@Test
	@DisplayName( "Can set a key to the default cache using a timespan as decimal days" )
	public void canPutDecimalDays() {
		runtime.executeSource(
		    """
		     cachePut( "canPutDecimalDays", "bar", .5, .5 );
		       result = cacheGet( "canPutDecimalDays" );
		    cacheClear( "canPutDecimalDays" );
		       """,
		    context );

		assertThat( variables.get( "result" ) ).isEqualTo( "bar" );
	}

}
