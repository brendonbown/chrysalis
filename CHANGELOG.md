# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
* *(Internal)* Refactored configuration to classes
* `CHRYSALIS_NET_ID`, `CHRYSALIS_DB_PASSWORD`, and `CHRYSALIS_API_KEY` can only be used when the `DEBUG` environment
  variable is set

## [0.1.0]

### Added
* Terminal commands
  * `add`
    * adds a list of permissions to the given user
    * syntax: `chrys add PERM_ONE PERM_TWO PERM_THREE...`
  * `remove`
    * removes a list of permissions from the given user
    * syntax: `chrys remove PERM_ONE PERM_TWO PERM_THREE...`
  * `list`
    * lists the permissions currently assigned to the given user
    * syntax: `chrys list`
  * `version`
    * prints the version number

[Unreleased]: https://github.com/brendonbown/chrysalis/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/brendonbown/chrysalis/releases/tag/v0.1.0