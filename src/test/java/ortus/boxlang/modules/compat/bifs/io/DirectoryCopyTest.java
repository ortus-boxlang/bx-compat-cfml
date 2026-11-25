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
package ortus.boxlang.modules.compat.bifs.io;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;

public class DirectoryCopyTest extends BaseIntegrationTest {

	// Builds a temp dir with a root file and a nested file so the copy is cleaned up in `finally`
	private static final String	SETUP		= """
	                                          tmp = getTempDirectory() & "bxdc-" & createUUID() & "/";
	                                          directoryCreate( tmp & "src/nested", true );
	                                          fileWrite( tmp & "src/a.txt", "a" );
	                                          fileWrite( tmp & "src/nested/b.txt", "b" );
	                                          """;

	private static final String	TEARDOWN	= """
	                                          directoryDelete( tmp, true );
	                                          """;

	@DisplayName( "It copies a directory tree recursively" )
	@Test
	public void testCopyDirectory() {
		// @formatter:off
		runtime.executeSource(
		    SETUP + """
				try {
					directoryCopy( tmp & "src", tmp & "dest", true );
					rootExists = fileExists( tmp & "dest/a.txt" );
					nestedExists = fileExists( tmp & "dest/nested/b.txt" );
					aContent = fileRead( tmp & "dest/a.txt" );
					bContent = fileRead( tmp & "dest/nested/b.txt" );
				} finally {
				""" + TEARDOWN + """
				}
			   """,
		    context );
		// @formatter:on
		assertThat( variables.get( Key.of( "rootExists" ) ) ).isEqualTo( true );
		assertThat( variables.get( Key.of( "nestedExists" ) ) ).isEqualTo( true );
		assertThat( variables.getAsString( Key.of( "aContent" ) ) ).isEqualTo( "a" );
		assertThat( variables.getAsString( Key.of( "bContent" ) ) ).isEqualTo( "b" );
	}

	@DisplayName( "The compat BIF overwrites existing destination files by default" )
	@Test
	public void testOverwriteDefaultsToTrue() {
		// @formatter:off
		runtime.executeSource(
		    SETUP + """
				try {
					directoryCreate( tmp & "dest/nested", true );
					fileWrite( tmp & "dest/a.txt", "stale" );
					fileWrite( tmp & "dest/nested/b.txt", "stale" );
					directoryCopy( tmp & "src", tmp & "dest", true );
					aContent = fileRead( tmp & "dest/a.txt" );
					bContent = fileRead( tmp & "dest/nested/b.txt" );
				} finally {
				""" + TEARDOWN + """
				}
			   """,
		    context );
		// @formatter:on
		assertThat( variables.getAsString( Key.of( "aContent" ) ) ).isEqualTo( "a" );
		assertThat( variables.getAsString( Key.of( "bContent" ) ) ).isEqualTo( "b" );
	}

	@DisplayName( "It leaves existing destination files alone when overwrite is false" )
	@Test
	public void testOverwriteFalseKeepsExisting() {
		// @formatter:off
		runtime.executeSource(
		    SETUP + """
				try {
					directoryCreate( tmp & "dest", true );
					fileWrite( tmp & "dest/a.txt", "stale" );
					errored = false;
					try {
						directoryCopy( tmp & "src", tmp & "dest", true, "*", true, false );
					} catch ( any e ) {
						errored = true;
					}
					result = errored || fileRead( tmp & "dest/a.txt" ) == "stale";
				} finally {
				""" + TEARDOWN + """
				}
			   """,
		    context );
		// @formatter:on
		assertThat( variables.get( Key.of( "result" ) ) ).isEqualTo( true );
	}

}
