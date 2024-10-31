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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.types.IStruct;

public class GetTagDataTest {

	static BoxRuntime	instance;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= new Key( "result" );

	@BeforeAll
	public static void setUp() {
		instance = BoxRuntime.getInstance( true );
	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext( instance.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@DisplayName( "It returns tag data" )
	@Test
	public void testBIF() {
		instance.executeSource(
		    """
		       result = getTagData( "cf", "dbinfo" )
		    println( result )
		       """,
		    context );
		IStruct data = variables.getAsStruct( result );
		assertThat( data.get( "nameSpaceSeperator" ) ).isEqualTo( "" );
		assertThat( data.get( "nameSpace" ) ).isEqualTo( "cf" );
		assertThat( data.get( "name" ) ).isEqualTo( "dbinfo" );
		assertThat( data.get( "description" ) ).isEqualTo( "" );
		assertThat( data.get( "status" ) ).isEqualTo( "implemented" );
		assertThat( data.get( "attributeType" ) ).isEqualTo( "fixed" );
		assertThat( data.get( "parseBody" ) ).isEqualTo( false );
		assertThat( data.get( "bodyType" ) ).isEqualTo( "free" );
		assertThat( data.get( "attrMin" ) ).isEqualTo( 0 );
		assertThat( data.get( "attrMax" ) ).isEqualTo( 0 );
		assertThat( data.get( "hasNameAppendix" ) ).isEqualTo( false );
		assertThat( data.get( "attributeCollection" ) ).isEqualTo( true );
		assertThat( data.get( "type" ) ).isEqualTo( "java" );
		assertThat( data.getAsStruct( Key.of( "attributes" ) ) ).isNotEmpty();
	}

}
