import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
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
