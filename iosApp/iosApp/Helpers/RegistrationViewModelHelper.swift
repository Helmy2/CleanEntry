import SwiftUI
import ComposeApp

/// Bridges the Kotlin RegistrationViewModel to SwiftUI and exposes local published state.
class RegistrationViewModelHelper: ObservableObject {
    let registrationViewModel = iOSApp.dependenciesHelper.registrationViewModel

    @Published
    private(set) var selectedCountry: AuthCountry = AuthCountry.Companion.init().Egypt
    @Published
    private(set) var firstNameError: String? = nil
    @Published
    private(set) var surnameError: String? = nil
    @Published
    private(set) var emailError: String? = nil
    @Published
    private(set) var phoneError: String? = nil


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
        for await state in registrationViewModel.state {
            self.selectedCountry = state.selectedCountry
            self.firstNameError = state.firstNameError
            self.surnameError = state.surnameError
            self.emailError = state.emailError
            self.phoneError = state.phoneError
            if Task.isCancelled { break }
        }
    }

    func onCountrySelected(country: AuthCountry) {
        registrationViewModel.handleEvent(
            event: AuthRegistrationReducerEventCountrySelected(country: country)
        )
    }
}
