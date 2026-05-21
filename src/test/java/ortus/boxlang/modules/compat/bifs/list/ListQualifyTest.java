/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.modules.compat.bifs.list;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.modules.compat.util.KeyDictionary;

public class ListQualifyTest extends BaseIntegrationTest {

	@DisplayName( "In Adobe mode, it qualifies empty fields with the qualifier" )
	@Test
	public void itQualifiesEmptyFieldsInAdobeMode() {
		moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, true );
		try {
			runtime.executeSource(
			    """
			    result = listqualify( "", "'", ",", "all", true );
			    """,
			    context );
			assertThat( variables.getAsString( result ) ).isEqualTo( "''" );
		} finally {
			moduleService.getModuleSettings( KeyDictionary.moduleName ).put( KeyDictionary.isAdobe, false );
		}
	}

	@DisplayName( "In Lucee mode, an empty list has zero elements to qualify" )
	@Test
	public void itQualifiesEmptyFieldsInLuceeMode() {
		runtime.executeSource(
		    """
		    result = listqualify( "", "'", ",", "all", true );
		    """,
		    context );
		assertThat( variables.getAsString( result ) ).isEqualTo( "" );
	}

}
