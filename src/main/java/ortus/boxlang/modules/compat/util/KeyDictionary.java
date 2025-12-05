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

/**
 * A dictionary of keys used in the Ortus BoxLang Compat module.
 *
 * @author Ortus Solutions, Corp.
 *
 * @since 1.0.0
 */
public class KeyDictionary {

	public static final Key	blockedExtForFileUpload		= Key.of( "blockedExtForFileUpload" );
	public static final Key	bxClients					= Key.of( "bxClients" );
	public static final Key	client						= Key.of( "client" );
	public static final Key	clientManagement			= Key.of( "clientManagement" );
	public static final Key	clientStorage				= Key.of( "clientStorage" );
	public static final Key	clientTimeout				= Key.of( "clientTimeout" );
	public static final Key	filterOrTags				= Key.of( "filterOrTags" );
	public static final Key	functionName				= Key.of( "functionName" );
	public static final Key	hitCount					= Key.of( "hitCount" );
	public static final Key	isAdobe						= Key.of( "isAdobe" );
	public static final Key	isKey						= Key.of( "isKey" );
	public static final Key	isLucee						= Key.of( "isLucee" );
	public static final Key	jsonEscapeControlCharacters	= Key.of( "jsonEscapeControlCharacters" );
	public static final Key	LUCEE_UPLOAD_BLOCKLIST		= Key.of( "LUCEE_UPLOAD_BLOCKLIST" );
	public static final Key	moduleName					= Key.of( "compat-cfml" );
	public static final Key	nameSpaceWithSeperator		= Key.of( "nameSpaceWithSeperator" );
	public static final Key	objectType					= Key.of( "objectType" );
	public static final Key	ON_CLIENT_CREATED			= Key.of( "onClientCreated" );
	public static final Key	ON_CLIENT_DESTROYED			= Key.of( "onClientDestroyed" );
	public static final Key	onServerStart				= Key.of( "onServerStart" );
	public static final Key	queryNullToEmpty			= Key.of( "queryNullToEmpty" );
	public static final Key	serverStartEnabled			= Key.of( "serverStartEnabled" );
	public static final Key	serverStartPath				= Key.of( "serverStartPath" );
	public static final Key	tagName						= Key.of( "tagName" );
	public static final Key	throwWhenNotExist			= Key.of( "throwWhenNotExist" );
	public static final Key	transpiler					= Key.of( "transpiler" );
	public static final Key	upperCaseKeys				= Key.of( "upperCaseKeys" );

	// ORM keys
	public static final Key	autoManageSession			= Key.of( "autoManageSession" );
	public static final Key	cfclocation					= Key.of( "cfclocation" );
	public static final Key	entityPaths					= Key.of( "entityPaths" );
	public static final Key	EVENT_ORM_POST_CONFIG_LOAD	= Key.of( "ORMPostConfigLoad" );
	public static final Key	EVENT_ORM_PRE_CONFIG_LOAD	= Key.of( "ORMPreConfigLoad" );
	public static final Key	flushAtRequestEnd			= Key.of( "flushAtRequestEnd" );
	public static final Key	ignoreParseErrors			= Key.of( "ignoreParseErrors" );
	public static final Key	skipCFCWithError			= Key.of( "skipCFCWithError" );
}
