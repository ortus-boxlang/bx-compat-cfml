/**
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
