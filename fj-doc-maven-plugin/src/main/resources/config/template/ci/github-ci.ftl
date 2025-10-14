name: CI workflow
on:
  push:
    branches:
      - main
  pull_request:
    types: [opened, synchronize, reopened]
jobs:
  build:
    name: Build
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - name: Set up JDK ${context.javaRelease}
        uses: actions/setup-java@v4
        with:
          java-version: ${context.javaRelease}
          distribution: 'zulu'
      - name: Cache Maven packages
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${r"${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}"}
          restore-keys: ${r"${{ runner.os }}"}-m2
      - name: Build
        run: mvn clean install