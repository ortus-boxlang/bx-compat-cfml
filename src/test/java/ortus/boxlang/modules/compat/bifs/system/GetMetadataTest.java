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

package ortus.boxlang.modules.compat.bifs.system;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.types.Struct;

public class GetMetadataTest extends BaseIntegrationTest {

	@DisplayName( "It returns meta for a BXClass" )
	@Test
	public void testItReturnsMetaFunction() {
		runtime.executeSource(
		    """
		    result = getMetadata( new src.test.resources.bx.MyClass() );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Struct.class );
	}

	@DisplayName( "It returns meta for a CFC Class" )
	@Test
	public void testItReturnsMetaClass() {
		runtime.executeSource(
		    """
		    result = getMetadata( new src.test.resources.cf.MyClassCF() );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Struct.class );
	}

	@DisplayName( "It returns meta for function" )
	@Test
	public void testItReturnsFunctionMeta() {
		runtime.executeSource(
		    """
		    result = getMetadata( ()=>{} );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Struct.class );

	}

	@DisplayName( "It returns proper class meta for Java object" )
	@Test
	public void testItReturnsJavaMeta() {

		runtime.executeSource(
		    """
		    result = getMetadata( createObject("java", "java.lang.StringBuilder") ).name;

		    """,
		    context );
		assertThat( variables.getAsString( result ) ).isEqualTo( "java.lang.StringBuilder" );

	}

	@DisplayName( "It returns meta for all other objects" )
	@Test
	public void testItReturnsOtherMeta() {

		runtime.executeSource(
		    """
		    result = getMetadata( {} );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Class.class );

		runtime.executeSource(
		    """
		    result = getMetadata( [] );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Class.class );

		runtime.executeSource(
		    """
		    result = getMetadata( "" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Class.class );

		runtime.executeSource(
		    """
		    result = getMetadata( 42 );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Class.class );

		runtime.executeSource(
		    """
		    result = getMetadata( true );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Class.class );
	}

}
