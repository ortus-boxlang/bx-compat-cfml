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

package ortus.boxlang.modules.compat.bifs.conversion;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.modules.compat.util.KeyDictionary;

public class JSONSerializeTest extends BaseIntegrationTest {

	// ===================== Adobe mode =====================

	@DisplayName( "Adobe: Queries serialize as row by default with uppercased columns" )
	@Test
	public void testAdobeQueriesSerializeAsRowByDefault() {
		moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, true );
		try {
			runtime.executeSource(
			    """
			       myQuery = queryNew( "col1", "varchar", { col1: "Grant" } );
			    result = JSONSerialize( myQuery )
			       """,
			    context );
			String myResult = variables.getAsString( result );
			assertThat( myResult ).startsWith( "{" );
			assertThat( myResult ).contains( "\"COLUMNS\"" );
			assertThat( myResult ).contains( "\"COL1\"" );
			assertThat( myResult ).contains( "\"DATA\"" );
			assertThat( myResult ).contains( "\"Grant\"" );
		} finally {
			moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, false );
		}
	}

	@DisplayName( "Adobe: Query toJSON row mode uppercases columns" )
	@Test
	public void testAdobeQueryToJSONRowUppercaseColumns() {
		moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, true );
		try {
			runtime.executeSource(
			    """
			       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "row" )
			    result = q
			       """,
			    context );

			String myResult = variables.getAsString( result );
			assertThat( myResult ).isEqualTo( "{\"COLUMNS\":[\"COL1\",\"COL2\",\"COLUMN3\"],\"DATA\":[[\"brad\",\"luis\",\"jon\"]]}" );
		} finally {
			moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, false );
		}
	}

	@DisplayName( "Adobe: Query toJSON column mode uppercases columns" )
	@Test
	public void testAdobeQueryToJSONColumnUppercaseColumns() {
		moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, true );
		try {
			runtime.executeSource(
			    """
			       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "column" )
			    result = q
			       """,
			    context );

			String myResult = variables.getAsString( result );
			assertThat( myResult ).isEqualTo(
			    "{\"ROWCOUNT\":1,\"COLUMNS\":[\"COL1\",\"COL2\",\"COLUMN3\"],\"DATA\":{\"COL1\":[\"brad\"],\"COL2\":[\"luis\"],\"COLUMN3\":[\"jon\"]}}" );
		} finally {
			moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, false );
		}
	}

	@DisplayName( "Adobe: Query toJSON struct mode uppercases keys" )
	@Test
	public void testAdobeQueryToJSONStructUppercaseColumns() {
		moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, true );
		try {
			runtime.executeSource(
			    """
			       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "struct" )
			    result = q
			       """,
			    context );

			String myResult = variables.getAsString( result );
			assertThat( myResult ).isEqualTo( "[{\"COL1\":\"brad\",\"COL2\":\"luis\",\"COLUMN3\":\"jon\"}]" );
		} finally {
			moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, false );
		}
	}

	// ===================== Lucee mode =====================

	@DisplayName( "Lucee: Queries serialize as row by default with uppercase outer keys, preserved column case" )
	@Test
	public void testLuceeQueriesSerializeAsRowByDefault() {
		runtime.executeSource(
		    """
		       myQuery = queryNew( "col1", "varchar", { col1: "Grant" } );
		    result = JSONSerialize( myQuery )
		       """,
		    context );
		String myResult = variables.getAsString( result );
		assertThat( myResult ).startsWith( "{" );
		assertThat( myResult ).contains( "\"COLUMNS\"" );
		assertThat( myResult ).contains( "\"col1\"" );
		assertThat( myResult ).contains( "\"DATA\"" );
		assertThat( myResult ).contains( "\"Grant\"" );
	}

	@DisplayName( "Lucee: Query toJSON row mode preserves column case in COLUMNS array" )
	@Test
	public void testLuceeQueryToJSONRowPreservesColumnCase() {
		runtime.executeSource(
		    """
		       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "row" )
		    result = q
		       """,
		    context );

		String myResult = variables.getAsString( result );
		assertThat( myResult ).isEqualTo( "{\"COLUMNS\":[\"col1\",\"COL2\",\"CoLuMn3\"],\"DATA\":[[\"brad\",\"luis\",\"jon\"]]}" );
	}

	@DisplayName( "Lucee: Query toJSON column mode preserves column case in COLUMNS array, uppercases DATA struct keys" )
	@Test
	public void testLuceeQueryToJSONColumnPreservesColumnCase() {
		runtime.executeSource(
		    """
		       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "column" )
		    result = q
		       """,
		    context );

		String myResult = variables.getAsString( result );
		assertThat( myResult ).isEqualTo(
		    "{\"ROWCOUNT\":1,\"COLUMNS\":[\"col1\",\"COL2\",\"CoLuMn3\"],\"DATA\":{\"COL1\":[\"brad\"],\"COL2\":[\"luis\"],\"COLUMN3\":[\"jon\"]}}" );
	}

	@DisplayName( "Lucee: Query toJSON struct mode preserves key case in row structs" )
	@Test
	public void testLuceeQueryToJSONStructPreservesKeyCase() {
		runtime.executeSource(
		    """
		       q = queryNew( "col1,COL2,CoLuMn3", "varchar,varchar,varchar", [["brad","luis","jon"]] ).toJSON( "struct" )
		    result = q
		       """,
		    context );

		String myResult = variables.getAsString( result );
		assertThat( myResult ).isEqualTo( "[{\"col1\":\"brad\",\"COL2\":\"luis\",\"CoLuMn3\":\"jon\"}]" );
	}
}
