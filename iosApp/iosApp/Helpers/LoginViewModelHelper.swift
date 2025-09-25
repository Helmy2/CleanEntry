import SwiftUI
import shared

/// Bridges the Kotlin LoginViewModel to SwiftUI and exposes local published state.
class LoginViewModelHelper: BaseViewModelHelper<AuthLoginReducerState, AuthLoginReducerEvent, AuthLoginReducerEffect> {
    init() {
        let vm = iOSApp.dependenciesHelper.loginViewModel
        super.init(viewModel: vm, initialState: vm.state.value)
    }
}
