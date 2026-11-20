# Contributing to Fugerit Venus Doc

We want to make contributing to this project as easy as possible.

Please make sure to follow our [Code of Conduct](CODE_OF_CONDUCT.md) and check the [Fugerit Org Project Conventions](https://universe.fugerit.org/src/docs/conventions/index.html) for general organization guidelines.

## Pull Requests

We actively welcome your pull requests and forks.

- **Target branch**: Open pull requests against the `main` branch.
- **Commit messages**: We follow [Conventional Commits](https://www.conventionalcommits.org/) (e.g. `fix(fj-doc-mod-pdfbox): ...`, `feat(...): ...`, `docs: ...`) and reference related issues when applicable (e.g. `#123`).
- **Tests & Quality**: Please include unit tests covering bug fixes or new features and ensure existing tests pass. Pull requests are checked by automated CI workflows (Maven build/tests, SonarCloud, Codacy, and CodeQL).
- **Changelog**: Updating [CHANGELOG.md](CHANGELOG.md) under `## [Unreleased]` (following [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)) is optional — feel free to add an entry, or maintainers will handle it before release.

## Enhancements

Propose your contribution by either:

1. [Submitting your proposal](https://github.com/fugerit-org/fj-doc/issues/new/choose)
2. [Opening a discussion on your ideas](https://github.com/fugerit-org/fj-doc/discussions/categories/ideas)

Any contribution is welcome (development, testing, documentation, etc.).

## Bug Fixes

You can use the [GitHub issue form](https://github.com/fugerit-org/fj-doc/issues/new/choose) to report bugs.

## Security

If you discover a security vulnerability, please **do not** open a public issue. Instead, report it privately via [GitHub Security Advisories](https://github.com/fugerit-org/fj-doc/security/advisories/new).

## Development Setup & Running Tests

### Prerequisites

- **Java 21+** to build the full project (core modules maintain **Java 8** runtime compatibility via `maven.compiler.release=8`, so avoid newer JDK APIs in Java 8 modules).
- **Apache Maven 3.9+** (or use the included Maven Wrapper `./mvnw` / `mvnw.cmd`).
- **Node.js 26+** is only required if building the React frontend of `fj-doc-playground-quarkus` (with `-Pbuildreact`).

### Building and Testing

Run the full build and test suite:

```shell
./mvnw test
```

To iterate faster on a specific module (along with its required local dependencies):

```shell
./mvnw test -pl <module-name> -am
```

For example:

```shell
./mvnw test -pl fj-doc-mod-pdfbox -am
```

## Generative AI Policy

Contributions assisted by Generative AI tools are welcome, provided that the committer reviews, tests, and takes full responsibility for the submitted changes (including code quality, correctness, and license compliance).

When submitting AI-assisted changes, we recommend adding a brief note to the commit message (or pull request description) using the following template (parts in brackets are optional):

```text
Note: Generative AI [VENDOR] assistance was used [to DETAIL ON THE CONTRIBUTION].
```

For example:

```text
Note: Generative AI (Google Gemini) assistance was used to improve test coverage and minor tweaks to the library itself.
```

This `Note:` line is sufficient; there is no need to add `Co-Authored-By` or similar tags for AI tools.

## License

By contributing to [Fugerit Venus Doc](https://github.com/fugerit-org/fj-doc),
you agree that your contributions will be licensed under the
[LICENSE](https://github.com/fugerit-org/fj-doc/blob/main/LICENSE)
file in the root directory of this source tree.
