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
package ortus.boxlang.modules.compat.util;

import ortus.boxlang.runtime.scopes.Key;

public class KeyDictionary {

	public static final Key	bxClients					= Key.of( "bxClients" );
	public static final Key	filterOrTags				= Key.of( "filterOrTags" );
	public static final Key	isKey						= Key.of( "isKey" );
	public static final Key	objectType					= Key.of( "objectType" );
	public static final Key	moduleName					= Key.of( "compat-cfml" );
	public static final Key	jsonEscapeControlCharacters	= Key.of( "jsonEscapeControlCharacters" );
	public static final Key	nameSpaceWithSeperator		= Key.of( "nameSpaceWithSeperator" );
	public static final Key	tagName						= Key.of( "tagName" );
	public static final Key	hitCount					= Key.of( "hitCount" );
	public static final Key	functionName				= Key.of( "functionName" );
	public static final Key	client						= Key.of( "client" );
	public static final Key	clientStorage				= Key.of( "clientStorage" );
	public static final Key	clientTimeout				= Key.of( "clientTimeout" );
	public static final Key	ON_CLIENT_CREATED			= Key.of( "onClientCreated" );
	public static final Key	ON_CLIENT_DESTROYED			= Key.of( "onClientDestroyed" );

}
