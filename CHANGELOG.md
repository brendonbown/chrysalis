# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.2] - 2023-04-05

### Added
* `copy` command
  * copies permissions from one user to another
  * syntax: `chrys copy <netId>`
* `-v`/`--verbose` and `-vv`/`--debug` options
  * triggers logging to the console

### Changed
* Version is now statically generated when `chrysalis` is built

### Removed
* `-P` option
  * With the switch to Tyk, this feature no longer functions
  * If someone wants to figure out how Tyk works, feel free to add this back in

## [0.1.1] - 2022-06-20

### Added
* `--P` option
  * Allows to add/remove by product speed URL or title
  * syntax: `chrys add --P ADV16`

### Changed
* *(Internal)* Refactored configuration to classes
* `CHRYSALIS_NET_ID`, `CHRYSALIS_DB_PASSWORD`, and `CHRYSALIS_API_KEY` can only be used when the `DEBUG` environment
  variable is set

## [0.1.0] - 2022-06-06

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

[Unreleased]: https://github.com/brendonbown/chrysalis/compare/v0.1.2...HEAD
[0.1.2]: https://github.com/brendonbown/chrysalis/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/brendonbown/chrysalis/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/brendonbown/chrysalis/releases/tag/v0.1.0