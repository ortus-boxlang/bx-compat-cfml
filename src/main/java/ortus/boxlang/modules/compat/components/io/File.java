package ortus.boxlang.modules.compat.components.io;

import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.ExpressionInterpreter;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

public class File extends ortus.boxlang.runtime.components.io.File {

	private static final String	VARIABLE_STRING		= "bxfile";
	private static final String	FILE_STRING			= "file";
	private static final String	FILE_UPLOAD_STRING	= "fileupload";

	/**
	 * Manages interactions with server files. Different combinations cause different attributes to be required.
	 *
	 * @param context        The context in which the Component is being invoked
	 * @param attributes     The attributes to the Component
	 * @param body           The body of the Component
	 * @param executionState The execution state of the Component
	 *
	 * @attribute.action The action to take. One of: append, copy, delete, move, read, readbinary, rename, upload, uploadall, write
	 *
	 * @attribute.file The file to act on
	 *
	 * @attribute.mode The mode to open the file in
	 *
	 * @attribute.output The output of the action
	 *
	 * @attribute.addnewline Add a newline to the end of the file
	 *
	 * @attribute.attributes Attributes to set on the file
	 *
	 * @attribute.charset The character set to use
	 *
	 * @attribute.source The source file
	 *
	 * @attribute.destination The destination file
	 *
	 * @attribute.variable The variable to store the result in
	 *
	 * @attribute.filefield The file field to use
	 *
	 * @attribute.nameconflict What to do if there is a name conflict
	 *
	 * @attribute.accept The accept header
	 *
	 * @attribute.result The result of the action
	 *
	 * @attribute.fixnewline Fix newlines
	 *
	 * @attribute.cachedwithin The time to cache the file within
	 *
	 */
	public BodyResult _invoke( IBoxContext context, IStruct attributes, ComponentBody body, IStruct executionState ) {
		BodyResult	result			= super._invoke( context, attributes, body, executionState );
		Key			action			= Key.of( attributes.getAsString( Key.action ) );
		Object		resultVariable	= ExpressionInterpreter.getVariable( context, VARIABLE_STRING, true );

		if ( action.getName().toLowerCase().startsWith( FILE_UPLOAD_STRING ) && resultVariable != null ) {
			ExpressionInterpreter.setVariable(
			    context,
			    FILE_STRING,
			    resultVariable
			);
		}

		return result;

	}

}
