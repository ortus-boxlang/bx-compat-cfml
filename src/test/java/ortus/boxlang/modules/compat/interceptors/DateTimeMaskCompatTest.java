/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.modules.compat.interceptors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

/**
 * Tests for DateTimeMaskCompat - specifically the quoteInvalidPatternLetters method
 * (BL-2322 / BL-2323) and integration scenarios with arbitrary literal words in masks.
 */
public class DateTimeMaskCompatTest extends BaseIntegrationTest {

	// -------------------------------------------------------------------------
	// Unit tests for quoteInvalidPatternLetters
	// -------------------------------------------------------------------------

	@DisplayName( "quoteInvalidPatternLetters: passes through null and empty strings" )
	@Test
	public void testQuoteInvalidPatternLettersNullAndEmpty() {
		assertEquals( null, DateTimeMaskCompat.quoteInvalidPatternLetters( null ) );
		assertEquals( "", DateTimeMaskCompat.quoteInvalidPatternLetters( "" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: leaves a fully-valid pattern unchanged" )
	@Test
	public void testQuoteInvalidPatternLettersValidPattern() {
		assertEquals( "yyyy-MM-dd", DateTimeMaskCompat.quoteInvalidPatternLetters( "yyyy-MM-dd" ) );
		assertEquals( "HH:mm:ss.SSS", DateTimeMaskCompat.quoteInvalidPatternLetters( "HH:mm:ss.SSS" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: quotes a single invalid letter" )
	@Test
	public void testQuoteInvalidPatternLettersSingleInvalid() {
		// 'i' is not a valid Java DateTimeFormatter letter
		assertEquals( "yyyy-'i'-dd", DateTimeMaskCompat.quoteInvalidPatternLetters( "yyyy-i-dd" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: quotes consecutive invalid letters as one literal" )
	@Test
	public void testQuoteInvalidPatternLettersConsecutiveInvalid() {
		// "at" starts with 'a' (valid) so the word is processed per same-char token:
		// 'a' is valid (AM/PM token), 't' is a single lowercase t -> AM/PM sentinel
		assertEquals( "HH:mm a" + DateTimeMaskCompat.AMPM_ABBR_SENTINEL + " dd-MM-yyyy",
		    DateTimeMaskCompat.quoteInvalidPatternLetters( "HH:mm at dd-MM-yyyy" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: handles a word starting with an invalid letter - whole word quoted" )
	@Test
	public void testQuoteInvalidPatternLettersEntireWord() {
		// "iso" starts with 'i' (invalid) -> the entire contiguous letter run is quoted as a literal
		// Note: in practice "iso" would be caught by COMMON_FORMATTERS early-exit before reaching this method
		assertEquals( "'iso'", DateTimeMaskCompat.quoteInvalidPatternLetters( "iso" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: does not re-quote already-quoted literals" )
	@Test
	public void testQuoteInvalidPatternLettersAlreadyQuoted() {
		// Already quoted sections must pass through verbatim
		assertEquals( "HH:mm 'at' dd", DateTimeMaskCompat.quoteInvalidPatternLetters( "HH:mm 'at' dd" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: handles escaped single quote (apostrophe literal '')" )
	@Test
	public void testQuoteInvalidPatternLettersEscapedQuote() {
		// '' is the Java DateTimeFormatter way to represent a literal single quote
		assertEquals( "dd''-MM-yyyy", DateTimeMaskCompat.quoteInvalidPatternLetters( "dd''-MM-yyyy" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: quotes an invalid same-char token at the end of a valid-starting word" )
	@Test
	public void testQuoteInvalidPatternLettersTrailing() {
		// "ddT" starts with 'd' (valid) -> per-token: "dd" valid, "T" single uppercase -> AM/PM sentinel
		assertEquals( "yyyy-MM-dd" + DateTimeMaskCompat.AMPM_ABBR_SENTINEL,
		    DateTimeMaskCompat.quoteInvalidPatternLetters( "yyyy-MM-ddT" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: non-letter characters are passed through unchanged" )
	@Test
	public void testQuoteInvalidPatternLettersNonLetters() {
		assertEquals( "yyyy/MM/dd HH:mm:ss", DateTimeMaskCompat.quoteInvalidPatternLetters( "yyyy/MM/dd HH:mm:ss" ) );
	}

	@DisplayName( "quoteInvalidPatternLetters: mixed valid, invalid, and non-letter characters" )
	@Test
	public void testQuoteInvalidPatternLettersMixed() {
		// 'r' is not a valid pattern letter; result should quote it
		assertEquals( "yyyy-MM-dd 'r'", DateTimeMaskCompat.quoteInvalidPatternLetters( "yyyy-MM-dd r" ) );
	}

}
