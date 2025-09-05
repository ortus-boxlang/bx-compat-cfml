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
package ortus.boxlang.modules.compat.bifs.decision;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.jr.annotationsupport.JacksonAnnotationExtension;
import com.fasterxml.jackson.jr.extension.javatime.JacksonJrJavaTimeExtension;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JacksonJrExtension;
import com.fasterxml.jackson.jr.ob.api.ExtensionContext;

import ortus.boxlang.modules.compat.util.SettingsUtil;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.CastAttempt;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.util.conversion.BoxJsonProvider;

@BoxBIF
public class IsJSON extends ortus.boxlang.runtime.bifs.global.decision.IsJSON {

	/**
	 * Evaluates whether a string is in valid JSON (JavaScript Object Notation) data interchange format.
	 *
	 * @argument.var The value to test for JSON
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope defining the value to test.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {

		if ( !SettingsUtil.isLucee() ) {
			return super._invoke( context, arguments );
		}

		Object				oInput			= arguments.get( Key.var );
		CastAttempt<String>	stringAttempt	= StringCaster.attempt( oInput );
		if ( !stringAttempt.wasSuccessful() ) {
			return false;
		}
		// Lucee JSON is very permissive so we need to be also
		try {
			JSON.builder(
			    new JsonFactory()
			        .setStreamWriteConstraints(
			            StreamWriteConstraints.builder()
			                .maxNestingDepth( Integer.MAX_VALUE )
			                .build()
			        )
			        .enable( JsonParser.Feature.ALLOW_COMMENTS )
			        .enable( JsonParser.Feature.ALLOW_YAML_COMMENTS )
			        // TODO: This whole block needs to be converted over to use the JsonFactory.builder() as the following feature is deprecated
			        .enable( JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER )
			        // Lucee compatibility
			        .enable( JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES )
			        .enable( JsonParser.Feature.ALLOW_SINGLE_QUOTES )
			)
			    // Enable JSON features
			    // https://fasterxml.github.io/jackson-jr/javadoc/jr-objects/2.8/com/fasterxml/jackson/jr/ob/JSON.Feature.html
			    .enable(
			        JSON.Feature.USE_BIG_DECIMAL_FOR_FLOATS,
			        JSON.Feature.USE_FIELDS,
			        JSON.Feature.WRITE_NULL_PROPERTIES
			    )
			    // Add Jackson annotation support
			    .register( JacksonAnnotationExtension.std )
			    // Add JavaTime Extension
			    .register( new JacksonJrJavaTimeExtension() )
			    // Add Custom Serializers/ Deserializers
			    .register( new JacksonJrExtension() {

				    @Override
				    protected void register( ExtensionContext extensionContext ) {
					    extensionContext.insertProvider( new BoxJsonProvider() );
				    }

			    } )
			    // Yeaaaahaaa!
			    .build().anyFrom( stringAttempt.get() );
			return true;
		} catch ( Exception e ) {
			return false;
		}

	}
}
