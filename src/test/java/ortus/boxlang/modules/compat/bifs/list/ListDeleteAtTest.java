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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;

public class ListDeleteAtTest extends BaseIntegrationTest {

	@DisplayName( "It does not lose preceding slashes when dealing with paths" )
	@Test
	public void itDoesNotLosePrecedingSlashes() {
		runtime.executeSource(
		    """
		       path = "/Users/elpete/Developer/github/coldbox-modules/quick/";
		       position = listLen( path, "\\/" );
		    result = listDeleteAt( path, position, "\\/" );
		       """,
		    context );
		assertEquals( 6, variables.getAsInteger( Key.position ) );
		assertEquals( "/Users/elpete/Developer/github/coldbox-modules", variables.getAsString( result ) );

	}

}
