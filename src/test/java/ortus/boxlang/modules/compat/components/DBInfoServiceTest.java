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

@DisplayName( "new dbinfo() Service Tests" )
public class DBInfoServiceTest extends BaseIntegrationTest {

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
	public static void setupDBInfoTests() {
		assumeTrue( BoxRuntime.getInstance().getModuleService().hasModule( Key.of( "mysql" ) ),
		    "bx-mysql module is not installed — skipping DBInfo service tests" );
	}

	@BeforeEach
	public void setupTable() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      queryExecute( "DROP TABLE IF EXISTS bx_dbinfo_test", [], { datasource: ds } );
		                                      queryExecute( "
		                                          CREATE TABLE bx_dbinfo_test (
		                                              id INT AUTO_INCREMENT PRIMARY KEY,
		                                              name VARCHAR(100) NOT NULL
		                                          )
		                                      ", [], { datasource: ds } );
		                                      """, dsn ),
		    context );
	}

	@DisplayName( "It can create a DBInfo service and set attributes" )
	@Test
	public void testImplicitSetters() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      dbService = new dbinfo();
		                                      dbService.setDatasource( ds );
		                                      dbService.setType( "tables" );
		                                      attrs = dbService.getAttributes();
		                                      hasDatasource = structKeyExists( attrs, "datasource" );
		                                      hasType = structKeyExists( attrs, "type" );
		                                      """, dsn ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasDatasource" ) ) ).isTrue();
		assertThat( variables.getAsBoolean( Key.of( "hasType" ) ) ).isTrue();
	}

	@DisplayName( "It can initialize attributes in the constructor" )
	@Test
	public void testConstructorAttributes() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      dbService = new dbinfo( datasource=ds, type="tables" );
		                                      attrs = dbService.getAttributes();
		                                      hasType = structKeyExists( attrs, "type" );
		                                      """, dsn ),
		    context );

		assertThat( variables.getAsBoolean( Key.of( "hasType" ) ) ).isTrue();
	}

	@DisplayName( "It can execute with type=tables" )
	@Test
	public void testExecuteTables() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      dbService = new dbinfo();
		                                      dbService.setDatasource( ds );
		                                      dbService.setType( "tables" );
		                                      result = dbService.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data ).isNotNull();
		assertThat( data.size() ).isGreaterThan( 0 );
	}

	@DisplayName( "It can execute with type=columns" )
	@Test
	public void testExecuteColumns() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      dbService = new dbinfo();
		                                      dbService.setDatasource( ds );
		                                      dbService.setType( "columns" );
		                                      dbService.setTable( "bx_dbinfo_test" );
		                                      result = dbService.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data ).isNotNull();
		assertThat( data.size() ).isGreaterThan( 0 );
	}

	@DisplayName( "It can use setAttributes() for bulk configuration" )
	@Test
	public void testSetAttributes() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      dbService = new dbinfo();
		                                      dbService.setAttributes( datasource=ds, type="tables" );
		                                      result = dbService.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data ).isNotNull();
	}

	@DisplayName( "It can use clear() to reset everything" )
	@Test
	public void testClear() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      dbService = new dbinfo();
		                                      dbService.setDatasource( ds );
		                                      dbService.setType( "tables" );
		                                      dbService.clear();
		                                      attrs = dbService.getAttributes();
		                                      attrCount = structCount( attrs );
		                                      """, dsn ),
		    context );

		assertThat( ( ( Number ) variables.get( Key.of( "attrCount" ) ) ).intValue() ).isEqualTo( 0 );
	}

	@DisplayName( "It supports method chaining on setters" )
	@Test
	public void testMethodChaining() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      dbService = new dbinfo();
		                                      dbService.setDatasource( ds )
		                                          .setType( "tables" );
		                                      result = dbService.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data ).isNotNull();
	}

}
