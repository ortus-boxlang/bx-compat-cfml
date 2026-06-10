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
package ortus.boxlang.modules.compat.bifs.conversion;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.modules.compat.BaseIntegrationTest;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

public class SerializeTest extends BaseIntegrationTest {

	@DisplayName( "It can serialize with default JSON type" )
	@Test
	void testSerializeDefaultJSON() {
		runtime.executeSource(
		    """
		    person = [ "firstName" : "John", "lastName" : "Doe" ];
		    result = reReplace( serialize( person ), "\\s", "", "all" );
		    """,
		    context );
		assertThat( variables.getAsString( result ) ).isEqualTo( "{\"firstName\":\"John\",\"lastName\":\"Doe\"}" );
	}

	@DisplayName( "It can serialize to explicit JSON type" )
	@Test
	void testSerializeExplicitJSONType() {
		runtime.executeSource(
		    """
		    person = [ "firstName" : "John", "lastName" : "Doe" ];
		    result = reReplace( serialize( person, "json" ), "\\s", "", "all" );
		    """,
		    context );
		assertThat( variables.getAsString( result ) ).isEqualTo( "{\"firstName\":\"John\",\"lastName\":\"Doe\"}" );
	}

	@DisplayName( "It throws unsupported for XML type" )
	@Test
	void testSerializeXMLUnsupported() {
		Throwable t = assertThrows(
		    BoxRuntimeException.class,
		    () -> runtime.executeSource(
		        """
		        serialize( { "firstName" : "John", "lastName" : "Doe" }, "xml" );
		        """,
		        context )
		);
		assertThat( t.getMessage().toLowerCase() ).contains( "supported" );
	}

	@DisplayName( "It throws unsupported for custom serializer" )
	@Test
	void testSerializeCustomSerializerUnsupported() {
		Throwable t = assertThrows(
		    BoxRuntimeException.class,
		    () -> runtime.executeSource(
		        """
		        serialize( { "firstName" : "John", "lastName" : "Doe" }, "json", true );
		        """,
		        context )
		);
		assertThat( t.getMessage().toLowerCase() ).contains( "supported" );
	}

	@DisplayName( "It serializes queries with COLUMNS and DATA" )
	@Test
	void testSerializeQueryJSON() {
		runtime.executeSource(
		    """
		    result = lCase( reReplace(
		        serialize(
		            queryNew(
		                "col1,col2",
		                "varchar,varchar",
		                [
		                    ["cell1a","cell2a"],
		                    ["cell1b","cell2b"]
		                ]
		            ),
		            "JSON"
		        ),
		        "\\s",
		        "",
		        "all"
		    ) );
		    """,
		    context );

		assertThat( variables.getAsString( result ) )
		    .isEqualTo( "{\"columns\":[\"col1\",\"col2\"],\"data\":[[\"cell1a\",\"cell2a\"],[\"cell1b\",\"cell2b\"]]}" );
	}
}
