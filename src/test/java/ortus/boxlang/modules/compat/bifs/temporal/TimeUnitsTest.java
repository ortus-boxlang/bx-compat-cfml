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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;

public class TimeUnitsTest extends BaseIntegrationTest {

	@DisplayName( "It tests the BIF DayOfWeekAsString" )
	@Test
	public void testBifDayOfWeekAsString() {
		runtime.executeSource(
		    """
		    result = dayOfWeekAsString( 1 );
		    """,
		    context );
		String test = variables.getAsString( result );
		assertEquals( test, "Sunday" );

		runtime.executeSource(
		    """
		    result = dayOfWeekAsString( now() );
		    """,
		    context );
		assertTrue( variables.get( result ) instanceof String );

	}

	@DisplayName( "It tests the BIF DayOfWeekAsString will keep the same week start but return a different language" )
	@Test
	public void testBifDayOfWeekAsStringLocale() {
		runtime.executeSource(
		    """
		    setLocale( "es_SV" );
		       result = dayOfWeekAsString( 1 );
		       """,
		    context );
		String test = variables.getAsString( result );
		assertEquals( test, "domingo" );
	}

	@DisplayName( "It tests the DateTime Member function DayOfWeekShortAsString" )
	@Test
	public void testMemberDayOfWeekShortAsString() {
		runtime.executeSource(
		    """
		    result = dayOfWeekShortAsString( 1 );
		    """,
		    context );
		String test = variables.getAsString( result );
		assertEquals( "Sun", test );
	}

	@DisplayName( "It tests the BIF MonthAsString" )
	@Test
	public void testBifMonthAsString() {
		runtime.executeSource(
		    """
		    result = monthAsString( 1 );
		    """,
		    context );
		String test = variables.getAsString( result );
		assertEquals( test, "January" );
	}

	@DisplayName( "It tests the DateTime Member function MonthShortAsString" )
	@Test
	public void testMemberMonthShortAsString() {
		runtime.executeSource(
		    """
		    result = monthShortAsString( 1 );
		    """,
		    context );
		String test = variables.getAsString( Key.of( "result" ) );
		assertEquals( test, "Jan" );
	}

}