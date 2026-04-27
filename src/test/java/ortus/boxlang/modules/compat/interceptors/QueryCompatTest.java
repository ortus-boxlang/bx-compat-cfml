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
				assert interceptData.options.cache == false;
				assert interceptData.options.cacheTimeout < 0;
			   """,
		    context );
		// @formatter:on
	}
}
