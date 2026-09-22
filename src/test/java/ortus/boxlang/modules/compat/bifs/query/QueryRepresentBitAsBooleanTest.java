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

package ortus.boxlang.modules.compat.bifs.query;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.jdbc.drivers.GenericJDBCDriver;
import ortus.boxlang.runtime.modules.BoxModuleConfig;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Query;

/**
 * Tests the {@code queryRepresentBitAsBoolean} module setting.
 *
 * This setting controls how BIT query values are stored internally: as booleans
 * ({@code true}/{@code false}) or as numbers ({@code 1}/{@code 0}). It defaults to
 * {@code null}, in which case it falls back to the configured engine (Lucee = booleans,
 * Adobe = numbers). The resolved value is pushed into the core
 * {@code GenericJDBCDriver.representBitAsBoolean} static flag and is applied by
 * {@code ModuleConfig.configureQueryRepresentation()}.
 *
 * These tests exercise every combination of engine (lucee/adobe) and the explicit
 * setting value ({@code true}/{@code false}/{@code null}) by re-invoking the
 * {@code configureQueryRepresentation()} method directly, mirroring the pattern used by
 * {@code JSONDeserializeTest.configureJSONParsing()}.
 */
public class QueryRepresentBitAsBooleanTest extends BaseIntegrationTest {

	private static final Key	ENGINE							= Key.of( "engine" );
	private static final Key	IS_ADOBE						= KeyDictionary.isAdobe;
	private static final Key	IS_LUCEE						= KeyDictionary.isLucee;
	private static final Key	QUERY_REPRESENT_BIT_AS_BOOLEAN	= Key.of( "queryRepresentBitAsBoolean" );
	private static final Key	MODULE_CONFIG					= Key.of( "moduleConfig" );

	// ===================== Default behavior (setting is null) =====================

	@DisplayName( "Lucee default: BIT values are stored as booleans" )
	@Test
	public void testLuceeDefaultStoresBitAsBoolean() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureQueryRepresentation( "lucee", null );
			runtime.executeSource(
			    """
			    q = queryNew( "flag", "bit", [ [ 1 ], [ 0 ], [ true ], [ false ] ] );
			       """,
			    context );
			assertQueryBitColumn( 1 );
		} finally {
			configureQueryRepresentation( previousEngine, null );
		}
	}

	@DisplayName( "Adobe default: BIT values are stored as numbers" )
	@Test
	public void testAdobeDefaultStoresBitAsNumber() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureQueryRepresentation( "adobe", null );
			runtime.executeSource(
			    """
			    q = queryNew( "flag", "bit", [ [ 1 ], [ 0 ], [ true ], [ false ] ] );
			       """,
			    context );
			assertQueryBitColumn( 0 );
		} finally {
			configureQueryRepresentation( previousEngine, null );
		}
	}

	// ===================== Explicit setting: true =====================

	@DisplayName( "Lucee + explicit true: BIT values are stored as booleans" )
	@Test
	public void testLuceeWithExplicitTrue() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureQueryRepresentation( "lucee", true );
			runtime.executeSource(
			    """
			    q = queryNew( "flag", "bit", [ [ 1 ], [ 0 ], [ true ], [ false ] ] );
			       """,
			    context );
			assertQueryBitColumn( 1 );
		} finally {
			configureQueryRepresentation( previousEngine, null );
		}
	}

	@DisplayName( "Adobe + explicit true: BIT values are stored as booleans" )
	@Test
	public void testAdobeWithExplicitTrue() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureQueryRepresentation( "adobe", true );
			runtime.executeSource(
			    """
			    q = queryNew( "flag", "bit", [ [ 1 ], [ 0 ], [ true ], [ false ] ] );
			       """,
			    context );
			assertQueryBitColumn( 1 );
		} finally {
			configureQueryRepresentation( previousEngine, null );
		}
	}

	// ===================== Explicit setting: false =====================

	@DisplayName( "Lucee + explicit false: BIT values are stored as numbers" )
	@Test
	public void testLuceeWithExplicitFalse() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureQueryRepresentation( "lucee", false );
			runtime.executeSource(
			    """
			    q = queryNew( "flag", "bit", [ [ 1 ], [ 0 ], [ true ], [ false ] ] );
			       """,
			    context );
			assertQueryBitColumn( 0 );
		} finally {
			configureQueryRepresentation( previousEngine, null );
		}
	}

	@DisplayName( "Adobe + explicit false: BIT values are stored as numbers" )
	@Test
	public void testAdobeWithExplicitFalse() {
		var previousEngine = moduleRecord.settings.getAsString( ENGINE );
		try {
			configureQueryRepresentation( "adobe", false );
			runtime.executeSource(
			    """
			    q = queryNew( "flag", "bit", [ [ 1 ], [ 0 ], [ true ], [ false ] ] );
			       """,
			    context );
			assertQueryBitColumn( 0 );
		} finally {
			configureQueryRepresentation( previousEngine, null );
		}
	}

	// ===================== Helpers =====================

	/**
	 * Assert that the core driver flag is in the expected state and that a freshly populated
	 * BIT query column reflects that state.
	 *
	 * @param storesAsBoolean When {@code 1}, cells are expected to store booleans; when {@code 0}, numbers.
	 */
	private void assertQueryBitColumn( int storesAsBoolean ) {
		boolean expected = storesAsBoolean == 1;
		// The module should have pushed its resolved setting into the core static flag.
		assertThat( GenericJDBCDriver.representBitAsBoolean ).isEqualTo( expected );

		Query query = variables.getAsQuery( Key.of( "q" ) );
		assertThat( query.getCell( Key.of( "flag" ), 0 ) ).isEqualTo( expected ? true : 1 );
		assertThat( query.getCell( Key.of( "flag" ), 1 ) ).isEqualTo( expected ? false : 0 );
		assertThat( query.getCell( Key.of( "flag" ), 2 ) ).isEqualTo( expected ? true : 1 );
		assertThat( query.getCell( Key.of( "flag" ), 3 ) ).isEqualTo( expected ? false : 0 );
	}

	/**
	 * Toggle the module's engine and {@code queryRepresentBitAsBoolean} settings and push the
	 * resolved value into the core {@code GenericJDBCDriver} by re-invoking the module's
	 * {@code configureQueryRepresentation()} method.
	 *
	 * @param engine                     The engine to set ("lucee" or "adobe").
	 * @param queryRepresentBitAsBoolean The explicit setting value, or {@code null} to use the engine default.
	 */
	private void configureQueryRepresentation( String engine, Boolean queryRepresentBitAsBoolean ) {
		var settings = moduleRecord.settings;
		settings.put( ENGINE, engine );
		settings.put( IS_ADOBE, engine.equals( "adobe" ) );
		settings.put( IS_LUCEE, engine.equals( "lucee" ) );
		settings.put( QUERY_REPRESENT_BIT_AS_BOOLEAN, queryRepresentBitAsBoolean );
		var moduleConfig = ( ( BoxModuleConfig ) moduleRecord.moduleConfig ).getBxClass();
		variables.put( MODULE_CONFIG, moduleConfig );
		runtime.executeSource( "moduleConfig.configureQueryRepresentation();", context );
	}

}