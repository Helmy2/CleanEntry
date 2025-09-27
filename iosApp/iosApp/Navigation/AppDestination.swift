//
// Created by platinum on 27/09/2025.
//

import Foundation

enum AppDestination: Hashable {
    case feed
    case profile
    case login
    case registration
    case countryCodePicker(selectedCountryCode: String?)
}

enum AppCommand: Hashable {
    case navigateBack
    case navigateTo(destination: AppDestination)
    case navigateAsRoot(destination: AppDestination)
}
