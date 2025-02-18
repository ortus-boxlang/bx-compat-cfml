# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

* * *

## [Unreleased]

## [1.19.0] - 2025-02-18

## [1.21.0] - 2025-02-22

## [1.20.0] - 2025-02-21

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

## [1.14.0] - 2024-12-10

### Fixed

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

## [1.8.0] - 2024-10-10

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

## [1.2.0] - 2024-08-09

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

[Unreleased]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.19.0...HEAD

[1.19.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.18.0...v1.19.0

[1.21.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.20.0...v1.21.0

[1.20.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.19.0...v1.20.0


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
