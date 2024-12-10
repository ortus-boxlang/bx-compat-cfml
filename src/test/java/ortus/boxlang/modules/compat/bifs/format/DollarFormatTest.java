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
package ortus.boxlang.modules.compat.bifs.format;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

public class DollarFormatTest extends BaseIntegrationTest {

	@DisplayName( "It formats a positive number as a U.S. Dollar string" )
	@Test
	void testItFormatsPositiveNumberAsDollarString() {
		runtime.executeSource(
		    """
		    result = dollarFormat(12345.67);
		    """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "$12,345.67" );
		runtime.executeSource(
		    """
		    result = dollarFormat(0.5);
		    """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "$0.50" );
	}

	@DisplayName( "It tests the member function Numeric.dollarFormat" )
	@Test
	void testNumericMemberFunction() {
		runtime.executeSource(
		    """
		    number = 12345.67;
		       result = number.dollarFormat();
		       """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "$12,345.67" );
		runtime.executeSource(
		    """
		    result = dollarFormat(0.5);
		    """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "$0.50" );
	}

	@DisplayName( "It formats a negative number as a U.S. Dollar string in parentheses" )
	@Test
	void testItFormatsNegativeNumberAsDollarStringInParentheses() {
		runtime.executeSource(
		    """
		    result = dollarFormat(-12345.67);
		    """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "($12,345.67)" );
		runtime.executeSource(
		    """
		    result = dollarFormat(-0.5);
		    """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "($0.50)" );
	}

	@DisplayName( "It formats zero as $0.00" )
	@Test
	void testItFormatsZeroAsDollarString() {
		runtime.executeSource(
		    """
		    result = dollarFormat(0);
		    """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "$0.00" );
	}

	@DisplayName( "It formats an empty string as $0.00" )
	@Test
	void testItFormatsEmptyStringAsDollarString() {
		runtime.executeSource(
		    """
		    result = dollarFormat("");
		    """,
		    context );
		assertThat( variables.get( result ) ).isEqualTo( "$0.00" );
	}

	@DisplayName( "It throws an exception when the argument is not a number" )
	@Test
	void testItThrowsExceptionWhenArgumentIsNotANumber() {
		assertThrows(
		    BoxRuntimeException.class,
		    () -> runtime.executeSource(
		        """
		        dollarFormat( "foo" );
		        """,
		        context )
		);
	}
}
