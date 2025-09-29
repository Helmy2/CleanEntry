import SwiftUI
import shared

@main
struct iOSApp: App {

    // Immutable dependencies helper to avoid accidental reassignment.
    static let dependenciesHelper = DependenciesHelper(
        phoneNumberValidator: IosPhoneNumberValidatorProvider()
    )

    var body: some Scene {
        WindowGroup {
            AppCoordinatorView()
        }
    }
}
