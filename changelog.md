# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

* * *

## [Unreleased]

## [1.36.0] - 2026-07-31

### Added

- [BLMODULES-255](https://ortussolutions.atlassian.net/browse/BLMODULES-255) - Added `allowIncludeClassFiles` module setting for CFML compat; allows `cfinclude` of CFC/BX files like CFML engines do

### Fixed

- [BL-2610](https://ortussolutions.atlassian.net/browse/BL-2610) - Query metadata (result struct) from `queryExecute`/`cfquery` is now modifiable, matching CFML engine behavior
- [BLMODULES-257](https://ortussolutions.atlassian.net/browse/BLMODULES-257) - Query `upperCaseKeys` now gates Adobe-specific behavior behind `isAdobe` flag; Lucee mode preserves original column name case

### Changed

- Bump com.gradleup.shadow from 9.4.2 to 9.6.1

## [1.35.0] - 2026-07-10

### Added

- [BLMODULES-227](https://ortussolutions.atlassian.net/browse/BLMODULES-227) - New `Serialize()` BIF for CFML compat (JSON-only with sensible error messages for XML and unsupported types)
- [BLMODULES-245](https://ortussolutions.atlassian.net/browse/BLMODULES-245) - New `IsDefined()` BIF override that handles literal dot-notation variable names for CFML compat (e.g., `"foo.bar"` treated as a variable name, not property access)
- [BL-2544](https://ortussolutions.atlassian.net/browse/BL-2544) - Adobe-mode JSON query serialization now uppercases column names in COLUMNS array, DATA struct keys, and row struct keys to match Adobe CF behavior

### Fixed

- [BLMODULES-234](https://ortussolutions.atlassian.net/browse/BLMODULES-234) - DateTimeMaskCompat: Added `"mm-"` → `"MM-"` date mask replacement
- [BL-2548](https://ortussolutions.atlassian.net/browse/BL-2548) - Set `defaultJSONQuerySerializationFormat` to `"row"` for CF compat when not explicitly configured; Override `DEFAULT_CLASS_REQUEST_CONTENT_TYPE` to match `DEFAULT_CONTENT_TYPE` in Adobe web runtime mode
- Fixed duplicate changelog headers to allow GitHub releases

### Changed

- Bump com.diffplug.spotless from 8.5.1 to 8.8.0
- Bump gradle-wrapper from 9.5.1 to 9.6.1
- Bump actions/checkout from 6 to 7
- Bump com.gradleup.shadow from 9.4.1 to 9.4.2

## [1.34.0] - 2026-05-26

### Fixed

- [BL-2435](https://ortussolutions.atlassian.net/browse/BL-2435) - For ACF compat, accept dates in array max/min/sum BIFs
- Override `enableNestedTransactions` setting to `false` for compat mode in boxlang@1.13.0 and below. (previously only 1.12).

## [1.33.0] - 2026-05-21

### Added

- [BLMODULES-207](https://ortussolutions.atlassian.net/browse/BLMODULES-207) - add qry.getColumnList() -> array for CF compat

### Fixed

- [BLMODULES-210](https://ortussolutions.atlassian.net/browse/BLMODULES-210) - Fix QueryCompat failing in ASMBoxpiler with `cannot use a function parameter with the same name as an import`
- [BLMODULES-187](https://ortussolutions.atlassian.net/browse/BLMODULES-187) - Set spreadsheet module to default type of binary
- [BL-2389](https://ortussolutions.atlassian.net/browse/BL-2389) - For ACF compat, treat empty strings as list with one value in listQualify()
- [BL-2374](https://ortussolutions.atlassian.net/browse/BL-2374) - Treat zero-length `cacheTimeout` as negative timeout to evict cache entry.

### Changed

- [BLMODULES-205](https://ortussolutions.atlassian.net/browse/BLMODULES-205) - Replaced `evaluate()` usage in service shims (FTP, Feed, IMAP, LDAP, PDF, POP) with direct scope dereference.

## [1.32.1] - 2026-04-21

## [1.32.0] - 2026-04-21

### Fixed

- Allow user config json to override `enableNestedTransactions` setting.

## [1.31.0] - 2026-04-09

## [1.30.2] - 2026-02-03

### Fixed

- [BL-1917](https://ortussolutions.atlassian.net/browse/BL-1917) - remove debug code
- [BL-2088](https://ortussolutions.atlassian.net/browse/BL-2088) - Fix context for cache retrieval
- [BL-2110](https://ortussolutions.atlassian.net/browse/BL-2110) - `getClassMetadata` in compat does not call pseudo constructor
- [BL-2124](https://ortussolutions.atlassian.net/browse/BL-2124) - compat version of `directoryCopy` overwrites by default
- Added all cache operations to use the context aware `getApplicationCache()` so the proper app-global hierarchy is respected.

## [1.30.1] - 2026-01-10

## [1.30.0] - 2026-01-09

### Fixed

- BL-1756 - ListDeleteAt Compat Behavior
- BL-2061 - Ensure `j2ee` session time does not have prefixed `session.sessionId` value
- BL-2075 - Improvements to the core allow removal of extra `equals` overload for DateTime

## [1.29.0] - 2025-12-05

## [1.28.1] - 2025-10-03

## [1.27.1] - 2025-08-06

## [1.27.0] - 2025-05-29

### Fixed

- Updated gradle wrapper to 8.14.1
- `obj` in the `SystemOutput` BIF should not be required as it can be null
- Missing `@build.version` on ModuleConfig
- `ApplicationCompatListener` doing a recursive stackoverflow when updating settings
- `ApplicationCompatListenerTest` had wrong charset

### Added

- Updated performance tuning for several key interceptions
- Missing package ortus headers
- [BL-1416](https://ortussolutions.atlassian.net/browse/BL-1416) - Support ACF/Lucee `blockedExtForFileUpload` Application Setting
- [BL-1409](https://ortussolutions.atlassian.net/browse/BL-1409) - Added `supportedLocales` to the `server.coldfusion` scope

## [1.26.0] - 2025-05-12

### Changed

- [BL-1375](https://ortussolutions.atlassian.net/browse/BL-1375) Compat - Move Legacy Date Format Interception to Module-Specific Interception Point for performance

## [1.25.0] - 2025-04-30

## [1.24.0] - 2025-04-05

## [1.22.0] - 2025-02-25

### Added

- BL-1100 resolve - Ensure the code and text are returned in compat mode for cfhttp.statusCode

## [1.20.0] - 2025-02-21

### Fixed

- Ensure client cache is not created or checked when not enabled

### Added

- BL-1097 Resolve - Allow for cachePut as a decimal number of days
- BL-1091 Resolve - Add `HTMLCodeFormat` BIF

## [1.19.0] - 2025-02-18

### Fixed

- Fixed gradle issues and build process
- Tests for BL-1031
- Ensure LS methods are handled
- `cacheKeyExists` alias was typed wrong.

### Added

- Upgrade shadow plugin and non -all.jar usage
- `<cfobjectcache>` component

## [1.18.0] - 2025-02-12

## [1.17.0] - 2025-01-31

## [1.16.0] - 2025-01-11

## [1.15.0] - 2024-12-17

## [1.14.0] - 2024-12-10

### Fixed

- When calling `getMetadata()` with a `DynamicObject` make sure the class is unwrapped
- Pre-seed `clientManagement` setting to `false` to avoid issues with Adobe/Lucee CFML engines

## [1.13.0] - 2024-12-10

### Added

- `CFIDE` mapping for compatibility for `orm` and `scheduler` interfaces
- More integration tests
- `Client` scope
- Many more test scenarios
- `struckKeyExists()` transpilers

## [1.12.0] - 2024-11-15

## [1.11.0] - 2024-10-31

### Added

- `getTagData()` and `getFunctionData()` lucee compats

## [1.10.0] - 2024-10-28

### Fixed

- Change to `toUnmodifiable` from `toImmutable`

### Added

- `cftoken` migration to comply with CFML engines.
- `cfid` migration to comply with CFML engines.

## [1.9.0] - 2024-10-15

### Fixed

- Bug with json escape characters in `serializeJSON()`

## [1.8.0] - 2024-10-10

### Fixed

- Bug with `structGet()` and invalid paths not working with `null` values
- `structGet()` not adhering to the actual Adobe CFML behavior

## [1.7.0] - 2024-09-30

## [1.6.0] - 2024-09-19

### Changed

- Name change to `bx-compat-cfml` to better describe the module

## [1.4.1] - 2024-09-19

## [1.4.0] - 2024-09-16

## [1.3.0] - 2024-09-04

### Added

- BL-491 New module settings:

```js
// The CF -> BL AST transpiler settings
// The transpiler is in the core, but will eventually live in this module, so the settings are here.
transpiler = {
	// Turn foo.bar into foo.BAR
	upperCaseKeys = true,
	// Add output=true to functions and classes
	forceOutputTrue = true,
	// Merged doc comments into actual function, class, and property annotations
	mergeDocsIntoAnnotations = true
}
```

- BL-449 preserve single quotes
- Added more docs
- Added new BIFS: `getVariable()`, `setVariable()`, `getComponentMetadata()`, `getMetaData()`, `deleteClientVariable()`, `getClientVariablesList()`

## [1.2.0] - 2024-08-09

### Added

- Module should coerce null values to empty string if the `queryNullToEmpty` is set to true, which is the default
- `objectLoad(), and objectSave()` aliases for `objectSerialize()` and `objectDeserialize()` respectively.

### Fixed

- Updated to use Attempts instead of Optionals for caching.

## [1.1.0] - 2024-06-29

### Fixed

- change of interface for cache provider returning arrays now since beta3
- New setting `engine` so you can chose "adobe" or "lucee" instead of the boolean operators
- Use the latest stable BoxLang beta build
- Gradle not using the `boxlangVersion` property

## [1.0.0] - 2024-06-13

- First iteration of this module

[unreleased]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.36.0...HEAD
[1.36.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.35.0...v1.36.0
[1.35.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.34.0...v1.35.0
[1.34.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.33.0...v1.34.0
[1.33.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.32.1...v1.33.0
[1.32.1]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.32.0...v1.32.1
[1.32.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.31.0...v1.32.0
[1.31.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.30.2...v1.31.0
[1.30.2]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.30.1...v1.30.2
[1.30.1]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.30.0...v1.30.1
[1.30.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.29.0...v1.30.0
[1.29.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.28.1...v1.29.0
[1.28.1]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.27.1...v1.28.1
[1.27.1]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.27.0...v1.27.1
[1.27.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.26.0...v1.27.0
[1.26.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.25.0...v1.26.0
[1.25.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.24.0...v1.25.0
[1.24.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.22.0...v1.24.0
[1.22.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.22.0...v1.22.0
[1.20.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.19.0...v1.20.0
[1.19.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.18.0...v1.19.0
[1.18.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.17.0...v1.18.0
[1.17.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.16.0...v1.17.0
[1.16.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.15.0...v1.16.0
[1.15.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.14.0...v1.15.0
[1.14.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.13.0...v1.14.0
[1.13.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.12.0...v1.13.0
[1.12.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.11.0...v1.12.0
[1.11.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.10.0...v1.11.0
[1.10.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.9.0...v1.10.0
[1.9.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.8.0...v1.9.0
[1.8.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.7.0...v1.8.0
[1.7.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.6.0...v1.7.0
[1.6.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.4.1...v1.6.0
[1.4.1]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.4.0...v1.4.1
[1.4.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.2.0...v1.3.0
[1.2.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.1.0...v1.1.0
[1.0.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/06e6a42cf95887e081e639073f36b481eb334097...v1.0.0
