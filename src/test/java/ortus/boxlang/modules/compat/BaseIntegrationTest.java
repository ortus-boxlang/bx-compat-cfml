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
package ortus.boxlang.modules.compat;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.modules.ModuleRecord;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.services.ModuleService;

public class BaseIntegrationTest {

	protected static BoxRuntime				runtime;
	protected static ModuleService			moduleService;
	protected static Key					result		= new Key( "result" );
	protected static Key					moduleName	= new Key( "compat-cfml" );
	protected ScriptingRequestBoxContext	context;
	protected IScope						variables;

	@BeforeAll
	public static void setup() {
		runtime			= BoxRuntime.getInstance( true );
		moduleService	= runtime.getModuleService();
	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext();
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@SuppressWarnings( "unused" )
	protected void loadModule() {
		String			physicalPath	= Paths.get( "./build/module" ).toAbsolutePath().toString();
		ModuleRecord	moduleRecord	= new ModuleRecord( physicalPath );

		// When
		moduleRecord
		    .loadDescriptor( context )
		    .register( context )
		    .activate( context );

		moduleService.getRegistry().put( moduleName, moduleRecord );
	}

}
