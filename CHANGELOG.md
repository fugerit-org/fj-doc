# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- tag element (HEAD) to scm element. (pom.xml)
- issueManagement element (pom.xml, url : https://github.com/fugerit-org/fj-doc/issues )
- dependency fj-tester-helper8 set to 0.4.1
- [workflow](src/main/md/github/create_maven_build_workflow.md) for package testing and dependency upload

### Changed

- fj-bom set to 1.3.3
- fj-bom set to 8.2.4
- Changelog badge link set absolute 'https://github.com/fugerit-org/fj-daogen/blob/main/CHANGELOG.md'
- module playground quarkus quarkus.platform.group-id set to 'io.quarkus', version set to '3.3.1'
- playground samples doc version set to 2.1

### Fixed

- scm url (.git was missing at the end).

## [1.5.8] - 2023-09-01

### Added

- Unit test for DocValidator and FreemarkerDocProcessConfigValidator (#56)

### Fixed

- Prohibit xml external entities on DocValidator and FreemarkerDocProcessConfigValidator (#56)

### Changed

- Added assertions to TestPOI junit

## [1.5.7] - 2023-09-01

### Added

- xsd badge
- keep a changelog badge
- maven repo central and javadoc badges to modules

### Changed

- fj-bom version set to 1.3.1
- Added build metadata to artifacts (https://github.com/fugerit-org/fj-bom/issues/2) (#54)
- Sonar Cloud Maven Build set to use maven profile sonarfugerit and github environmental variable for sonarKey (#54)
- New changelog style based on : [https://github.com/olivierlacan/keep-a-changelog](https://github.com/olivierlacan/keep-a-changelog) (#53)
- some link in the README.md

### Removed

- index.md

## [1.5.6 and previous]

### Changed

- only the [release notes](docgen/release-notes.txt) are available.