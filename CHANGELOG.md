# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- [workflow codeql on branch main](.github/workflows/codeql-analysis.yml)

### Security

- module fj-doc-val-poi now use poi5-version 5.2.3 (#61) [CVE-2022-26336](https://github.com/advisories/GHSA-mqvp-7rrg-9jxc) (NOTE: now this module will only work with java 11+, unless Apache POI version is overridden) 

## [1.5.10] - 2023-09-04

### Added

- [dependabot](.github/dependabot.yml) configuration

### Changed

- fj-core set to 8.2.6
- sample module [fj-doc-mod-itext](https://github.com/fugerit-org/fj-doc-mod-itext/) substituted by [fj-doc-mod-openpdf](https://github.com/fugerit-org/fj-doc-mod-openpdf/)
- reference to https://keepachangelog.com/ v1.1.0 in changelog

### Removed

- removed unsupported modules fj-doc-bom, fj-doc-bom-core, fj-doc-bom-fop

### Security

- fj-bom set to 1.3.5 (#59) [CVE-2016-1000352](https://github.com/advisories/GHSA-w285-wf9q-5w69) [CVE-2016-1000344](https://github.com/advisories/GHSA-2j2x-hx4g-2gf4) [CVE-2016-1000343](https://github.com/advisories/GHSA-rrvx-pwf8-p59p) [CVE-2016-1000342](https://github.com/advisories/GHSA-qcj7-g2j5-g7r3) [CVE-2016-1000338](https://github.com/advisories/GHSA-4vhj-98r6-424h) [CVE-2018-1000180](https://github.com/advisories/GHSA-xqj7-j8j5-f2xr) [CVE-2023-33201](https://github.com/advisories/GHSA-hr8g-6v94-x4m9) [CVE-2020-15522](https://github.com/advisories/GHSA-6xx3-rg99-gc3p) [CVE-2020-26939](https://github.com/advisories/GHSA-72m5-fvvv-55m6) [CVE-2016-1000345](https://github.com/advisories/GHSA-9gp4-qrff-c648) [CVE-2015-7940](https://github.com/advisories/GHSA-4mv7-cq75-3qjm) [CVE-2016-1000341](https://github.com/advisories/GHSA-r9ch-m4fh-fc7q) [CVE-2016-1000339](https://github.com/advisories/GHSA-c8xf-m4ff-jcxj) [CVE-2021-3803](https://github.com/advisories/GHSA-rp65-9cf3-cjxr) [CVE-2016-1000346](https://github.com/advisories/GHSA-fjqm-246c-mwqg) 
- apache fop version set to 2.9 (#58) [CVE-2022-44729](https://github.com/advisories/GHSA-gq5f-xv48-2365) [CVE-2022-44729](https://github.com/advisories/GHSA-gq5f-xv48-2365) [CVE-2022-44730](https://github.com/advisories/GHSA-2474-2566-3qxp) 

## [1.5.9] - 2023-09-04

### Added

- tag element (HEAD) to scm element. (pom.xml)
- issueManagement element (pom.xml, url : https://github.com/fugerit-org/fj-doc/issues )
- dependency fj-tester-helper8 set to 0.4.1
- [workflow](.github/workflows/build_maven_package.yml) for package testing and dependency upload

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