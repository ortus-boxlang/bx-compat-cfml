# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

* * *

## [Unreleased]

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

[Unreleased]: https://github.com/ortus-boxlang/bx-compat/compare/v1.2.0...HEAD

[1.2.0]: https://github.com/ortus-boxlang/bx-compat/compare/v1.1.0...v1.2.0

[1.1.0]: https://github.com/ortus-boxlang/bx-compat/compare/v1.1.0...v1.1.0

[1.0.0]: https://github.com/ortus-boxlang/bx-compat/compare/06e6a42cf95887e081e639073f36b481eb334097...v1.0.0
