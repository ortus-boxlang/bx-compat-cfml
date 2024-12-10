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

package ortus.boxlang.modules.compat.bifs.struct;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

public class StructGetTest extends BaseIntegrationTest {

	@DisplayName( "Tests that the compat behavior will mutate the struct return an empty struct" )
	@Test
	public void testMutation() {
		runtime.executeSource(
		    """
		    	ref = {};
		      	result = structGet( "ref.a.b.c" );
		    """,
		    context );

		assertTrue( variables.get( result ) instanceof IStruct );
		IStruct ref = variables.getAsStruct( Key.of( "ref" ) );
		assertTrue( ref.containsKey( Key.of( "a" ) ) );
		assertTrue( ref.get( Key.of( "a" ) ) instanceof IStruct );
		assertTrue( ref.getAsStruct( Key.of( "a" ) ).containsKey( Key.of( "b" ) ) );
		assertTrue( ref.getAsStruct( Key.of( "a" ) ).getAsStruct( Key.of( "b" ) ).containsKey( Key.of( "c" ) ) );
		assertEquals( variables.get( result ), ref.getAsStruct( Key.of( "a" ) ).getAsStruct( Key.of( "b" ) ).get( Key.of( "c" ) ) );
	}

	@DisplayName( "It tests the BIF StructGet Will return a empty struct if a value is not present" )
	@Test
	public void testBifNullReturn() {
		runtime.executeSource(
		    """
		    myStruct={
		    	"foo" : {
		    		"bar" : "baz"
		    	}
		    };
		    result = StructGet( "myStruct.foo.blah.blerge" );
		    """,
		    context );
		IStruct rStruct = variables.getAsStruct( result );
		assertThat( rStruct.size() ).isEqualTo( 0 );
		// Test dumb adobe mutations
		IStruct myStruct = variables.getAsStruct( Key.of( "myStruct" ) );
		System.out.println( myStruct );
		assertTrue( myStruct.containsKey( Key.of( "foo" ) ) );
		assertTrue( myStruct.getAsStruct( Key.of( "foo" ) ).containsKey( Key.of( "blah" ) ) );
		assertTrue(
		    myStruct
		        .getAsStruct( Key.of( "foo" ) )
		        .getAsStruct( Key.of( "blah" ) )
		        .containsKey( Key.of( "blerge" ) )
		);

	}

	@DisplayName( "Returns an empty struct if the root value used is NOT a struct or not found" )
	@Test
	public void testRootValueNotStruct() {
		runtime.executeSource(
		    """
		    result = StructGet( "myStruct.foo.bar.baz" );
		    """,
		    context );
		IStruct rStruct = variables.getAsStruct( result );
		assertThat( rStruct.size() ).isEqualTo( 0 );
	}

}
