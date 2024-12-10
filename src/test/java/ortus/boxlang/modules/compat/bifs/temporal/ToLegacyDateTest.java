
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

package ortus.boxlang.modules.compat.bifs.temporal;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class ToLegacyDateTest extends BaseIntegrationTest {

	@DisplayName( "It tests the BIF ToLegacyDate" )
	@Test
	public void testToLegacyDateBIF() {
		runtime.executeSource(
		    """
		    result = toLegacyDate( now() );
		    """,
		    context );
		assertTrue( variables.get( result ) instanceof Date );
	}

	@DisplayName( "It tests the Member function ToLegacyDate" )
	@Test
	public void testToLegacyDateMember() {
		runtime.executeSource(
		    """
		    result = now().toLegacyDate();
		    """,
		    context );
		assertTrue( variables.get( result ) instanceof Date );
	}

}
