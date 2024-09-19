# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

* * *

## [Unreleased]

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

[Unreleased]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.6.0...HEAD

[1.6.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.4.1...v1.6.0

[1.4.1]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.4.0...v1.4.1

[1.4.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.3.0...v1.4.0

[1.3.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.2.0...v1.3.0

[1.2.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.1.0...v1.2.0

[1.1.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/v1.1.0...v1.1.0

[1.0.0]: https://github.com/ortus-boxlang/bx-compat-cfml/compare/06e6a42cf95887e081e639073f36b481eb334097...v1.0.0
