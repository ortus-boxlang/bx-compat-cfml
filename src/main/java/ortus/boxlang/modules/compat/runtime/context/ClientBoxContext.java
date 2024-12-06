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
package ortus.boxlang.modules.compat.runtime.context;

import ortus.boxlang.runtime.context.BaseBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.exceptions.ScopeNotFoundException;

/**
 * This class represents the context of a client in the BoxLang runtime
 * It is a child of the RuntimeBoxContext and has access to the client scope
 * and the parent context and its scopes
 */
public class ClientBoxContext extends BaseBoxContext {

	/**
	 * --------------------------------------------------------------------------
	 * Private Properties
	 * --------------------------------------------------------------------------
	 */

	/**
	 * The variables scope
	 */
	protected Client	client;

	/**
	 * The client scope for this application
	 */
	protected IScope	clientScope;

	/**
	 * --------------------------------------------------------------------------
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Creates a new execution context with a bounded execution template and parent context
	 *
	 * @param client The client for this context
	 */
	public ClientBoxContext( Client client ) {
		super( null );
		this.client			= client;
		this.clientScope	= client.getClientScope();
	}

	/**
	 * --------------------------------------------------------------------------
	 * Helper Methods
	 * --------------------------------------------------------------------------
	 */

	/**
	 * Get the client for this context
	 *
	 * @return The client for this context
	 */
	public Client getClient() {
		return this.client;
	}

	/**
	 * Update the client for this context with a new client
	 *
	 * @param client The new client to use
	 *
	 * @return This context
	 */
	public ClientBoxContext updateClient( Client client ) {
		this.client			= client;
		this.clientScope	= client.getClientScope();
		return this;
	}

	/**
	 * --------------------------------------------------------------------------
	 * Interface Methods
	 * --------------------------------------------------------------------------
	 */

	/**
	 * @inheritDoc
	 */
	@Override
	public IStruct getVisibleScopes( IStruct scopes, boolean nearby, boolean shallow ) {
		if ( hasParent() && !shallow ) {
			getParent().getVisibleScopes( scopes, false, false );
		}
		scopes.getAsStruct( Key.contextual ).put( ClientScope.name, clientScope );
		return scopes;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ScopeSearchResult scopeFindNearby( Key key, IScope defaultScope, boolean shallow, boolean forAssign ) {

		// There are no near-by scopes in the client context. Everything is global here.

		if ( shallow ) {
			return null;
		}

		return scopeFind( key, defaultScope, forAssign );
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ScopeSearchResult scopeFind( Key key, IScope defaultScope, boolean forAssign ) {
		if ( key.equals( clientScope.getName() ) ) {
			return new ScopeSearchResult( clientScope, clientScope, key, true );
		}

		return parent.scopeFind( key, defaultScope, forAssign );
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public IScope getScope( Key name ) throws ScopeNotFoundException {

		// Check the scopes I know about
		if ( name.equals( clientScope.getName() ) ) {
			return clientScope;
		}

		return parent.getScope( name );
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public IScope getScopeNearby( Key name, boolean shallow ) throws ScopeNotFoundException {

		if ( shallow ) {
			return null;
		}

		// The RuntimeBoxContext has no "nearby" scopes
		return getScope( name );
	}

}
