# AGENTS.md

## Overview

`bx-compat-cfml` is a BoxLang compatibility module that provides CFML (ColdFusion/Lucee) compatibility for BoxLang applications. It contributes BIFs (Built-In Functions), tags, components, interceptors, and runtime behaviors to bridge the gap between legacy CFML engines and BoxLang.

## Build & Development Commands

### Setup

```bash
# Download the BoxLang core JAR for compilation
./gradlew downloadBoxLang
```

### Build

```bash
# Full build (compile, test, package)
./gradlew build

# Compile only
./gradlew compileJava

# Create the shadow (fat) JAR + module structure
./gradlew shadowJar

# Create the distributable module structure (JAR + BX sources + metadata)
./gradlew createModuleStructure
```

### Testing

```bash
# Run all tests
./gradlew test

# With debug output
./gradlew test --stacktrace --console=plain

# Run a specific test class
./gradlew test --tests "ortus.boxlang.modules.compat.bifs.cache.CacheGetTest"

# Run a specific test method
./gradlew test --tests "ortus.boxlang.modules.compat.bifs.cache.CacheGetTest.testCacheGet"

# First-time or after clean: needs BoxLang JAR available (downloadBoxLang)
./gradlew downloadBoxLang && ./gradlew shadowJar test --stacktrace --console=plain
```

### Formatting

```bash
# Check Java formatting (Spotless)
./gradlew spotlessCheck

# Auto-format Java
./gradlew spotlessApply

# Check CFML/BX formatting (requires CommandBox + cfformat)
box cfformat check

# Auto-format CFML/BX
box cfformat format
```

### Versioning

```bash
./gradlew bumpMajorVersion
./gradlew bumpMinorVersion
./gradlew bumpPatchVersion
```

## Project Structure

```
src/
├── main/
│   ├── bx/                    # BoxLang source files (.bx, .cfc)
│   │   ├── ModuleConfig.bx    # Module entry point (configure, onLoad, onUnload)
│   │   ├── bifs/              # BoxLang-implemented BIFs
│   │   ├── CFIDE/             # CFML IDE compatibility (ORM, scheduler)
│   │   ├── components/        # Tag/component implementations
│   │   ├── interceptors/      # BoxLang-implemented interceptors
│   │   └── models/service/    # Service components (Mail, HTTP, FTP, etc.)
│   ├── java/ortus/boxlang/modules/compat/
│   │   ├── bifs/              # Java-implemented BIFs (cache, conversion, encryption, etc.)
│   │   ├── components/        # Java tag/component implementations
│   │   ├── interceptors/      # Java-implemented interceptors
│   │   ├── runtime/context/   # Runtime context classes (Client, ClientScope)
│   │   └── util/              # Utilities (KeyDictionary, SettingsUtil)
│   └── resources/             # META-INF services, etc.
└── test/
    ├── java/ortus/boxlang/modules/compat/
    │   ├── BaseIntegrationTest.java   # Base class for module-loaded tests
    │   └── bifs/..., components/..., interceptors/...  # Test classes
    └── resources/                     # boxlang.json, test fixtures (.bx, .cfc)
```

## Architecture Patterns

### Module Entry Point

`src/main/bx/ModuleConfig.bx` is the module entry point:
- `configure()` — registers settings and mappings
- `onLoad()` — registers module services at runtime
- `onUnload()` — cleanup

### BIF Pattern

**Java BIFs** — each BIF is a class in `ortus.boxlang.modules.compat.bifs.<category>`:
- Extends `BIF`
- Annotated with `@BoxBIF`
- Implements `public Object _invoke(IBoxContext context, ArgumentsScope arguments)`
- Uses `Key.of("name")` for argument access (keys defined in `KeyDictionary.java`)
- Automatically discovered via META-INF services (`ServiceLoader` generates entries)

**BoxLang BIFs** — `.bx` files in `src/main/bx/bifs/`:
- Annotated with `@BoxBIF` annotation
- Implements an `invoke()` method

### Interceptor Pattern

**Java interceptors** in `ortus.boxlang.modules.compat.interceptors`:
- Extend `BaseInterceptor`
- Use `@InterceptionPoint("eventName")` on handler methods
- Discovered via META-INF services

**BoxLang interceptors** in `src/main/bx/interceptors/`:
- Implement event handler methods matching interception points

### Key Utility Classes

- `KeyDictionary.java` — central registry of `Key` constants used across the module
- `SettingsUtil.java` — helper to read module settings from `BoxContext`

## Testing Conventions

### Test Framework
- **JUnit Jupiter (JUnit 5)** with `@Test`, `@BeforeAll`, `@AfterAll`, `@BeforeEach`, `@DisplayName`
- **Google Truth** for assertions: `assertThat(result).isEqualTo(expected)`
- **Mockito** for mocking where needed
- **WireMock** for HTTP stubbing (in service tests)

### Test Patterns

1. **Integration tests** (requiring the full module):
   - Extend `BaseIntegrationTest`
   - Module is loaded from `./build/module` (built by `createModuleStructure`)
   - Provides: `runtime`, `moduleService`, `cacheService`, `context`, `variables`

2. **Simple BIF tests** (no module loading):
   - Extend `BaseCacheTest` or create standalone runtime
   - Only need a BoxRuntime instance

3. **Inline source execution**: Tests execute BoxLang source via `runtime.executeSource(sourceCode, context)` and inspect results via `variables.get("result")`

4. **Test resource fixtures**: `.bx` and `.cfc` files placed in `src/test/resources/`

### Writing a New Test

```java
class MyBifTest extends BaseIntegrationTest {

    @Test
    @DisplayName("It should do the thing")
    void testMyBif() {
        runtime.executeSource(
            "result = myBif('arg');",
            context
        );
        assertThat(variables.get("result")).isEqualTo(expected);
    }
}
```

## Code Conventions

### Java
- **Java version**: JDK 21
- **Line width**: 160 characters
- **Indentation**: Tabs (size 4)
- **Formatting**: Eclipse formatter with `.ortus-java-style.xml` (Spotless)
- **License header**: Apache 2.0 on every Java file
- **Package**: `ortus.boxlang.modules.compat.<category>`

### BoxLang (.bx, .cfc)
- **Line width**: 115 characters
- **Indentation**: Tabs (size 4)
- **Strings**: Double quotes
- **Formatting**: `cfformat` with `.cfformat.json`
- **License header**: Apache 2.0 on every BoxLang file

### General
- **Line endings**: LF
- **Encoding**: UTF-8
- **`.editorconfig`** defines whitespace/encoding rules across all file types

## CI/CD

- **PR checks**: `pr.yml` — runs tests + format check (`spotlessCheck` + `cfformat check`)
- **Snapshot on push to `development`**: `snapshot.yml` — tests → auto-format → snapshot release
- **Release on push to `main`**: `release.yml` — build, tag, publish to S3 + ForgeBox, bump version
- **Tests matrix**: `tests.yml` — reusable workflow running on ubuntu-latest and windows-latest
