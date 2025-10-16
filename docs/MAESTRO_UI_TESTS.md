# Maestro UI Tests

This document provides an overview of the Maestro UI tests in this project and how to run them for
both Android and iOS.

## Maestro

Maestro is a simple and effective mobile UI testing framework. You can learn more about it from the
official documentation: [https://maestro.mobile.dev/](https://maestro.mobile.dev/)

## Test Scenarios

The following UI test scenarios are covered:

* **Login:** Tests the user login flow.
* **Feed:** Tests the main content feed.
* **Logout:** Tests the user logout flow.

These tests are available for both Android and iOS platforms.

## Running the Tests

To run the Maestro tests, you need to have Maestro installed. You can find the installation
instructions [here](https://maestro.mobile.dev/getting-started/installing).

### Android

To run the Android UI tests, open the android emulator, install the app and run the following
command:

```bash
maestro test ../maestro/android/feed.yaml 
```

### iOS

To run the iOS UI tests, open the simulator, install the app and run the following command:

```bash
maestro test ../maestro/ios/feed.yaml
```
