import SwiftUI
import shared

/// Bridges the Kotlin RegistrationViewModel to SwiftUI and exposes local published state.
class RegistrationViewModelHelper: BaseViewModelHelper<AuthRegistrationReducerState, AuthRegistrationReducerEvent, AuthRegistrationReducerEffect> {
    init() {
        let vm = iOSApp.dependenciesHelper.registrationViewModel
        super.init(viewModel: vm, initialState: vm.state.value)
    }
}

