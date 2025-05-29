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
 * Event handler for Scheduler tasks Events.
 */
Interface
{     /**
       * Called when job is about to be executed.If this returns false, CF will veto the job and wont execute it
       */
	public boolean function onTaskStart(Struct context);

    /**
     * Called once execution of the task is over
     */
	public void function onTaskEnd(Struct context);

   /**
     * Called when a task gets misfired
     */
	public void function onMisfire(Struct context);

    /**
     * Called when a task throws an runtime exception
     */
	public void function onError(Struct context);

    /**
     * Called when URL is not specified.Instead this method will be invoked on scheduled times
     */
	public void function execute(Struct context);
}
