package ortus.boxlang.modules.compat.bifs.system;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.types.Struct;

public class GetComponentMetadataTest {

	static BoxRuntime	instance;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= new Key( "result" );

	@BeforeAll
	public static void setUp() {
		instance = BoxRuntime.getInstance( true );
	}

	@AfterAll
	public static void teardown() {

	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext( instance.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@DisplayName( "It can get the metadata of a bx class" )
	@Test
	public void testAPath() {
		instance.executeSource(
		    """
		    result = getComponentMetadata( "src.test.resources.bx.MyClass" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Struct.class );
	}

	@DisplayName( "It can get the metadata of a cfc class" )
	@Test
	public void testBPath() {
		instance.executeSource(
		    """
		    result = getComponentMetadata( "src.test.resources.cf.MyClassCF" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Struct.class );
	}

	@DisplayName( "It can get the metadata of a cfc instance" )
	@Test
	public void testCPath() {
		instance.executeSource(
		    """
		    result = getComponentMetadata( new src.test.resources.cf.MyClassCF() );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( Struct.class );
	}

}
