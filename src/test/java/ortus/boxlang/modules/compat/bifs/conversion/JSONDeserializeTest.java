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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.modules.BoxModuleConfig;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

public class JSONDeserializeTest extends BaseIntegrationTest {

	private static final Key	ENGINE					= Key.of( "engine" );
	private static final Key	IS_ADOBE				= KeyDictionary.isAdobe;
	private static final Key	IS_LUCEE				= KeyDictionary.isLucee;
	private static final Key	LENIENT_JSON_PARSING	= Key.of( "lenientJSONParsing" );
	private static final Key	LEADING_ZEROS			= Key.of( "useLenientParsingLeadingZeros" );
	private static final Key	MODULE_CONFIG			= Key.of( "moduleConfig" );

	@DisplayName( "This is Adobe" )
	@Test
	public void testCanDeserializeSingleQuoteKey() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureJSONParsing( "adobe" );
			Throwable throwable = assertThrows( BoxRuntimeException.class, () -> runtime.executeSource(
			    """
			    result = JSONDeserialize( "{ 'a' : 1, 'b' : 2, 'c' : 3 }" )
			         """,
			    context ) );
			assertThat( throwable.getMessage() ).contains( "Failed to parse JSON" );
		} finally {
			configureJSONParsing( previousEngine );
		}
	}

	@DisplayName( "This is Lucee" )
	@Test
	public void testCanDeserializeUnQuoteKey() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureJSONParsing( "lucee" );
			runtime.executeSource(
			    """
			    result = JSONDeserialize( "{ a : 1, b : 2, c : 3 }" )
			         """,
			    context );
			assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );
		} finally {
			configureJSONParsing( previousEngine );
		}
	}

	@DisplayName( "This is Adobe" )
	@Test
	public void testCanDeserializeTrailingCommas() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureJSONParsing( "adobe" );
			Throwable throwable = assertThrows( BoxRuntimeException.class, () -> runtime.executeSource(
			    """
			    result = JSONDeserialize( '{ "foo" : "bar", }' )
			         """,
			    context ) );
			assertThat( throwable.getMessage() ).contains( "Failed to parse JSON" );
		} finally {
			configureJSONParsing( previousEngine );
		}
	}

	@DisplayName( "This is Adobe" )
	@Test
	public void testCanDeserializeLeadingZeroes() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureJSONParsing( "adobe" );
			runtime.executeSource(
			    """
			    result = JSONDeserialize( '{ "foo" : 01 }' )
			         """,
			    context );
			assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );

		} finally {
			configureJSONParsing( previousEngine );
		}
	}

	@DisplayName( "This is Lucee" )
	@Test
	public void testCanDeserializeLeadingZeroesInLucee() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureJSONParsing( "lucee" );
			runtime.executeSource(
			    """
			    result = JSONDeserialize( '{ "foo" : 01 }' )
			         """,
			    context );
			assertThat( variables.get( result ) ).isInstanceOf( IStruct.class );
		} finally {
			configureJSONParsing( previousEngine );
		}
	}

	private void configureJSONParsing( String engine ) {
		var settings = moduleRecord.settings;
		settings.put( ENGINE, engine );
		settings.put( IS_ADOBE, engine.equals( "adobe" ) );
		settings.put( IS_LUCEE, engine.equals( "lucee" ) );
		settings.put( LENIENT_JSON_PARSING, null );
		settings.put( LEADING_ZEROS, null );
		var moduleConfig = ( ( BoxModuleConfig ) moduleRecord.moduleConfig ).getBxClass();
		variables.put( MODULE_CONFIG, moduleConfig );
		runtime.executeSource( "moduleConfig.configureJSONParsing();", context );
	}

}
