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
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Query;

@DisplayName( "new Query() Service Tests" )
public class QueryServiceTest extends BaseIntegrationTest {

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
	public static void setupQueryTests() {
		assumeTrue( BoxRuntime.getInstance().getModuleService().hasModule( Key.of( "mysql" ) ),
		    "bx-mysql module is not installed — skipping Query service tests" );
	}

	@BeforeEach
	public void setupTable() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      queryExecute( "DROP TABLE IF EXISTS bx_query_test", [], { datasource: ds } );
		                                      queryExecute( "
		                                          CREATE TABLE bx_query_test (
		                                              id INT AUTO_INCREMENT PRIMARY KEY,
		                                              name VARCHAR(100) NOT NULL,
		                                              role VARCHAR(100) NOT NULL
		                                          )
		                                      ", [], { datasource: ds } );
		                                      queryExecute( "INSERT INTO bx_query_test (name, role) VALUES ('Brad', 'Admin')", [], { datasource: ds } );
		                                      queryExecute( "INSERT INTO bx_query_test (name, role) VALUES ('Luis', 'Developer')", [], { datasource: ds } );
		                                      queryExecute( "INSERT INTO bx_query_test (name, role) VALUES ('Jon', 'Designer')", [], { datasource: ds } );
		                                      """, dsn ),
		    context );
	}

	@DisplayName( "It can execute a basic SELECT with new Query()" )
	@Test
	public void testBasicSelect() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test ORDER BY id" );
		                                      result = q.execute();
		                                      data = result.getResult();
		                                      prefix = result.getPrefix();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 3 );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.getAsInteger( Key.of( "recordCount" ) ) ).isEqualTo( 3 );
	}

	@DisplayName( "It can pass datasource as a struct in the constructor" )
	@Test
	public void testConstructorDatasource() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query( datasource=ds, sql="SELECT * FROM bx_query_test WHERE name='Brad'" );
		                                      result = q.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 1 );
	}

	@DisplayName( "It can use named parameters with addParam()" )
	@Test
	public void testNamedParams() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test WHERE name = :name" );
		                                      q.addParam( name="name", value="Luis", cfsqltype="cf_sql_varchar" );
		                                      result = q.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 1 );
	}

	@DisplayName( "It can use positional parameters with addParam()" )
	@Test
	public void testPositionalParams() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test WHERE role = ?" );
		                                      q.addParam( value="Developer", cfsqltype="cf_sql_varchar" );
		                                      result = q.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 1 );
	}

	@DisplayName( "It can execute an INSERT and return prefix metadata" )
	@Test
	public void testInsertWithPrefix() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "INSERT INTO bx_query_test (name, role) VALUES (:name, :role)" );
		                                      q.addParam( name="name", value="Jorge", cfsqltype="cf_sql_varchar" );
		                                      q.addParam( name="role", value="Tester", cfsqltype="cf_sql_varchar" );
		                                      result = q.execute();
		                                      prefix = result.getPrefix();
		                                      """, dsn ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix ).isNotNull();
	}

	@DisplayName( "It supports method chaining on addParam()" )
	@Test
	public void testMethodChaining() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test WHERE name = :name AND role = :role" );
		                                      q.addParam( name="name", value="Brad", cfsqltype="cf_sql_varchar" )
		                                       .addParam( name="role", value="Admin", cfsqltype="cf_sql_varchar" );
		                                      result = q.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 1 );
	}

	@DisplayName( "It can use setAttributes() for bulk configuration" )
	@Test
	public void testSetAttributes() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setAttributes( datasource=ds, sql="SELECT * FROM bx_query_test ORDER BY id" );
		                                      result = q.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 3 );
	}

	@DisplayName( "It can clear params and re-execute" )
	@Test
	public void testClearParams() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test WHERE name = :name" );
		                                      q.addParam( name="name", value="Brad", cfsqltype="cf_sql_varchar" );
		                                      result1 = q.execute();

		                                      q.clearParams();
		                                      q.setSQL( "SELECT * FROM bx_query_test ORDER BY id" );
		                                      result2 = q.execute();
		                                      data1 = result1.getResult();
		                                      data2 = result2.getResult();
		                                      """, dsn ),
		    context );

		Query	data1	= ( Query ) variables.get( Key.of( "data1" ) );
		Query	data2	= ( Query ) variables.get( Key.of( "data2" ) );
		assertThat( data1.size() ).isEqualTo( 1 );
		assertThat( data2.size() ).isEqualTo( 3 );
	}

	@DisplayName( "It can use maxrows to limit results" )
	@Test
	public void testMaxRows() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test ORDER BY id" );
		                                      q.setMaxrows( 2 );
		                                      result = q.execute();
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 2 );
	}

	@DisplayName( "It can override attributes at execute time" )
	@Test
	public void testOverrideOnExecute() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test ORDER BY id" );
		                                      result = q.execute( sql="SELECT * FROM bx_query_test WHERE name='Jon'" );
		                                      data = result.getResult();
		                                      """, dsn ),
		    context );

		Query data = ( Query ) variables.get( Key.of( "data" ) );
		assertThat( data.size() ).isEqualTo( 1 );
	}

	@DisplayName( "It can use getAttributes() and clearAttributes()" )
	@Test
	public void testGetAndClearAttributes() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT 1" );
		                                      attrs = q.getAttributes();
		                                      q.clearAttributes( "sql" );
		                                      attrsAfter = q.getAttributes();
		                                      hasDatasource = structKeyExists( attrsAfter, "datasource" );
		                                      hasSQL = structKeyExists( attrsAfter, "sql" );
		                                      """, dsn ),
		    context );

		IStruct attrs = ( IStruct ) variables.get( Key.of( "attrs" ) );
		assertThat( attrs.containsKey( Key.of( "datasource" ) ) ).isTrue();
		assertThat( attrs.containsKey( Key.of( "sql" ) ) ).isTrue();

		assertThat( ( Boolean ) variables.get( Key.of( "hasDatasource" ) ) ).isTrue();
		assertThat( ( Boolean ) variables.get( Key.of( "hasSQL" ) ) ).isFalse();
	}

	@DisplayName( "It can use clear() to reset everything" )
	@Test
	public void testClear() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test" );
		                                      q.addParam( name="name", value="Brad", cfsqltype="cf_sql_varchar" );
		                                      q.clear();
		                                      attrs = q.getAttributes();
		                                      attrCount = structCount( attrs );
		                                      """, dsn ),
		    context );

		assertThat( ( ( Number ) variables.get( Key.of( "attrCount" ) ) ).intValue() ).isEqualTo( 0 );
	}

	@DisplayName( "It supports implicit getters like getDatasource() and getSQL()" )
	@Test
	public void testImplicitGetters() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT 1" );
		                                      myDS = q.getDatasource();
		                                      mySQL = q.getSQL();
		                                      """, dsn ),
		    context );

		assertThat( variables.get( Key.of( "myDS" ) ) ).isNotNull();
		assertThat( variables.get( Key.of( "mySQL" ) ) ).isEqualTo( "SELECT 1" );
	}

	@DisplayName( "It can filter getAttributes() with a comma-separated list" )
	@Test
	public void testGetAttributesFiltered() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT 1" );
		                                      q.setMaxrows( 5 );
		                                      filtered = q.getAttributes( "sql,maxrows" );
		                                      hasSQL = structKeyExists( filtered, "sql" );
		                                      hasMaxrows = structKeyExists( filtered, "maxrows" );
		                                      hasDatasource = structKeyExists( filtered, "datasource" );
		                                      """, dsn ),
		    context );

		assertThat( ( Boolean ) variables.get( Key.of( "hasSQL" ) ) ).isTrue();
		assertThat( ( Boolean ) variables.get( Key.of( "hasMaxrows" ) ) ).isTrue();
		assertThat( ( Boolean ) variables.get( Key.of( "hasDatasource" ) ) ).isFalse();
	}

	@DisplayName( "It returns sqlparameters in the prefix metadata" )
	@Test
	public void testPrefixSqlParameters() {
		runtime.executeSource( String.format( """
		                                      ds = %s;
		                                      q = new Query();
		                                      q.setDatasource( ds );
		                                      q.setSQL( "SELECT * FROM bx_query_test WHERE name = :name" );
		                                      q.addParam( name="name", value="Brad", cfsqltype="cf_sql_varchar" );
		                                      result = q.execute();
		                                      prefix = result.getPrefix();
		                                      """, dsn ),
		    context );

		IStruct prefix = ( IStruct ) variables.get( Key.of( "prefix" ) );
		assertThat( prefix.containsKey( Key.of( "sql" ) ) ).isTrue();
		assertThat( prefix.containsKey( Key.of( "sqlParameters" ) ) ).isTrue();
	}

}
