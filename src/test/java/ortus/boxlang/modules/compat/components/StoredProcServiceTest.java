package ortus.boxlang.modules.compat.components;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Query;

@DisplayName( "new storedProc() Service Tests" )
public class StoredProcServiceTest extends BaseIntegrationTest {

	static final String dsn = """
	                          {
	                              type: 'mysql',
	                              host: 'localhost',
	                              port: 3309,
	                              database: 'myDB',
	                              username: 'root',
	                              password: '123456Password'
	                          }
	                          """;

	@BeforeAll
	public static void setupStoredProcTests() {
		assumeTrue( BoxRuntime.getInstance().getModuleService().hasModule( Key.of( "mysql" ) ),
		    "bx-mysql module is not installed — skipping StoredProc service tests" );
	}

	@BeforeEach
	public void setupProcedures() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      queryExecute( "DROP TABLE IF EXISTS bx_sp_test", [], { datasource: ds } );
		                                      queryExecute( "
		                                          CREATE TABLE bx_sp_test (
		                                              id INT AUTO_INCREMENT PRIMARY KEY,
		                                              name VARCHAR(100) NOT NULL,
		                                              role VARCHAR(100) NOT NULL
		                                          )
		                                      ", [], { datasource: ds } );
		                                      queryExecute( "INSERT INTO bx_sp_test (name, role) VALUES ('Brad', 'Admin')", [], { datasource: ds } );
		                                      queryExecute( "INSERT INTO bx_sp_test (name, role) VALUES ('Luis', 'Developer')", [], { datasource: ds } );
		                                      queryExecute( "INSERT INTO bx_sp_test (name, role) VALUES ('Jon', 'Designer')", [], { datasource: ds } );

		                                      queryExecute( "DROP PROCEDURE IF EXISTS sp_get_by_role", [], { datasource: ds } );
		                                      queryExecute( "
		                                          CREATE PROCEDURE sp_get_by_role( IN p_role VARCHAR(100) )
		                                          BEGIN
		                                              SELECT * FROM bx_sp_test WHERE role = p_role;
		                                          END
		                                      ", [], { datasource: ds } );

		                                      queryExecute( "DROP PROCEDURE IF EXISTS sp_count_users", [], { datasource: ds } );
		                                      queryExecute( "
		                                          CREATE PROCEDURE sp_count_users( OUT p_count INT )
		                                          BEGIN
		                                              SELECT COUNT(*) INTO p_count FROM bx_sp_test;
		                                          END
		                                      ", [], { datasource: ds } );

		                                      queryExecute( "DROP PROCEDURE IF EXISTS sp_get_all", [], { datasource: ds } );
		                                      queryExecute( "
		                                          CREATE PROCEDURE sp_get_all()
		                                          BEGIN
		                                              SELECT * FROM bx_sp_test ORDER BY id;
		                                          END
		                                      ", [], { datasource: ds } );
		                                      """, dsn ),
		    context );
	}

	@DisplayName( "It can create a storedProc service and set attributes" )
	@Test
	public void testImplicitSetters() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds );
		                                      sp.setProcedure( "sp_get_all" );
		                                      attrs = sp.getAttributes();
		                                      hasDatasource = structKeyExists( attrs, "datasource" );
		                                      hasProcedure = structKeyExists( attrs, "procedure" );
		                                      """, dsn ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasDatasource" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasProcedure" ) ) ).isTrue();
	}

	@DisplayName( "It can initialize attributes in the constructor" )
	@Test
	public void testConstructorAttributes() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc( datasource=ds, procedure="sp_get_all" );
		                                      attrs = sp.getAttributes();
		                                      hasProcedure = structKeyExists( attrs, "procedure" );
		                                      """, dsn ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasProcedure" ) ) ).isTrue();
	}

	@DisplayName( "It can execute a simple stored procedure with a result set" )
	@Test
	public void testExecuteWithResultSet() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds );
		                                      sp.setProcedure( "sp_get_all" );
		                                      sp.addProcResult( name="rs1", resultset=1 );
		                                      result = sp.execute();
		                                      data = result.getProcResultSets().rs1;
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 3 );
	}

	@DisplayName( "It can execute a stored procedure with IN parameters" )
	@Test
	public void testExecuteWithInParam() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds );
		                                      sp.setProcedure( "sp_get_by_role" );
		                                      sp.addParam( cfsqltype="cf_sql_varchar", type="in", value="Admin" );
		                                      sp.addProcResult( name="rs1", resultset=1 );
		                                      result = sp.execute();
		                                      data = result.getProcResultSets().rs1;
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 1 );
	}

	@DisplayName( "It can execute a stored procedure with OUT parameters" )
	@Test
	public void testExecuteWithOutParam() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds );
		                                      sp.setProcedure( "sp_count_users" );
		                                      sp.addParam( cfsqltype="cf_sql_integer", type="out", variable="userCount" );
		                                      result = sp.execute();
		                                      outVars = result.getProcOutVariables();
		                                      theCount = outVars.userCount;
		                                      """, dsn ),
		    context );

		assertThat( Integer.parseInt( variables.get( Key.of( "theCount" ) ).toString() ) ).isEqualTo( 3 );
	}

	@DisplayName( "It supports method chaining" )
	@Test
	public void testMethodChaining() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds )
		                                          .setProcedure( "sp_get_all" )
		                                          .addProcResult( name="rs1", resultset=1 );
		                                      result = sp.execute();
		                                      data = result.getProcResultSets().rs1;
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 3 );
	}

	@DisplayName( "It can clearParams() and clearProcResults()" )
	@Test
	public void testClearParamsAndResults() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds );
		                                      sp.setProcedure( "sp_get_by_role" );
		                                      sp.addParam( cfsqltype="cf_sql_varchar", type="in", value="Admin" );
		                                      sp.addProcResult( name="rs1", resultset=1 );
		                                      sp.clearParams();
		                                      sp.clearProcResults();

		                                      sp.setProcedure( "sp_get_all" );
		                                      sp.addProcResult( name="rs1", resultset=1 );
		                                      result = sp.execute();
		                                      data = result.getProcResultSets().rs1;
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 3 );
	}

	@DisplayName( "It can use clear() to reset everything" )
	@Test
	public void testClear() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds );
		                                      sp.setProcedure( "sp_get_all" );
		                                      sp.addParam( cfsqltype="cf_sql_varchar", type="in", value="x" );
		                                      sp.addProcResult( name="rs1", resultset=1 );
		                                      sp.clear();
		                                      attrs = sp.getAttributes();
		                                      attrCount = structCount( attrs );
		                                      """, dsn ),
		    context );

		assertThat( ( ( Number ) variables.get( Key.of( "attrCount" ) ) ).intValue() ).isEqualTo( 0 );
	}

	@DisplayName( "It can use setAttributes() for bulk configuration" )
	@Test
	public void testSetAttributes() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setAttributes( datasource=ds, procedure="sp_get_all" );
		                                      sp.addProcResult( name="rs1", resultset=1 );
		                                      result = sp.execute();
		                                      data = result.getProcResultSets().rs1;
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 3 );
	}

	@DisplayName( "It populates getPrefix() with execution metadata" )
	@Test
	public void testGetPrefix() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      sp = new storedProc();
		                                      sp.setDatasource( ds );
		                                      sp.setProcedure( "sp_get_all" );
		                                      sp.addProcResult( name="rs1", resultset=1 );
		                                      result = sp.execute();
		                                      prefix = result.getPrefix();
		                                      hasExecTime = structKeyExists( prefix, "executionTime" );
		                                      """, dsn ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasExecTime" ) ) ).isTrue();
	}

}
