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
package ortus.boxlang.modules.compat.bifs.system;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.components.ComponentDescriptor;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.exceptions.BoxValidationException;

@BoxBIF
public class GetTagData extends BIF {

	/**
	 * Constructor
	 */
	public GetTagData() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, KeyDictionary.nameSpaceWithSeperator ),
		    new Argument( true, Argument.STRING, KeyDictionary.tagName )
		};
	}

	/**
	 * Lucee compat: Return information to a Tag as Struct
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.namespaceWithSeperator The namespace of the tag (Ex: cf)
	 *
	 * @argument.tagName The name of the tag (Ex: mail, http, dbinfo, etc)
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		// Namespace is ignored, they never completed the other ones in lucee
		String				tagName		= arguments.getAsString( KeyDictionary.tagName );
		ComponentDescriptor	descriptor	= componentService.getComponent( tagName );

		if ( descriptor == null ) {
			// Throw Validation Exception
			throw new BoxValidationException( "Tag not found: " + tagName );
		}

		return Struct.of(
		    "nameSpaceSeperator", "",
		    "nameSpace", "cf",
		    "name", tagName,
		    "description", "",
		    "status", "implemented",
		    "attributeType", "fixed",
		    "parseBody", descriptor.allowsBody(),
		    "bodyType", descriptor.requiresBody() ? "required" : "free",
		    "attrMin", 0,
		    "attrMax", 0,
		    "hasNameAppendix", false,
		    "attributeCollection", true,
		    "type", "java",
		    "attributes", descriptor.getAttributes()
		);
	}

}
