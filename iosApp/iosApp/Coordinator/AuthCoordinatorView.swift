import SwiftUI
import ComposeApp

// Identifies which flow should receive a selected country.
enum CountrySelectionTarget: Hashable {
    case login
    case registration
}

// Defines the navigation destinations for the auth flow.
enum AuthNavigationPath: Hashable {
    case registration
    case countryPicker(countryCode: String, target: CountrySelectionTarget)
}

/**
 A coordinator view that manages navigation and state for the authentication flow.
 It owns the LoginViewModelHelper and RegistrationViewModelHelper and routes user actions.
 */
struct AuthCoordinatorView: View {

    @StateObject private var loginHelper = LoginViewModelHelper()
    @StateObject private var registrationHelper = RegistrationViewModelHelper()

    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            // Root: Login view
            LoginView(
                viewModel: loginHelper.loginViewModel,
                onNavigateToCountryPicker: { country in
                    // Prefer provided country code if present, else use current selection.
                    let code = country?.code ?? loginHelper.selectedCountryCode
                    path.append(AuthNavigationPath.countryPicker(countryCode: code, target: .login))
                },
                onLoginSuccess: {
                    // TODO: Replace with navigation to main app flow
                    print("Login successful! Navigate to main app...")
                },
                onCreateAccountClick: {
                    path.append(AuthNavigationPath.registration)
                }
            )
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(for: AuthNavigationPath.self) { destination in
                switch destination {
                case .registration:
                    RegistrationView(
                        viewModel: registrationHelper.registrationViewModel,
                        onBackClick: {
                            if !path.isEmpty {
                                path.removeLast()
                            }
                        },
                        onNavigateToCountryPicker: { country in
                            let code = country?.code ?? registrationHelper.selectedCountryCode
                            path.append(AuthNavigationPath.countryPicker(countryCode: code, target: .registration))
                        },
                        onRegistrationSuccess: {
                            // TODO: Replace with navigation to main app flow
                            print("Registration successful! Navigate to main app...")
                        }
                    )
                    .navigationBarBackButtonHidden(true)

                case let .countryPicker(countryCode, target):
                    NativeCountryPickerView(
                        selectedCountryCode: countryCode,
                        onCountrySelected: { country in
                            // Route selection to the correct flow.
                            switch target {
                            case .login:
                                loginHelper.onCountrySelected(country: country)
                            case .registration:
                                registrationHelper.onCountrySelected(country: country)
                            }
                            // Pop back after selection.
                            if !path.isEmpty {
                                path.removeLast()
                            }
                        }
                    )
                }
            }
        }
        // Lifecycle-aware state collection
        .onAppear {
            loginHelper.start()
            registrationHelper.start()
        }
        .onDisappear {
            loginHelper.stop()
            registrationHelper.stop()
        }
    }
}
