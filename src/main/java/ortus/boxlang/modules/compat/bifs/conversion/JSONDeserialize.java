package ortus.boxlang.modules.compat.bifs.conversion;

import ortus.boxlang.modules.compat.util.KeyDictionary;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.bifs.BoxMember;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.dynamic.casters.BooleanCaster;
import ortus.boxlang.runtime.modules.ModuleRecord;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.BoxLangType;

@BoxBIF
@BoxMember( type = BoxLangType.STRING )
public class JSONDeserialize extends ortus.boxlang.runtime.bifs.global.conversion.JSONDeserialize {

	public static final Key moduleName = Key.of( "compat-cfml" );

	/**
	 * Converts a JSON (JavaScript Object Notation) string data representation into data, such as a structure or array.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.json The JSON string to convert to data.
	 *
	 * @argument.strictMapping A Boolean value that specifies whether to convert the JSON strictly. If true, everything becomes structures.
	 *
	 * @argument.useCustomSerializer A string that specifies the name of a custom serializer to use. (Not used)
	 *
	 * @return The data representation of the JSON string.
	 */
	@Override
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		String			json					= arguments.getAsString( Key.json );
		ModuleRecord	moduleRecord			= moduleService.getModuleRecord( moduleName );
		Object			moduleSetting			= moduleRecord.settings.get( KeyDictionary.jsonEscapeControlCharacters );
		Boolean			escapeControlCharacters	= BooleanCaster
		    .attempt( moduleSetting )
		    .getOrDefault( false );

		if ( escapeControlCharacters ) {
			// Escape all control characters in the JSON string
			arguments.put( Key.json, escapeControlCharacters( json ) );
		}

		return super._invoke( context, arguments );
	}

	/**
	 * Escape all control characters in ASCII range 0-31 and
	 * convert them to their escaped representation.
	 *
	 * @param json The JSON string to escape control characters in.
	 *
	 * @return The JSON string with control characters escaped.
	 */
	private String escapeControlCharacters( String json ) {
		StringBuilder	escapedJson	= new StringBuilder();
		boolean			inQuotes	= false; // Flag to track if we're inside quotes (single or double)

		for ( int i = 0; i < json.length(); i++ ) {
			char c = json.charAt( i );

			// Check for single or double quotes and toggle the inQuotes flag
			if ( c == '\"' || c == '\'' ) {
				escapedJson.append( c );
				inQuotes = !inQuotes;
			} else if ( inQuotes && c < 32 ) {
				// Only escape control characters if inside quotes
				switch ( c ) {
					case 9 : // Tab
						escapedJson.append( "\\t" );
						break;
					case 10 : // Newline
						escapedJson.append( "\\n" );
						break;
					case 13 : // Carriage return
						escapedJson.append( "\\r" );
						break;
					case 12 : // Form feed
						escapedJson.append( "\\f" );
						break;
					default :
						escapedJson.append( String.format( "\\u%04x", ( int ) c ) );
						break;
				}
			} else {
				// If not in quotes, append character as-is
				escapedJson.append( c );
			}
		}

		return escapedJson.toString();
	}

}
