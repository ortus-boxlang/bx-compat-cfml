package ortus.boxlang.modules.compat.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class ZipCompatTest extends BaseIntegrationTest {

	// Builds a temp dir with a root file and a nested file, zips it to `zipFile`, and cleans up in `finally`
	private static final String	SETUP		= """
	                                          tmp = getTempDirectory() & "bxzip-" & createUUID() & "/";
	                                          directoryCreate( tmp & "src/nested", true );
	                                          fileWrite( tmp & "src/a.txt", "a" );
	                                          fileWrite( tmp & "src/nested/b.txt", "b" );
	                                          zipFile = tmp & "test.zip";
	                                          bx:zip action="zip" file=zipFile source=tmp & "src" overwrite="true";
	                                          """;

	private static final String	TEARDOWN	= """
	                                          directoryDelete( tmp, true );
	                                          """;

	@DisplayName( "cfzip action=list with name returns a query with the full entry path in the name column" )
	@Test
	public void testListWithNameReturnsQuery() {
		// @formatter:off
		runtime.executeSource(
		    SETUP + """
				try {
					rs = "";
					bx:zip action="list" file=zipFile name="rs";
					assert isQuery( rs );
					assert rs.recordCount == 3;
					names = queryColumnData( rs, "name" );
					assert names.contains( "a.txt" );
					assert names.contains( "nested/" );
					assert names.contains( "nested/b.txt" );
					// core columns are all present
					assert rs.columnList.listFindNoCase( "fullpath" );
					assert rs.columnList.listFindNoCase( "size" );
					assert rs.columnList.listFindNoCase( "dateLastModified" );
					// the name variable is queryable
					count = 0;
					bx:loop query="rs" { count++; }
					assert count == 3;
					// nothing else is left behind
					assert !isDefined( "bxzip" );
				} finally {
				""" + TEARDOWN + """
				}
			   """,
		    context );
		// @formatter:on
	}

	@DisplayName( "cfzip action=list with name keeps directory segments so path traversal can be validated" )
	@Test
	public void testListWithNameKeepsFullPath() {
		// @formatter:off
		runtime.executeSource(
		    SETUP + """
				try {
					bx:zip action="list" file=zipFile name="rs";
					nested = queryFilter( rs, ( row ) => row.fullpath == "nested/b.txt" );
					assert nested.recordCount == 1;
					assert nested.name == "nested/b.txt";
				} finally {
				""" + TEARDOWN + """
				}
			   """,
		    context );
		// @formatter:on
	}

	@DisplayName( "cfzip action=list without name still returns the core array in result" )
	@Test
	public void testListWithoutNameDelegatesToCore() {
		// @formatter:off
		runtime.executeSource(
		    SETUP + """
				try {
					bx:zip action="list" file=zipFile result="entries";
					assert isArray( entries );
					assert entries.len() == 3;
					bx:zip action="list" file=zipFile;
					assert isArray( bxzip );
				} finally {
				""" + TEARDOWN + """
				}
			   """,
		    context );
		// @formatter:on
	}

	@DisplayName( "cfzip other actions (unzip with a body zipparam) still work" )
	@Test
	public void testOtherActionsDelegateToCore() {
		// @formatter:off
		runtime.executeSource(
		    SETUP + """
				try {
					bx:zip action="unzip" file=zipFile destination=tmp & "out" {
					}
					assert fileExists( tmp & "out/a.txt" );
					assert fileExists( tmp & "out/nested/b.txt" );

					bx:zip action="zip" file=tmp & "two.zip" overwrite="true" {
						bx:zipparam source=tmp & "src/a.txt";
					}
					bx:zip action="list" file=tmp & "two.zip" name="rs2";
					assert rs2.recordCount == 1;
					assert rs2.name == "a.txt";
				} finally {
				""" + TEARDOWN + """
				}
			   """,
		    context );
		// @formatter:on
	}
}
