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
 * ----
 * Event handler for ORM Events. This should be used as a global application wide handler that can be set in the application
 * using ormsettings.eventHandler=MyEventHandler. These events can be handled by the application to perform any pre or post
 * actions for all ORM operations.
 */
Interface
{
	/**
     * Called before injecting property values into a newly loaded entity instance.
	 */
	public void function preLoad(any entity);

    /**
     * Called after an entity is fully loaded.
     */
	public void function postLoad(any entity);

   /**
    * Called before inserting the enetity into the database.
    */
	public void function preInsert(any entity);

    /**
     * Called after the entity is inserted into the database.
     */
	public void function postInsert(any entity);

    /**
     * Called before the entity is updated in the database.
     */
    public void function preUpdate(any entity, Struct oldData);

    /**
     * Called after the entity is updated in the database.
     */
    public void function postUpdate(any entity);


    /**
     * Called before the entity is deleted from the database.
     */
    public void function preDelete(any entity);

    /**
     * Called after deleting an item from the datastore
     */
    public void function postDelete(any entity);

    /**
     * Called before the session is flushed.
     */
    public void function preFlush(any entities);

    /**
     * Called after the session is flushed.
     */
    public void function postFlush(any entities);

}
