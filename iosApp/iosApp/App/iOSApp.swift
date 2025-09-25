import SwiftUI
import shared

@main
struct iOSApp: App {

    // Immutable dependencies helper to avoid accidental reassignment.
    static let dependenciesHelper = DependenciesHelper()

    init() {
        // Initialize shared Koin container from Kotlin Multiplatform
        InitKoinKt.doInitKoin()
        // Provide platform-specific phone number validator implementation
        PhoneNumberUtil_iosKt.phoneNumberValidatorProvider = IosPhoneNumberValidatorProvider()
    }

    var body: some Scene {
        WindowGroup {
            AuthCoordinatorView()
        }
    }
}
