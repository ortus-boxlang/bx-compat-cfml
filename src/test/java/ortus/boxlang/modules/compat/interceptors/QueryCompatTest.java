package ortus.boxlang.modules.compat.interceptors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class QueryCompatTest extends BaseIntegrationTest {

	@DisplayName( "It converts zero timeouts to negative timeouts" )
	@Test
	public void testZeroCacheTimeout() {
		// @formatter:off
		runtime.executeSource(
		    """
				import java:ortus.boxlang.runtime.jdbc.QueryOptions;
				import src.main.bx.interceptors.QueryCompat;
				options = new QueryOptions({
					cache       : true,
					cacheTimeout: createTimeSpan( 0, 0, 0, 0 )
				});

				interceptData = { bindings : [], options : options };
				new QueryCompat().onQueryBuild( interceptData );
				assert interceptData.options.cacheTimeout < 0;
			   """,
		    context );
		// @formatter:on
	}

	@DisplayName( "Query map turns string null values to empty strings" )
	@Test
	public void testQueryMapTurnsStringNullsToEmpty() {
		// @formatter:off
		runtime.executeSource(
		    """
				q = queryNew("col1,col2,col3", "varchar,varchar,integer")
				q.addRow( { col1: "nullsy" } )
				q.addRow( { col1: "not nullsy", col2 : "", col3 : 123 } )
				assert q.recordCount == 2 : "Expected 2 records in the query result, but got " & q.recordCount;

				q2 = queryExecute( "select * from q where col2 = null", [], { dbType : "query" } );				
				assert q2.recordCount == 1 : "Expected 1 records in the QoQ query result, but got " & q2.recordCount;

				// This turns nulls into empty strings in the original query since the row struct has empties
				q = q.map( (row)=>row )
				
				assert q.recordCount == 2 : "Expected 2 records in the query result, but got " & q.recordCount;
				q2 = queryExecute( "select * from q where col2 = null", [], { dbType : "query" } );

				// the string null in col2 got turned into an empty string
				assert q2.recordCount == 0 : "Expected 0 records in the QoQ query result, but got " & q2.recordCount;
				
				q2 = queryExecute( "select * from q where col3 = null", [], { dbType : "query" } );
				// the integer null in col3 remains as null
				assert q2.recordCount == 1 : "Expected 1 record in the QoQ query result, but got " & q2.recordCount;
			   """,
		    context );
		// @formatter:on
	}

	@DisplayName( "Query filter retains all original nulls" )
	@Test
	public void testQueryFilterRetainsOriginalNulls() {
		// @formatter:off
		runtime.executeSource(
		    """
				q = queryNew("col1,col2,col3", "varchar,varchar,integer")
				q.addRow( { col1: "nullsy" } )
				q.addRow( { col1: "not nullsy", col2 : "", col3 : 123 } )
				assert q.recordCount == 2 : "Expected 2 records in the query result, but got " & q.recordCount;

				q2 = queryExecute( "select * from q where col2 = null", [], { dbType : "query" } );				
				assert q2.recordCount == 1 : "Expected 1 records in the QoQ query result, but got " & q2.recordCount;

				// This turns nulls into empty strings in the original query since the row struct has empties
				q = q.filter( ()=>true )
				
				assert q.recordCount == 2 : "Expected 2 records in the query result, but got " & q.recordCount;
				q2 = queryExecute( "select * from q where col2 = null", [], { dbType : "query" } );

				// The string null in col2 remains as null since filter does not modify the row data
				assert q2.recordCount == 1 : "Expected 1 record in the QoQ query result, but got " & q2.recordCount;
				
				q2 = queryExecute( "select * from q where col3 = null", [], { dbType : "query" } );
				// The integer null in col3 remains as null since filter does not modify the row data
				assert q2.recordCount == 1 : "Expected 1 record in the QoQ query result, but got " & q2.recordCount;
			   """,
		    context );
		// @formatter:on
	}

	@DisplayName( "Query cells with null show as empty strings no matter how you get them" )
	@Test
	public void testQueryCellsWithNullShowAsNull() {
		// @formatter:off
		runtime.executeSource(
		    """
				q = queryNew("col1,col2,col3", "varchar,varchar,integer");
				q.addRow( { col1: "test" } );
				assert !isNull( q.col2 ) && q.col2 == "" : "Expected q.col2 to be an empty string, but got " & q.col2;
				assert !isNull( q.col3 ) && q.col3 == "" : "Expected q.col3 to be an empty string, but got " & q.col3;
				
				assert !isNull( q["col2"] ) && q["col2"] == "" : "Expected q.col2 to be an empty string, but got " & q["col2"];
				assert !isNull( q["col3"] ) && q["col3"] == "" : "Expected q.col3 to be an empty string, but got " & q["col3"];
				
				assert !isNull( q["col2"][1] ) && q["col2"][1] == "" : "Expected q.col2 to be an empty string, but got " & q["col2"][1];
				assert !isNull( q["col3"][1] ) && q["col3"][1] == "" : "Expected q.col3 to be an empty string, but got " & q["col3"][1];

				
				assert !isNull( q.columnData("col2")[1] ) && q.columnData("col2")[1] == "" : "Expected q.col2 to be an empty string, but got " & q.columnData("col2")[1];
				assert !isNull( q.columnData("col3")[1] ) && q.columnData("col3")[1] == "" : "Expected q.col3 to be an empty string, but got " & q.columnData("col3")[1];

				assert arrayReduce( q["col2"], (acc="",val)=>acc &= (val?:"<null>") ) == "" : "Expected array BIF to get empty string for col2";
				assert arrayReduce( q["col3"], (acc="",val)=>acc &= (val?:"<null>") ) == "" : "Expected array BIF to get empty string for col3";
			   """,
		    context );
		// @formatter:on
	}

}
