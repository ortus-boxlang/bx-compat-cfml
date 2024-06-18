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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.bifs.BIFDescriptor;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;

public class StructKeyTranslateTest {

	static BoxRuntime	instance;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= new Key( "result" );

	@BeforeAll
	public static void setUp() {
		instance = BoxRuntime.getInstance( true );
		BIFDescriptor descriptor = instance.getFunctionService().getGlobalFunction( "StructKeyTranslate" );
		assertEquals( "ortus.boxlang.modules.compat.bifs.struct.StructKeyTranslate", descriptor.BIFClass.getName() );
	}

	@AfterAll
	public static void teardown() {

	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext( instance.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@DisplayName( "Tests that the compat behavior will return the keys length" )
	@Test
	public void testLegacyReturn() {
		instance.executeSource(
		    """
		    ref = {
		    	"alpha.one.ten": {
		    		"beta.two.twenty": "v1"
		    	}
		    };
		    ex1 = duplicate( ref );
		    result1 = structkeytranslate( ex1 );

		    ex2 = duplicate( ref );
		    result2 = structkeytranslate( ex2, true, false );

		    ex3 = duplicate( ref );
		    result3 = structkeytranslate( ex3, false, true );
		            """,
		    context );

		assertEquals( 1, variables.get( Key.of( "result1" ) ) );
		assertEquals( 2, variables.get( Key.of( "result2" ) ) );
		assertEquals( 1, variables.get( Key.of( "result3" ) ) );

	}

}