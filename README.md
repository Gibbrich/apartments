# Apartments

## General information
App allows user to download apartments from backend. On apartment card click user can check detailed data about this apartment. User can navigate map, set filters for specific dates or beds quantity and book apartments. Once apartment booked, user won't be able to see it, it there is set filter by date. Data is stored in memory, so after closing app, new data will be fetched from network. App does not persist any apartment data.

## Architecture:
- App divided into several layers: general Android framework (UI, ViewModel, Activities, Fragments) - app, data fetching/caching - data and common code - core.
- App is built using recommended Google architecture: UI - ViewModel - Repository (remote). Basic architecture pattern is MVVM, implemented by using Architecture components.
- Dependencies and build logic are written in Kotlin script and should be placed in buildSrc module, which improves readability/comprehensibility of custom build logic.

## What can be improved/added from technical perspective:
- More UI and integration tests

## How to build and install app
Installs the Debug build

    ./gradlew installDebug

## How to test
- Connect Android device or launch emulator

- Install and run instrumentation tests on connected devices

      ./gradlew connectedAndroidTest

- Run unit tests

      ./gradlew test
