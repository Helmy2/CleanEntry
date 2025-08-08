import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    static var dependenciesHelper = DependenciesHelper()
    
    init() {
        InitKoinKt.doInitKoin()
        PhoneNumberUtil_iosKt.phoneNumberValidatorProvider = IosPhoneNumberValidatorProvider()
    }
    
    var body: some Scene {
        WindowGroup {
            AuthCoordinatorView()
        }
    }
}
