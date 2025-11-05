# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Fixed

- fj-doc-mod-fop, recover Apache FOP version <https://github.com/fugerit-org/fj-doc/issues/571>

## [8.17.4] - 2025-11-04

### Changed

- fj-doc-mod-poi, metadata handling <https://github.com/fugerit-org/fj-doc/issues/569>

## [8.17.3] - 2025-11-04

### Added

- new security-hardening property <https://github.com/fugerit-org/fj-doc/issues/567>

### Changed

- doc-producer and doc-creator handling for AsciiDoc handler
- doc-producer and doc-creator handling for HTML handler

### Fixed

- doc-language handling for AsciiDoc handler

## [8.17.2] - 2025-10-31

### Added

- fj-doc-mod-fop, new mod-fop-xslt-debug property <https://github.com/fugerit-org/fj-doc/issues/560>

### Changed

- fj-version 8.7.1

### Fixed

- fj-doc-mod-fop, fix doc-creator and doc-producer handling <https://github.com/fugerit-org/fj-doc/issues/563>

## [8.17.1] - 2025-10-28

### Added

- Image validator based on TwelveMonkeys ImageIO (currently supporting TIFF) <https://github.com/fugerit-org/fj-doc/issues/551>
- fj-doc-mod-fop, new mod-fop-xslt-path property <https://github.com/fugerit-org/fj-doc/issues/558>

### Changed

- quarkus-version set to 3.26.5 across all the modules
- now DocInput has access to DocProcessContext <https://github.com/fugerit-org/fj-doc/issues/545>
- quarkus-version set to 3.29.0 across all the modules

### Fixed

- DocInput.getDoc() now reuse docBase

## [8.17.0] - 2025-10-14

### Added

- fj-doc-maven-plugin, init goal, add CI support for GitHub <https://github.com/fugerit-org/fj-doc/issues/543>
- fj-doc-maven-plugin, init goal, 'addFormatting' <https://github.com/fugerit-org/fj-doc/issues/541>

### Changed

- quarkus-version set to 3.28.3 across all the modules <https://github.com/fugerit-org/fj-doc/issues/539>

## [8.16.9] - 2025-10-06

### Added

- fj-doc-maven-plugin, init goal, 'addJacoco' parameter <https://github.com/fugerit-org/fj-doc/issues/536>

### Changed

- fj-doc-maven-plugin, init goal, micronaut flavour version 4.9.4
- fj-doc-maven-plugin, init goal, springboot-3 flavour version 3.5.6
- fj-doc-val-pdfbox, pdfbox-version set to 2.0.35

## [8.16.8] - 2025-10-05

### Added

- fj-doc-playground-quarkus PDF/UA-1 rendering option' <https://github.com/fugerit-org/fj-doc/issues/527>

### Changed

- quarkus-version set to 3.28.2 across all the modules <https://github.com/fugerit-org/fj-doc/issues/525>
- fj-doc-maven-plugin, improvements in quarkus 3 flavours <https://github.com/fugerit-org/fj-doc/issues/531>

### Fixed

- fj-doc-maven-plugin, org.fugerit.java:freemarker-native dependency reference

## [8.16.7] - 2025-10-03

### Added

- info 'doc-producer' <https://github.com/fugerit-org/fj-doc/issues/523>

## [8.16.6] - 2025-10-02

### Added

- type handlers for XML (DocTypeHandlerCoreXML and DocTypeHandlerCoreXMLUTF8)
- type handlers for JSON (DocTypeHandlerCoreJSON and DocTypeHandlerCoreJSONUTF8)
- type handlers for JSON (DocTypeHandlerCoreYAML and DocTypeHandlerCoreYAMLUTF8)

### Fixed

- no native metadata for fj-doc-base-json and fj-doc-base-yaml <https://github.com/fugerit-org/fj-doc/issues/521>
- org.fugerit.java.doc.base.config.DocTypeHandlerXML renders the source document in the source format (i.e. JSON or YAML) <https://github.com/fugerit-org/fj-doc/issues/519>

## [8.16.5] - 2025-09-29

### Changed

- fj-doc-mod-openpdf-ext, producer is now 'Venus Fugerit Doc over OpenPDF'
- fj-doc-mod-openpdf-ext, default creator is now 'Venus Fugerit Doc (<https://github.com/fugerit-org/fj-doc>)'

### Fixed

- fj-doc-mod-openpdf-ext - OpenPDF alignment not set by type handler <https://github.com/fugerit-org/fj-doc/issues/517>

## [8.16.4] - 2025-09-25

### Fixed

- typo in PdfFopNoAccessibilityTypeHandler <https://github.com/fugerit-org/fj-doc/issues/512>

## [8.16.3] - 2025-09-24

### Changed

- fj-doc-mod-fop, make accessibility and keep empty tags parameters configurable <https://github.com/fugerit-org/fj-doc/issues/508>
- quarkus-version set to 3.28.1 across all the modules <https://github.com/fugerit-org/fj-doc/issues/510>

## [8.16.2] - 2025-09-12

### Changed

- fj-bom version 2.0.5
- quarkus-version set to 3.26.3 across all the modules <https://github.com/fugerit-org/fj-doc/issues/498>
- fj-doc-maven-plugin, flavour openliberty version set to 25.0.0.9

## [8.16.1] - 2025-09-01

### Changed

- fj-doc-maven-plugin, flavour openliberty version set to 25.0.0.8
- fj-doc-maven-plugin, flavour micronaut version set to 4.9.3
- fj-doc-maven-plugin, flavour springboot-3 version set to 3.5.5
- quarkus-version set to 3.26.1 across all the modules <https://github.com/fugerit-org/fj-doc/issues/494>

## [8.16.0] - 2025-08-22

### Added

- fj-doc-mod-openpdf-ext / fj-doc-mod-openrtf-ext, multiple java version dependency handling <https://github.com/fugerit-org/fj-doc/issues/489>

### Changed

- maven deploy workflow switched from java 17 (corretto) to java 21 (GraalVM)
- maven build and scan workflow switched from java 17 (corretto) to java 21 (GraalVM)

## [8.15.1] - 2025-08-21

### Changed

- quarkus-version set to 3.25.3 across all the modules <https://github.com/fugerit-org/fj-doc/issues/490>

## [8.15.0] - 2025-08-19

### Added

- fj-doc-core, check table columns and rows integrity <https://github.com/fugerit-org/fj-doc/issues/480>

## [8.14.1] - 2025-08-17

### Changed

- fj-bom version set to 2.0.4 <https://github.com/fugerit-org/fj-doc/issues/484>
- fj-doc-maven-plugin, flavour openliberty version set to 25.0.0.7
- fj-doc-maven-plugin, flavour springboot-3 version set to 3.5.3
- fj-doc-maven-plugin, flavour micronaut version set to 4.9.1
- quarkus-version set to 3.25.3 across all the modules <https://github.com/fugerit-org/fj-doc/issues/482>
- upgrade base image for playground quarkus ubi9/openjdk-21-runtime:1.22-1.1753981256

## [8.14.0] - 2025-07-13

### Added

- [fj-doc-val-*] DocTypeValidationResult has now validation message and exceptions

### Changed

- quarkus-version set to 3.24.3 across all the modules <https://github.com/fugerit-org/fj-doc/issues/469>

## [8.13.14] - 2025-07-06

### Changed

- fj-core version set to 8.7.0
- quarkus-version set to 3.24.2 across all the modules <https://github.com/fugerit-org/fj-doc/issues/464>

### Fixed

- [fj-doc-maven-plugin] check fj-core version at project creation <https://github.com/fugerit-org/fj-doc/issues/466>

## [8.13.13] - 2025-06-24

### Fixed

- nexus publish validation message "Project name is missing" <https://github.com/fugerit-org/fj-doc/issues/458>

## [8.13.12] - 2025-06-24

### Changed

- [fj-doc-maven-plugin] goal add, new parameter : basePackage (optional) <https://github.com/fugerit-org/fj-doc/issues/456>
- fj-bom version 2.0.2
- quarkus-version set to 3.24.0 across all the modules <https://github.com/fugerit-org/fj-doc/issues/454>

### Fixed

- openpdf-version set to 1.3.43 <https://github.com/fugerit-org/fj-doc/issues/452>

## [8.13.11] - 2025-05-12

### Changed

- [fj-doc-mod-fop] fop-version 2.11 <https://xmlgraphics.apache.org/fop/2.11/releaseNotes_2.11.html> (NOTE: now dependent on PDFBox 3)
- [fj-doc-maven-plugin] goal init - micronaut-4 flavour version set to 4.8.2 <https://github.com/fugerit-org/fj-doc/issues/432>
- [fj-doc-maven-plugin] goal init - springboot-3 flavour version set to 3.8.5 <https://github.com/fugerit-org/fj-doc/issues/432>
- [fj-doc-maven-plugin] goal init - openliberty flavour version set to 25.0.0.4<https://github.com/fugerit-org/fj-doc/issues/432>
- quarkus-version set to 3.22.2 across all the modules <https://github.com/fugerit-org/fj-doc/issues/432>

### Changed

## [8.13.10] - 2025-04-29

- [CHORE]: 'direct' goal + 'add' / 'init' configure support for extra modules <https://github.com/fugerit-org/fj-doc/pull/424>

### Fixed

- [BUG]: [fj-mod-doc-fop] error if a element 'phrase' is inside a 'cell' <https://github.com/fugerit-org/fj-doc/pull/426>

## [8.13.9] - 2025-04-28

### Changed

- quarkus-version set to 3.22.0 across all the modules <https://github.com/fugerit-org/fj-doc/pull/420>

### Fixed

- [fj-doc-maven-plugin] when 'addDocFacade' is set to 'false' 'freemarker-verify' execution gets an error <https://github.com/fugerit-org/fj-doc/pull/422>

## [8.13.8] - 2025-04-27

### Changed

- Migrating from "Legacy OSSRH" to "Central Portal" <https://github.com/fugerit-org/fj-doc/issues/415>
- fj-bom version set to 2.0.0 <https://github.com/fugerit-org/fj-doc/issues/415>

## [8.13.7] - 2025-04-27

### Changed

- [fj-doc-maven-plugin] for goal 'init' flavour 'direct', addDocFacade set to 'false' by default <https://github.com/fugerit-org/fj-doc/pull/413>
- [fj-doc-maven-plugin] verify plugin should be for direct plugin template path <https://github.com/fugerit-org/fj-doc/pull/411>

## [8.13.6] - 2025-04-26

### Added

- [fj-doc-maven-plugin] support add 'direct' goal to maven goal 'init' and 'add' <https://github.com/fugerit-org/fj-doc/pull/405>
- [fj-doc-playground-quarkus] project init - add verify and direct plugin options

## [8.13.5] - 2025-04-25

### Added

- [fj-doc-lib-direct] add useChainId param for chain alias <https://github.com/fugerit-org/fj-doc/pull/406>

## [8.13.4] - 2025-04-25

### Added

- [fj-doc-lib-direct] add configuration option to create parent directory <https://github.com/fugerit-org/fj-doc/pull/401>

### Fixed

- [fj-doc-mod-poi] element phrase it is not correctly rendered <https://github.com/fugerit-org/fj-doc/pull/403>
- [fj-doc-freemarker] handling link in asciidoc handler <https://github.com/fugerit-org/fj-doc/pull/399>

## [8.13.3] - 2025-04-24

### Fixed

- [fj-doc-base] handling link in simple mark down handler <https://github.com/fugerit-org/fj-doc/pull/397>

## [8.13.1] - 2025-04-24

### Added

- [fj-doc-maven-plugin] add variables to 'direct' goal <https://github.com/fugerit-org/fj-doc/issues/395>

### Changed

- quarkus-version set to 3.21.4 across all the modules <https://github.com/fugerit-org/fj-doc/pull/393>

## [8.13.0] - 2025-04-24

### Added

- [fj-doc-maven-plugin] goal : direct <https://github.com/fugerit-org/fj-doc/issues/391>
- [fj-doc-lib-direct] new module to generate documents from a configuration file <https://github.com/fugerit-org/fj-doc/issues/391>

### Changed

- graalvm '24' instead of '23' for build_fj-doc-native-quarkus_test.yml workflow
- quarkus-version set to 3.21.3 across all the modules <https://github.com/fugerit-org/fj-doc/pull/388>

## [8.12.8] - 2025-04-16

### Changed

- fj-bom version 1.7.4 <https://github.com/fugerit-org/fj-doc/issues/386>
- fj-core version set to 8.6.8 <https://github.com/fugerit-org/fj-doc/issues/382>
- quarkus-version set to 3.21.2 across all the modules <https://github.com/fugerit-org/fj-doc/pull/384>

## [8.12.7] - 2025-03-26

### Changed

- quarkus-version set to 3.21.0 across all the modules <https://github.com/fugerit-org/fj-doc/pull/344>

## [8.12.6] - 2025-03-25

### Changed

- [fj-doc-maven-plugin] goal 'add' new param 'simpleModel' <https://github.com/fugerit-org/fj-doc/issues/350>

### Fixed

- [fj-doc-freemarker] handling of svg in html handler <https://github.com/fugerit-org/fj-doc/issues/348>

## [8.12.5] - 2025-03-24

### Changed

- [fj-doc-maven-plugin] openliberty flavour version set to 25.0.0.2 <https://github.com/fugerit-org/fj-doc/issues/341>
- [fj-doc-maven-plugin] micronaut flavour version set to 4.7.6 <https://github.com/fugerit-org/fj-doc/issues/341>
- quarkus-version set to 3.19.4 across all the modules

### Fixed

- [fj-doc-maven-plugin] Error for goal 'add' groupId with '-' <https://github.com/fugerit-org/fj-doc/issues/346>

## [8.12.4] - 2025-03-22

### Added

- [fj-doc-mod-fop] Support for SVG rendering <https://github.com/fugerit-org/fj-doc/issues/337>
- [fj-doc-freemarker] FreeMarker function 'base64ToString' <https://github.com/fugerit-org/fj-doc/issues/337>
- [fj-doc-base] 'svg' to accepted image types <https://github.com/fugerit-org/fj-doc/issues/337>

### Changed

- [fj-doc-maven-plugin] spring-boot flavour version set to 3.4.4 <https://github.com/fugerit-org/fj-doc/issues/335>
- [fj-doc-maven-plugin] flavour extra configurations <https://github.com/fugerit-org/fj-doc/issues/333>
- release notes integration <https://github.com/fugerit-org/fj-doc/issues/331>
- quarkus-version set to 3.19.4 across all the modules

## [8.12.3] - 2025-03-17

### Added

- Add quarkus-3-properties (maven) flavour <https://github.com/fugerit-org/fj-doc/issues/329>
- workflow "CI native modules build and test" test for ubuntu arm and windows <https://github.com/fugerit-org/fj-doc/issues/313>

### Changed

- fj-bom version 1.7.3
- fj-xml-to-json-version 1.2.1
- quarkus-version set to 3.19.3 across all the modules

### Fixed

- [CHORE]: playground documentation not updated <https://github.com/fugerit-org/fj-doc/issues/310>
- [BUG]: Error in JDK 23, does it support JDK 23? <https://github.com/fugerit-org/fj-doc/issues/302>
- [BUG]: compatibility build with oracle and microsoft 17 jvm <https://github.com/fugerit-org/fj-doc/issues/304>
- [CHORE]: junit-jupiter-version 5.12.1 from fj-bom not compatible with quarkus 3 <https://github.com/fugerit-org/fj-doc/issues/315>

## [8.12.2] - 2025-02-28

### Changed

- [fj-doc-playground-quarkus] base image 1.21-1.1739376167
- quarkus-version set to 3.19.1 across all the modules

### Fixed

- Use UBI9 based Quarkus micro image for quarkus 3 <https://github.com/fugerit-org/fj-doc/issues/298>

## [8.12.1] - 2025-02-15

### Added

- Add quarkus-3-gradle (groovy) flavour <https://github.com/fugerit-org/fj-doc/issues/293>

### Changed

- [fj-doc-playground-quarkus] quarkus-info extension
- concurrency setup for GitHub workflows
- quarkus-version set to 3.18.3 across all the modules

### Fixed

- [fj-doc-playground-quarkus] project init - quarkus-3-gradle-kts flavour available

## [8.12.0] - 2025-01-31

### Added

- Add quarkus-3-gradle-kts flavour <https://github.com/fugerit-org/fj-doc/issues/284>
- better native AOT compatibility workflow

### Changed

- [fj-doc-maven-plugin] spring-boot flavour version set to 3.4.2
- [fj-doc-val-pdfbox] pdfbox version 2.0.33
- quarkus-version set to 3.18.1 across all the modules
- [fj-doc-maven-plugin] micronaut flavour version set to 4.7.4
- [fj-doc-maven-plugin] spring-boot flavour version set to 3.4.1

## [8.11.9] - 2025-01-11

### Fixed

- Fix native support for Apache FreeMarker <https://github.com/fugerit-org/fj-doc/issues/278>
- subfolder for native embedded configuration file <https://github.com/fugerit-org/fj-doc/issues/276>
- freemarker-version 2.3.34
- quarkus-version set to 3.17.6 across all the modules

## [8.11.8] - 2025-01-10

### Changed

- subfolder for native embedded configuration file <https://github.com/fugerit-org/fj-doc/issues/276>
- freemarker-version 2.3.34
- quarkus-version set to 3.17.6 across all the modules

## [8.11.7] - 2024-12-19

### Changed

- fixed endline for markdown format

## [8.11.6] - 2024-12-15

### Changed

- quarkus-version set to 3.17.4 across all the modules
- [fj-doc-maven-plugin] micronaut flavour version set to 4.7.2
- [fj-doc-maven-plugin] goal init, flavour quarkus-3 added eager init example #270
- [fj-doc-maven-plugin] goal init, flavour springboot-3 added eager init example #269

## [8.11.5] - 2024-12-06

### Added

- [fj-mod-doc-openpdf-ext] basic list implementation
- info suppress-wrong-type-error, some type error will be ignored if set to true and '1'

### Changed

- quarkus-version set to 3.17.3 across all the modules
- [fj-doc-maven-plugin] spring-boot flavour version set to 3.4.0
- [fj-doc-maven-plugin] micronaut flavour version set to 4.7.1
- [fj-doc-maven-plugin] openliberty flavour version set to 24.0.0.11

## [8.11.4] - 2024-11-27

### Changed

- [fj-doc-mod-fop] better logging for FreemarkerDocProcessConfigFacade.loadConfigSafe()

## [8.11.3] - 2024-11-27

### Added

- [fj-doc-playground-quarkus] added documentation <https://github.com/fugerit-org/fj-doc/issues/265>

### Changed

- [fj-doc-mod-fop] better init check for PdfFopTypeHandler
- quarkus-version set to 3.17.0 across all the modules
- [fj-doc-maven-plugin] micronaut flavour version set to 4.7.0
- [fj-doc-maven-plugin] spring-boot flavour version set to 3.3.6

## [8.11.2] - 2024-11-20

### Added

- [fj-doc-freemarker] new function formatDateTime

### Changed

- quarkus-version set to 3.16.4 across all the modules

## [8.11.1] - 2024-11-19

### Fixed

- [fj-doc-val-*] fix doc validation <https://github.com/fugerit-org/fj-doc/issues/262>

## [8.11.0] - 2024-11-19

### Added

- [fj-doc-val-core] DocValidatorTypeCheck facade to check file type <https://github.com/fugerit-org/fj-doc/issues/260>
- [fj-doc-val-p7m] check the inner type on P7MContentValidator <https://github.com/fugerit-org/fj-doc/issues/260>

### Changed

- [fj-doc-playground-quarkus] show quakus version
- quarkus-version set to 3.16.3 across all the modules
- xsd-parser-version set to 1.2.18

## [8.10.9] - 2024-11-03

### Changed

- native-helper-maven-plugin version set to 1.4.6
- [fj-doc-native-quarkus] added build arg : -H:+UnlockExperimentalVMOptions
- [fj-doc-maven-plugin] added information for flavour quarkus-3 native version

### Fixed

- [fj-doc-maven-plugin] fix version check for AsciiDoc example

## [8.10.8] - 2024-11-02

### Changed

- [fj-doc-mod-opencsv] added GraalVM native metadata to csv format
- [fj-doc-native-quarkus] added quarkus integration tests

## [8.10.7] - 2024-11-02

### Changed

- native-helper-maven-plugin version set to 1.4.5
- workflow "CI native modules build and test" added test against native executable

### Fixed

- native metadata sort

## [8.10.6] - 2024-11-01

### Added

- [fj-doc-native-quarkus] native metadata test project and workflow <https://github.com/fugerit-org/fj-doc/issues/246>

### Changed

- [fj-doc-maven-plugin] flavour quarkus-3 native configuration
- [fj-doc-maven-plugin] added quarkus-freemarker dependency to flavour quarkus-3

## [8.10.5] - 2024-11-01

### Changed

- [fj-doc-freemarker] generate stub and config conversion now supports registerById and allowDuplicatedId attributes
- [fj-doc-playground-quarkus] base image registry.access.redhat.com/ubi9/openjdk-21:1.20-2.1729773471
- [fj-doc-playground-quarkus] added output format ADOC (AsciiDoc)
- [fj-doc-playground-quarkus] quarkus-version 3.16.1
- [fj-doc-maven-plugin] goal init, flavour quarkus-3, default version 3.16.1
- [fj-doc-playground-quarkus] base image switched to ubi9/openjdk-21-runtime:1.20-2.1729773452

## [8.10.4] - 2024-10-27

### Added

- [CONTRIBUTING.md](CONTRIBUTING.md) section

### Changed

- Added sourceType 'kotlin' to freemarker-doc-process-1-0.xsd
- [fj-doc-maven-plugin] goal init/add - support for base-kotlin example <https://github.com/fugerit-org/fj-doc/issues/236>

### Fixed

- [fj-doc-freemarker] kotlin step, attribute map

## [8.10.3] - 2024-10-26

- [fj-doc-maven-plugin] goal init/add - support for base-json, base-yaml and base-kotlin example <https://github.com/fugerit-org/fj-doc/issues/231>
- [fj-doc-maven-plugin] module base-kotlin is now accepted.
- fj-bom version 1.6.7
- [repository] new issue template
- [documentation] README review to better refer to guide
- [fj-doc-mod-fop] PdfFopTypeHandler now concat PDFA and PDFUA mode in format field if both present (i.e. 'PDF/A-1b_PDF/UA-1')
- [fj-doc-maven-plugin] goal init, flavour quarkus-3, default springboot version 3.16.0
- [fj-doc-playground-quarkus] set quarkus version 3.16.0

## [8.10.2] - 2024-10-25

### Changed

- [fj-mod-lib-kotlin] fj-script-helper-version set to 2.0.3
- [fj-mod-lib-kotlin] added simpleMap conversion function to HelperDSL <https://github.com/fugerit-org/fj-doc/issues/229>
- [fj-doc-playground-quarkus] init supported versions review : '8.10.1', '8.10.0', '8.9.7', '8.9.0', '8.8.9', '8.8.0', '8.7.6'
- [fj-doc-playground-quarkus] io.quarkus:quarkus-webjars-locator relocated to io.quarkus:quarkus-web-dependency-locator
- [fj-doc-playground-quarkus] added quarkus-smallrye-openapi
- [fj-doc-maven-plugin] goal init, flavour springboot-3, default springboot version 3.3.5

### Fixed 

- [fj-doc-playground-quarkus] quarkus.rest.path in stead of quarkus.resteasy-reactive.path in REST reactive environment <https://github.com/quarkusio/quarkus/issues/35794>
- [fj-doc-mod-fop] set of profile PDF-UA
- [fj-doc-mod-fop] fail to create a PDF which is both compliant to PDF/A-1b and PDF/UA-1 <https://github.com/fugerit-org/fj-doc/issues/52>

## [8.10.1] - 2024-10-23

### Changed

- [fj-doc-base-kotlin] fj-script-helper version set to 2.0.0

## [8.10.0] - 2024-10-23

### Added

- [fj-doc-freemarker] new step type 'kotlin' <https://github.com/fugerit-org/fj-doc/issues/222>
- [fj-doc-base-kotlin] support to use kotlin script (KTS) as source <https://github.com/fugerit-org/fj-doc/issues/222>
- [fj-doc-lib-kotlin] kotlin utilities <https://github.com/fugerit-org/fj-doc/issues/222>

## [8.9.7] - 2024-10-20

### Added

- [fj-doc-freemarker] new FreeMarkerSkipProcessStep (skipfm) <https://github.com/fugerit-org/fj-doc/issues/225>

## [8.9.6] - 2024-10-20

### Added

- [fj-doc-freemarker] support for source type in FreemarkerDocProcessConfig <https://github.com/fugerit-org/fj-doc/issues/223>

## [8.9.5] - 2024-10-17

### Changed

- [fj-doc-maven-plugin] removed annotation @Tags for quarkus X flavour
- [fj-doc-maven-plugin] removed unused dependencies for quarkus X flavour

### Fixed

- [fj-doc-maven-plugin] Fix path test openrtf quarkus X flavour

## [8.9.4] - 2024-10-16

### Changed

- [fj-doc-maven-plugin] support for openpdf-ext and openrtf-ext modules

## [8.9.3] - 2024-10-16

### Changed

- [fj-doc-maven-plugin] support for asciidoc doc handler (fj-doc-version 8.8.7+)
- [fj-doc-maven-plugin] updated readme information for quarkus-3 flavour
- [fj-doc-guide] added pdf documentation
- [fj-doc-playground-quarkus] fj-doc-ext-kotlin-version set to 0.4.2

## [8.9.2] - 2024-10-14

### Changed

- [fj-doc-playground-quarkus] fj-doc-ext-kotlin-version set to 0.4.1
- [fj-doc-playground-quarkus] apply DocFacadeSource.cleanInput() to document generation playground
- [fj-doc-playground-quarkus] fj-service-helper-bom-version set to 1.4.3

### Fixed

- [fj-doc-base] doc-2-1.xsd, element **phrase**, **para**, **h** are now allowed as 
  children for **para** and **h**.

## [8.9.1] - 2024-10-12

### Changed

- [fj-doc-mod-fop] fop-version set to 2.10
- [fj-doc-playground-quarkus] kotlin.version set to 2.0.21
- [fj-doc-freemarker] new CleanXmlFun and CleanTextFun <https://github.com/fugerit-org/fj-doc/issues/213>
- [fj-doc-base] new DocXMLUtils utility <https://github.com/fugerit-org/fj-doc/issues/213>
- [fj-doc-freemarker] new attribute : validating, failOnValidate, cleanSource

## [8.9.0] - 2024-10-11

### Added

- [fj-doc-guide] new [asciidoc guide](https://venusdocs.fugerit.org/guide/)

### Fixed

- [fj-doc-base] Fix log doc xml validation

## [8.8.9] - 2024-09-27

### Changed

- [fj-doc-maven-plugin] goal init, flavour micronaut, default micronaut version 4.6.3
- [fj-doc-playground-quarkus] added options for new venus versions in playground
- [fj-doc-playground-quarkus] quarkus version set to 3.15.1
- [fj-doc-maven-plugin] goal init, flavour quarkus-3, default quarkus version 3.15.1

## [8.8.8] - 2024-09-25

### Changed

- [fj-doc-playground-quarkus] quarkus version set to 3.15.0
- [fj-doc-maven-plugin] goal init, flavour springboot-3, default springboot version 3.3.4
- [fj-doc-maven-plugin] goal init, flavour quarkus-3, default quarkus version 3.15.0

### Fixed

- [fj-doc-freemarker] handle space-before and space-after in table #206 [html]
- [fj-doc-mod-fop] handle space-before and space-after in table #206 [pdf]

## [8.8.7] - 2024-09-16

### Added

- [fj-doc-freemarker] new simple asciidoc renderer

### Changed

- [fj-doc-playground-quarkus] base image changed to registry.access.redhat.com/ubi9/openjdk-21:1.20-2.1725851045
- [fj-doc-playground-quarkus] quarkus version set to 3.14.4

## [8.8.6] - 2024-09-13

### Changed

- [fj-doc-maven-plugin] goal init, new parameter : flavourVersion
- [fj-doc-playground-quarkus] doc project init, added flavour version parameter
- [fj-doc-maven-plugin] goal init, flavour quarkus-2, default quarkus version 2.16.12.Final
- [fj-doc-maven-plugin] goal init, flavour springboot-3, default springboot version 3.3.3
- [fj-doc-maven-plugin] goal init, flavour openliberty, default openliberty version 24.0.0.9
- [fj-doc-maven-plugin] goal init, flavour micronaut-4, default micronaut version 4.6.2
- [fj-doc-maven-plugin] goal init, flavour quarkus-3, default quarkus version 3.14.3
- [fj-doc-playground-quarkus] doc project init, default venus version 8.8.6
- [fj-doc-playground-quarkus] added venus version 8.8.5 and 8.8.6 to doc project init
- [fj-doc-playground-quarkus] quarkus version set to 3.14.3

### Fixed

- [fj-doc-maven-plugin] goal init, generation of flavour 'vanilla' was bugged

## [8.8.5] - 2024-09-12

### Changed

- [fj-doc-maven-plugin] goal init, added openapi documentation for flavour 'openliberty' #193

### Fixed

- [fj-doc-playground-quarkus] springboot-3 flavour on doc project init

## [8.8.4] - 2024-09-11

### Changed

- [fj-doc-mod-openrtf-ext] handling doc-title, doc-subject, doc-author, doc-language, doc-creator #196
- [fj-doc-mod-openpdf-ext] handling doc-title, doc-subject, doc-author, doc-language, doc-creator #196
- [fj-doc-mod-openpdf-ext] default creator set to 'OpenPDF over Fugerit Venus DOC' #196
- [fj-doc-mod-fop] default creator set to 'Apache FOP over Fugerit Venus DOC'
- [fj-doc-playground-quarkus] added venus version 8.8.3 and 8.8.4 to doc project init

## [8.8.3] - 2024-09-11

### Fixed

- [fj-doc-maven-plugin] m2e lifecycleMappingMetadata (xml is not valid) # 194  

### Changed

- [fj-doc-maven-plugin] goal init, added openapi documentation for flavour 'micronaut-4'
- [fj-doc-maven-plugin] goal init, added openapi documentation for flavour 'springboot-3'
- [fj-doc-playground-quarkus] added venus version 8.8.1 and 8.8.2 to doc project init

## [8.8.2] - 2024-09-10

### Changed

- [fj-doc-maven-plugin] goal init, new flavour : springboot-3
- [fj-doc-maven-plugin] goal init, Quarkus and Micronaut generation based on freemarker macros.

### Fixed

- [fj-doc-maven-plugin] goal init, micronaut-4 typo in Controller class name

## [8.8.1] - 2024-09-10

### Changed

- [fj-doc-maven-plugin] goal add, new parameter : freemarkerVersion (default : 2.3.32)
- [fj-doc-maven-plugin] goal init, new flavour : micronaut-4
- [fj-doc-playground-quarkus] doc project init default version set to 8.8.0

## [8.8.0] - 2024-09-09

### Added

- [fj-doc-maven-plugin] goal init, flavour parameter (currently : vanilla, quarkus-3, quarkus-2)
- [fj-doc-maven-plugin] goal add, optimized order for fj-doc-* dependencies
- [fj-doc-maven-plugin] addDependencyOnTop parameter to put fj-doc-* dependencies on top
- [fj-doc-maven-plugin] addLombok parameter to add lombok dependency (will add slf4j-simple in test scope)
- [fj-doc-maven-plugin] addJunit5 parameter to add junit-jupiter dependency (will skip main generation)
- [fj-doc-val-p7m] P7MPemValidator and P7MRawValidator

### Changed

- fj version 8.6.5
- fj-bom version 1.6.6
- [fj-doc-playground-quarkus] quarkus version 3.14.2
- [fj-doc-playground-quarkus] changed base image eclipse-temurin:21.0.4_7-jre-alpine
- [fj-doc-val-p7m] Optimized P7MValidator (for previous behaviour use P7MLegacyValidator)

### Fixed

- [fj-doc-maven-plugin] add goal, short name module recognition

## [8.7.6] - 2024-09-02

### Fixed

- [fj-doc-val-p7m] failed to validate PKCS7 : unknown tag 31 encountered <https://github.com/fugerit-org/fj-doc/issues/188>

## [8.7.5] - 2024-08-29

### Changed

- [fj-doc-playground-quarkus] Add project init function
- [fj-doc-maven-plugin] is no on plugin management of fj-doc parent 
- [fj-doc-sample] fj-doc-maven-plugin:verify configuration

### Fixed

- [fj-doc-maven-plugin] Could not find goal 'verify' <https://github.com/fugerit-org/fj-doc/issues/180>

## [8.7.4] - 2024-08-28

### Fixed

- docker build

## [8.7.3] - 2024-08-28

### Fixed

- [fj-doc-mod-fop] java.lang.IllegalArgumentException: Not supported: http://javax.xml.XMLConstants/property/accessExternalDTD

## [8.7.2] - 2024-08-28

### Changed

- [fj-doc-maven-plugin] added init fugerit venus goal
- [fj-doc-maven-plugin] build order changed, now will build before fj-doc-sample
- [fj-doc-maven-plugin] new param 'reportOutputFormat' (default 'html') of 'verify' plugin

### Removed

- obsolete [docgen](https://mtmacdonald.github.io/docgen/docs/index.html) folder.

## [8.7.1] - 2024-08-25

### Changed

- [fj-doc-maven-plugin] new param 'addVerifyPlugin' (default 'true') of 'add' plugin

## [8.7.0] - 2024-08-25

### Added

- [fj-doc-maven-plugin] verify plugin using FreeMarkerTemplateSyntaxVerifier
- [fj-doc-maven-plugin] m2e lifecycle configuration
- [fj-doc-freemarker] tool FreeMarkerTemplateSyntaxVerifier (check for FreeMarker templates syntax) with report

### Fixed

- [fj-doc-freemarker] back color handling in html check
- [fj-doc-maven-plugin] removed template reference to font.


## [8.6.5] - 2024-08-23

### Fixed

- [fj-doc-maven-plugin] groupId from parent

## [8.6.4] - 2024-08-22

### Changed

- [fj-doc-playground-quarkus] quarkus version set to 3.14.0
- [fj-doc-maven-plugin] new parameter excludeXmlApis (could be needed with quarkus)
- [fj-doc-maven-plugin] new parameter addExclusions

## [8.6.3] - 2024-08-21

### Changed

- [fj-doc-mod-openpdf-ext] revert openpdf to version 1.3.43

## [8.6.2] - 2024-08-21

### Changed

- [fj-doc-val-pdfbox] pdfbox version 2.0.32
- [fj-doc-mod-openpdf-ext] openpdf version 1.4.2
- [fj-doc-maven-plugin] latest version discovery
- [fj-doc-maven-plugin] version customization
- [fj-doc-maven-plugin] check if project is already configured for fj-doc (default fail)
- [fj-doc-maven-plugin] new parameter : force (if set will not fail if project already configured)

## [8.6.1] - 2024-08-21

### Changed

- [fj-doc-maven-plugin] check if module exists 

## [8.6.0] - 2024-08-21

### Added

- fj-doc-maven-plugin (configure a maven project for Fugerit Venus Doc usage)

## [8.5.2] - 2024-08-14

### Changed

- [base] new findHandlerRequired() method
- [freemarker] new fullProcess() by handler id method
- [freemarker] added load-bundled-functions property
- [freemarker] Changed latest freemarker constant to 2.3.33
- [playground-quarkus] quarkus-version set to 3.13.2

## [8.5.1] - 2024-08-09

### Added

- [playground-quarkus] Config conversion user interface
- [freemarker] text wrap function (using zero with space `&#8203;`)

### Changed

- [playground-quarkus] quarkus-version set to 3.13.1

## [8.5.0] - 2024-08-03

### Added

- [mod-fop] fop-pool-min and fop-pool-max properties (fop configuration can be now resued)
- [playground-quarkus] added kotlin input <https://github.com/fugerit-org/fj-doc-ext-kotlin/>

### Changed

- DocTypeHandlerDefault.toString() has more informations now,
  and a customID generated from UUID when the class is created
- [doc-base] DocHandlerFacade.findHandler() now changed a bit handlers id resolution,
  (will now search first the natural id from the xml configuration)
- fj-version set to 8.6.4
- fj-bom set to 1.6.5
- [playground-quarkus] kotlin source set to fj-doc-ext-kotlin 0.3.2
- [playground-quarkus] quarkus-version set to 3.12.2
- [playground-quarkus] base image is no eclipse-temurin:21.0.3_9-jre-alpine

## [8.4.6] - 2024-04-21

### Changed

- xsd-parser-version set to 1.2.13 <https://github.com/xmlet/XsdParser/issues/67>
- openpdf version set to 1.3.43
- bouncy-castle-version set to 1.78
- fj-version set to 8.5.4
- [playground-quarkus] quarkus-version set to 3.9.4
- [playground-quarkus] docker image eclipse-temurin:21.0.2_13-jre-alpine
- fj-bom set to 1.6.3
- pdfbox-version set to 2.0.31

## [3.4.5] - 2024-03-19

### Added

- html-charset property for freemarker(html) renderer
- [fj-doc-lib-autodoc] alternate template with additional
  schema handling <https://github.com/fugerit-org/fj-doc/issues/135>

### Changed

- xsd-parser-version set to 1.2.10 <https://github.com/xmlet/XsdParser/issues/63>
- fj-version set to 8.5.3
- openpdf-version set to 1.3.42 <https://github.com/fugerit-org/fj-doc/discussions/128>
- [playground-quarkus] quarkus-version set to 3.8.3

## [3.4.4] - 2024-02-28

### Added

- [codacy](https://www.codacy.com/) badge

### Changed

- fj-xml-to-json-version set to 1.2.0
- exec-plugin.version set to 3.2.0
- xsd-parser-version set to 1.2.8
- fj-version set to 8.4.10
- fj-bom set to 1.6.1
- [playground-quarkus] quarkus-version set to 3.8.1

### Fixed

- security issue on html include
- security issue on Dockerfile
- typo in test case

## [3.4.3] - 2024-02-20

### Changed

- [xsd-parser-version 1.2.7](https://github.com/xmlet/XsdParser/issues/57)

### Fixed

- [playground-quarkus] doc conversion shortcut for xml, json and yaml

## [3.4.2] - 2024-02-19

### Changed

- openpdf-version set to 1.3.40 <https://github.com/fugerit-org/fj-doc/discussions/128>
- xsd-parser-version set to 1.2.6
- fj-xml-to-json version set to 1.0.0
- Upgraded build_maven_package workflow to version 1.0.1, (accespt DISABLE_MAVEN_DEPENDENCY_SUBMISSION)
- [playground-quarkus] handle direct link to xml2xml, json2json and yaml2yaml conversion
- [playground-quarkus] theme selector
- [playground-quarkus] base image amazoncorretto:21.0.2-alpine3.19
- [playground-quarkus] migrate SPA bundler to [Vite](https://vitejs.dev/)
- [playground-quarkus] suggested node 20 for react front end
- [playground-quarkus] quarkus-version set to 3.7.3

## [3.4.1] - 2024-02-11

### Added

- docker publish workflow
- [fugerit org github project conventions](https://universe.fugerit.org/src/docs/conventions/index.html) reference
- snyk status badge and scan

### Changed

- new deploy workflow
- workflows review and documentation
- fj-bom set to 1.6.0
- [playground-quarkus] quarkus-version set to 3.7.2
- [playground-quarkus] base image changed to 21.0.2_13-jre-ubi9-minimal

## [3.4.0] - 2024-02-04

### Added

- fj-doc-mod-openpdf-ext, PDF and HTML renderer based on [OpenPDF](https://github.com/LibrePDF/OpenPDF)
- fj-doc-mod-openrtf-ext, RTF renderer based on [OpenRTF](https://github.com/LibrePDF/OpenRTF)

### Changed

- Setup java 17 for code ql workflow
- Switch to codeql actions v3
- Minimum java version to run quarkus playground set to 17
- Minimum java version for build set to 17
- fj-doc-playground quarkus, added openpdf and openrtf output format
- [playground-quarkus] quarkus-version set to 3.7.1
- [playground-quarkus] base docker image eclipse-temurin:21.0.2_13-jre-alpine
- fj-core version set to 8.4.7

## [3.3.1] - 2024-01-10

### Added

- fj-doc-mod-opencsv, new OpenCSVTypeHandlerUTF8 handler
- fj-doc-base, new SimpleMarkdownBasicTypeHandlerNoCommentsUTF8 and SimpleMarkdownExtTypeHandlerNoCommentsUTF8 handlers

### Changed

- fj-doc-playground-quarkus, switched to freemarker doc process config xml
- fj-doc-sample, fj-doc-mod-openpdf version set to 1.3.0
- fj-doc-sample, setup fop-config fod pdf/a doc handler as inline

### Fixed

- freemarker-doc-process-1-0.xsd, docHandlerCustomConfigType now accepts any child elements

## [3.3.0] - 2024-01-05

### Changed

- fj-doc-mod-fop, now fop configuration can be inline
- fj-doc-freemarker, freemarker-doc-process-1-0.xsd now allows any child element in docHandlerCustomConfigType
- fj-doc-mod-openpdf version set to 1.2.2

## [3.2.5] - 2023-12-23

### Added

- [playground-quarkus] sample for table align

### Changed

- [playground-quarkus] quarkus-version set to 3.6.4
- Added java 21 to github action workflow for compatibility check
- fj-core version set to 8.4.6
- fj-bom parent set to 1.5.2
- fj-bom parent set to 1.5.1, [fix lombok-maven-plugin compatibility with java 21](https://github.com/fugerit-org/fj-bom/blob/main/CHANGELOG.md#151---2023-12-22)

## [3.2.4] - 2023-12-07

### Added

- [playground-quarkus] xsl-fo output (as of mod-fop format)

### Changed

- [playground-quarkus] quarkus-version set to 3.6.1

### Fixed

- [mod-fop] fix cell vertifcal align #104

## [3.2.3] - 2023-12-05

- [val-p7m] P7MContentValidator (validate p7m and possibly its content with a facade)
- [val-pdfbox] PdfboxStrictValidator (uses not lenient parser)

### Changed

- [val-pdfbox] pdfbox version 2.0.30
- [core] meta info 'table-border-collapse' [documentation improvement](https://venusdocs.fugerit.org/docs/html/doc_meta_info.html#table-border-collapse)

## [3.2.2] - 2023-11-30

### Added

- [core] 'table-border-collapse' document info to setup default border collapse behavior

### Changed

- [mod-fop] support for table cell back-color attribute
- [playground-quarkus] quarkus-version set to 3.6.0

## [3.2.1] - 2023-11-22

### Added

- [val-p7m] utility to extract p7m content
- [val-core] XmlValidator for simple xml doc type validation
- [val] P7MValidator in full validator facade
- [playground-quarkus] P7MValidator in validator feature

### Changed

- [playground-quarkus] docker image eclipse-temurin:21.0.1_12-jre-alpine
- [playground-quarkus] quarkus-version set to 3.5.2
- fj-core version set to 3.4.5
- fj-bom version set to 1.5.0
- bouncycastle jdk18on-1.77
- org.codehaus.mojo-exec-maven-plugin-3.1.1
- fj-doc-mod-openpdf-1.2.0-sa.1

## [3.2.0] - 2023-11-02

### Added

- [val-p7m] module for p7m validation

### Changed

- [playground-quarkus] changed docker base image to 21.0.1_12-jre-ubi9-minimal
- [playground-quarkus] added no cache headers

## [3.1.9] - 2023-10-31

### Added

- init handler with exception suppression

### Changed

- Added init handler with exception suppression
- [playground-quarkus] set docker image to eclipse-temurin:21.0.1_12-jdk
- [playground-quarkus] validation disabled for FTLX
- [playground-quarkus] better validation handling
- fj-core version set to 8.4.4
- fj-bom version set to 1.4.8
- quarkus version set to 3.5.0

## [3.1.8] - 2023-10-22

### Added

- [playground-quarkus] csv output format
- [fj-doc-mod-opencsv] 'csv-line-end' property to override the default line terminator (\n) 
- [fj-doc-mod-opencsv] 'csv-separator' property to override the default separator (,) 
- [lib-autodoc] documentation for csv properties
- [lib-autodoc] documentation for spreadsheet properties
- [playground-quarkus] DocConversion same format conversion (i.e. json -> json)
- [playground-quarkus] DocConversion only convert without pretty print
- [playground-quarkus] Venus XML Doc output format
- [playground-quarkus] messageFun (SimpleMessageFun) usable in ftl templates
- [playground-quarkus] freemarker (ftl) code highlight
- [playground-quarkus] complex example taken from <https://github.com/fugerit-org/fj-doc-guides/tree/main/fj-doc-guides-A003-full-document-freemarker>
- [playground-quarkus] os.arch info in home page
- [playground-quarkus] generation error output 

### Changed

- [playground-quarkus] home page provides the 'snapshot' image tag (instead of previous 'latest').
- [playground-quarkus] code review to remove bootstrap
- [playground-quarkus] default html type handler set to FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8

### Fixed

- [playground-quarkus] <#ftl> directive handling

## [3.1.7] - 2023-10-21

### Added

- [playground-quarkus] markdown output
- [playground-quarkus] document catalog filter by input type
- [playground-quarkus] system info in home page

### Changed

- [playground-quarkus] set -Dfile. encoding="UTF-8" on quarkus image
- [playground-quarkus] better json custom data handling in samples
- [playground-quarkus] base docker image set to amazoncorretto:21

### Fixed

- utf8 read function
- [playground-quarkus] link in home page

## [3.1.6] - 2023-10-15

### Added

- [playground-quarkus] Run locally instruction

### Changed

- [playground-quarkus] favicon and logo

### Fixed

- [Server-side template injection](https://github.com/fugerit-org/fj-doc/security/code-scanning/9)

## [3.1.5] - 2023-10-15

### Added

- Maven wrapper
- Public [Playground docker repository](https://hub.docker.com/repository/docker/fugeritorg/fj-doc-playground-quarkus/general)
- Dockerfile for playground and instructions
- FreeMarker Template sample features on fj-doc-playground-quarkus (91)

### Changed

- quarkus version set to 3.4.3

## [3.1.4] - 2023-10-14

### Added

- [fj-doc-freemarker] config attribute for FreeMarkerDocHelperTypeHandler : escapeTextAsHtml
- [fj-doc-freemarker] FreeMarkerHtmlTypeHandlerEscapeUTF8 with default escapeTextAsHtml=true and UTF8 charset
- [fj-doc-freemarker] FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8 with default escapeTextAsHtml=true and UTF8 charset
- [fj-doc-freemarker] output_format xml test

### Changed

- [fj-doc-freemarker] FreeMarkerHtmlTypeHandlerEscapeUTF8 and FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8 set as default for config stub generation

### Removed

- reference to fj-doc-mod-poi5 in README.md

## [3.1.3] - 2023-10-03

### Added

- FormatTypeConsts methods for standardDateParse() and standardNumberParse()

### Changed

- FreeMarkerHtmlTypeHandler, element 'para', more attributes are evaluated : space-left, space-right, white-space-collapse, leading, text-indent
- PdfFopTypeHandler, element 'para', attribute leading evaluated

## [3.1.2] - 2023-10-02

### Added

- methods to initAll handlers at once in InitHandler utility

### Fixed

- InitHandler initialization of PDF/A fop handlers failed if a font was not found.

## [3.1.1] - 2023-10-01

### Changed

- java and maven badges link

### Fixed

- LICENSE place holder
- page-break tag now rendered in html as `</div><div>` by freemarker html type handler

## [3.1.0] - 2023-10-01

### Added

- new config attribute 'fop-suppress-events' top mod-fop
- fj-xml-to-json 0.1.1 dependency
- [next generation json format support](fj-doc-base-json/src/main/docs/xml_conversion_ng.md) [experimental]

### Changed

- refactor of json and yaml format to use [fj-xml-to-json](https://github.com/fugerit-org/fj-xml-to-json)
- fj-bom set to 1.4.7
- jackon and opencsv version set in fj-bom
- fj-version set to 8.4.1
- pdfbox-version set to 2.0.29 (version 3.0.0 breaks compatibility, consider import)
- better test coverage DocHandlerFacade and FreemarkerDocProcessConfigFacade
- better error handling for FreemarkerDocProcessConfigFacade
- increased test coverage of fj-doc-base module
- DocXmlParser can now be set to fail when elements are unknown

### Fixed

- DocHandlerFacade.findHandler() was not looking for all registered handlers in some case.
- Fixed missing attributes 'map-all' in complex step xsd.
- Markdown handler ext table with extra spaces on header

## [3.0.9] - 2023-09-24

### Changed

- fj-doc-mod-openpdf-version set to 1.1.0-sa.1
- jajarta-jaxb-version set to 4.0.1

## [3.0.8] - 2023-09-23

### Added

- Code of conduct badge and file
- [Sample jdk compatibility check workflow on branch develop](.github/workflows/build_maven_compatibility.yml)

### Changed

- quarkus version set to 3.4.1
- fj-bom set to 1.4.2
- fj-version set to 8.3.8
- [Sonar cloud workflow merged in maven build](.github/workflows/deploy_maven_package.yml)

### Removed

- Sonar cloud workflow yml removed. (after being merged with maven build)

### Fixed

- [GHSA-4f4r-wgv2-jjvg](https://github.com/advisories/GHSA-4f4r-wgv2-jjvg) [CVE-2023-4853](https://nvd.nist.gov/vuln/detail/CVE-2023-4853)

### Security

- [Quarkus HTTP vulnerable to incorrect evaluation of permissions](https://github.com/fugerit-org/fj-doc/security/dependabot/23) resolved. (fix GHSA-4f4r-wgv2-jjvg)

## [3.0.7] - 2023-09-20

### Changed

- fj-core set to 8.3.7

## [3.0.6] - 2023-09-19

### Added

- method FreemarkerDocProcessConfig.fullProcess() with minimal parameters, generating the document and returning xml data.

### Changed

- fj-core set to 8.3.6

### Fixed

- now default chain in FreemarkerDocProcessConfig.newSimpleConfig() maps all attributes to freemarker template.

## [3.0.5] - 2023-09-17

### Added

- added java/maven badges

### Changed

- fj-core set to 8.3.5
- all Sonar Cloud issues addressed

## [3.0.4] - 2023-09-15

### Changed

- fj-bom set to 1.4.0 (fj-test-helper8 now managed by parent pom)

### Fixed

- Javadoc generation with java 17

### Security

- [Apache Commons Compress denial of service vulnerability](https://github.com/fugerit-org/fj-doc/security/dependabot/22)

## [3.0.3] - 2023-09-13

### Added

- [maven deploy workflow](.github/workflows/deploy_maven_package.yml)

### Changed

- fj-core set to 8.2.8
- fj-test-helper8 set to 0.5.0
- increased test coverage

## [2.0.2] - 2023-09-05

### Changed

- fj-bom set to 1.3.6 (poi-version set to 5.2.3)

## [2.0.1] - 2023-09-05

### Removed

- FopConfigClassLoader (previously deprecated) (#61)

## [2.0.0] - 2023-09-05

### Changed

- The apache poi5+ is now the default version required (and java11+ unless the poi version is overridden )
- FreemarkerDocProcessConfigFacade.newSimpleConfig() has a new method accepting the free marker version as a parameter.
- SimpleTableDocConfig.newConfig() now works in legacy mode (freemarker 2.3.29), and there are new methods for setting freemarker version

### Removed

- The module fj-doc-mod-poi5 has beend removed as now the module fj-doc-mod-poi requires apache poi 5+

## [1.5.11] - 2023-09-04

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
- Added build metadata to artifacts <https://github.com/fugerit-org/fj-bom/issues/2> (#54)
- Sonar Cloud Maven Build set to use maven profile sonarfugerit and github environmental variable for sonarKey (#54)
- New changelog style based on : <https://github.com/olivierlacan/keep-a-changelog> (#53)
- some link in the README.md

### Removed

- index.md

## [1.5.6 and previous]

### Changed

- only the [release notes](docgen/release-notes.txt) are available.
