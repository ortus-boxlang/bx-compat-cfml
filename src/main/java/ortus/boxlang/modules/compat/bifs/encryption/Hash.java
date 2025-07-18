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
package ortus.boxlang.modules.compat.bifs.encryption;

import java.io.IOException;

import com.fasterxml.jackson.jr.ob.JSONObjectException;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.dynamic.casters.IntegerCaster;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.BoxLangType;
import ortus.boxlang.runtime.types.IStruct;

@BoxBIF
@BoxBIF( alias = "Hash40" )
@BoxMember( type = BoxLangType.STRING_STRICT, name = "hash" )
@BoxMember( type = BoxLangType.STRUCT, name = "hash" )
@BoxMember( type = BoxLangType.ARRAY, name = "hash" )
@BoxMember( type = BoxLangType.DATETIME, name = "hash" )
public class Hash extends ortus.boxlang.runtime.bifs.global.encryption.Hash {

	private static final String		DEFAULT_ALGORITHM	= "MD5";
	private static final String		DEFAULT_ENCODING	= "utf-8";

	private static final IStruct	MODULE_SETTINGS		= BoxRuntime.getInstance().getModuleService().getModuleSettings( KeyDictionary.moduleName );

	/**
	 * Constructor
	 */
	public Hash() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, "any", Key.input ),
		    new Argument( false, "string", Key.algorithm, DEFAULT_ALGORITHM ),
		    new Argument( false, "string", Key.encoding, DEFAULT_ENCODING ),
		    // we null default this in the override so we can handle the engine differences in how iterations are applied
		    new Argument( false, "integer", Key.numIterations )
		};
	}

	/**
	 * Creates an algorithmic hash of an object and returns it in the CFML compat upper case format
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @throws IOException
	 * @throws JSONObjectException
	 *
	 * @argument.input The item to be hashed
	 *
	 * @argument.algorithm The supported {@link java.security.MessageDigest } algorithm (case-insensitive) or "quick" for an insecure 64-bit hash
	 *
	 * @argument.encoding Applicable to strings ( default "utf-8" )
	 *
	 * @argument.iterations The number of iterations to re-digest the object ( default 1 );
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		Object	iterations	= arguments.get( Key.numIterations );
		Integer	appliedIterations;
		if ( iterations == null ) {
			appliedIterations = 1;
		} else if ( BooleanCaster.cast( MODULE_SETTINGS.getOrDefault( KeyDictionary.isAdobe, false ) ) ) {
			appliedIterations = IntegerCaster.cast( iterations ) + 1; // Adobe engines default to 0 iterations and each iteration is an additional digest
		} else {
			appliedIterations = IntegerCaster.cast( iterations );
		}

		arguments.put( Key.numIterations, appliedIterations );

		return StringCaster.cast( super._invoke( context, arguments ) ).toUpperCase();
	}
}
