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

import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.bifs.BIFDescriptor;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.util.LocalizationUtil;

public class TimeUnitsTest {

	static BoxRuntime	instance;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= Key.of( "result" );
	static Locale		locale;

	@BeforeAll
	public static void setUp() {
		instance = BoxRuntime.getInstance( true );
		BIFDescriptor descriptor = instance.getFunctionService().getGlobalFunction( "DayOfWeekAsString" );
		assertEquals( "ortus.boxlang.modules.compat.bifs.temporal.TimeUnits", descriptor.BIFClass.getName() );
	}

	@AfterAll
	public static void teardown() {
	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext( instance.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
		locale		= LocalizationUtil.parseLocaleFromContext( context, new ArgumentsScope() );
	}

	@DisplayName( "It tests the BIF DayOfWeekAsString" )
	@Test
	public void testBifDayOfWeekAsString() {
		instance.executeSource(
		    """
		    result = dayOfWeekAsString( 1 );
		    """,
		    context );
		String test = variables.getAsString( result );
		assertEquals( test, "Sunday" );
	}

	@DisplayName( "It tests the BIF DayOfWeekAsString will keep the same week start but return a different language" )
	@Test
	public void testBifDayOfWeekAsStringLocale() {
		instance.executeSource(
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
		instance.executeSource(
		    """
		    result = dayOfWeekShortAsString( 1 );
		    """,
		    context );
		String test = variables.getAsString( result );
		assertEquals( test, "Sun" );
	}

	@DisplayName( "It tests the BIF MonthAsString" )
	@Test
	public void testBifMonthAsString() {
		instance.executeSource(
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
		instance.executeSource(
		    """
		    result = monthShortAsString( 1 );
		    """,
		    context );
		String test = variables.getAsString( Key.of( "result" ) );
		assertEquals( test, "Jan" );
	}

}