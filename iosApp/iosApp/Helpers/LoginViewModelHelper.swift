import SwiftUI
import ComposeApp

/// Bridges the Kotlin LoginViewModel to SwiftUI and exposes local published state.
class LoginViewModelHelper: ObservableObject {
    let loginViewModel = iOSApp.dependenciesHelper.loginViewModel

    @Published
    private(set) var selectedCountry: AuthCountry = AuthCountry.Companion.init().Egypt
    @Published
    private(set) var phoneError: String? = nil
    @Published
    private(set) var passwordError: String? = nil

    private var stateTask: Task<Void, Never>?

    /// Begins observing the view model state if not already started.
    func start() {
        guard stateTask == nil else { return }
        stateTask = Task { [weak self] in
            await self?.activate()
        }
    }

    /// Stops observing the view model state.
    func stop() {
        stateTask?.cancel()
        stateTask = nil
    }

    /// Collects the state stream and updates published properties on the main actor.
    @MainActor
    func activate() async {
        for await state in loginViewModel.state {
            self.selectedCountry = state.selectedCountry
            self.phoneError = state.phoneError
            self.passwordError = state.passwordError
            if Task.isCancelled { break }
        }
    }

    func onCountrySelected(country: AuthCountry) {
        loginViewModel.handleEvent(
            event: AuthLoginReducerEventCountrySelected(country: country)
        )
    }
}
