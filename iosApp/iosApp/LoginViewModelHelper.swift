import ComposeApp
import SwiftUI


class LoginViewModelHelper: ObservableObject {
    let loginViewModel = iOSApp.dependenciesHelper.loginViewModel

    @Published
    private(set) var selectedCountryCode: String = ""

    @MainActor
    func activate() async {
        for await state in loginViewModel.state {
            self.selectedCountryCode = state.selectedCountryCode
        }
    }
    
    func onCountrySelected(country: Country){
        loginViewModel.handleEvent(
            event: LoginReducerEventCountrySelected(result:  country)
        )
    }
}
