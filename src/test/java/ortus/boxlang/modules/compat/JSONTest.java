package ortus.boxlang.modules.compat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This loads the module and runs an integration test on the module.
 */
public class JSONTest extends BaseIntegrationTest {

	@DisplayName( "Test control characters in JSON" )
	@Test
	public void testJSONControlCharacters() {
		// Given
		loadModule();

		// @formatter:off
		runtime.executeSource( """
			seed = '{
						"COLUMNS":
							["EIN","FIRST NAME","LAST NAME"," EMAIL","MANAGER EIN","MANAGER NAME","MANAGER:EMAIL","TEAM"," COMPANY"," GENDER ","N-LEVEL","COUNTRY","LETTER REQUIRED!","	TEMPLATETOSEND","^INVITATION METHOD$","!COMPLETION@METHOD%"],
						"DATA":
							[
								["01234","Amy","Adams","a-a@anyco.co.uk","","","","CEO","Any Co","Male","","United Kingdom","","Batch 2 - UK & I ","Online","Online"],
								[56789,"Barry","Bocum","b.b@anyco.co.uk","01234","Charlie Chalk","c.c@anyco.co.uk","Division","Division  Ireland","Male",-5.0,"Ireland","","Batch 2 - UK & I ","Online","Online"]
							]
					}';
			result = jsonDeserialize( seed, false );
			"""
			, context );
		// @formatter:on

	}
}
