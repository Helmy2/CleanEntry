import ComposeApp
import SwiftUI

class RegistrationViewModelHelper: ObservableObject {
    let registrationViewModel = iOSApp.dependenciesHelper.registrationViewModel

    @Published
    private(set) var selectedCountryCode: String = ""

    @MainActor
    func activate() async {
        for await state in registrationViewModel.state {
            self.selectedCountryCode = state.selectedCountryCode
        }
    }

    func onCountrySelected(country: Country) {
        registrationViewModel.handleEvent(
            event: RegistrationReducerEventCountrySelected(result: country)
        )
    }
}
