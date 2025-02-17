package ortus.boxlang.modules.compat.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.compiler.parser.BoxSourceType;
import ortus.boxlang.modules.compat.BaseIntegrationTest;

public class ObjectCacheTest extends BaseIntegrationTest {

	@DisplayName( "It can call cfobjectcache" )
	@Test
	public void testComponent() {
		// @formatter:off
		assertDoesNotThrow( () -> {
			runtime.executeSource(
		    """
		      	bx:objectcache;
				bx:objectcache action="clear";
		    """,
		    context );
		});
		// @formatter:on
	}

	@DisplayName( "It can call cfobjectcache in cf mode" )
	@Test
	public void testComponentInCF() {
		// @formatter:off
		assertDoesNotThrow( () -> {
			runtime.executeSource(
		    """
				<cfobjectcache>
				<cfobjectcache action="clear">
		    """,
		    context,
			BoxSourceType.CFTEMPLATE );
		});
		// @formatter:on
	}

}
