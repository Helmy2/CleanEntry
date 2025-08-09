import SwiftUI
import ComposeApp


enum AuthNavigationPath: Hashable {
    case registration
    case countryPicker(countryCode: String)
}

/**
 * A coordinator view that manages the navigation and state for the authentication flow.
 * It is the single source of truth, owning the LoginViewModelHelper.
 */
struct AuthCoordinatorView: View {

    @StateObject private var loginHelper = LoginViewModelHelper()
    @StateObject private var registrationHelper = RegistrationViewModelHelper()

    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            LoginView(
                viewModel: loginHelper.loginViewModel,
                onNavigateToCountryPicker: { Country in
                    path
                        .append(
                            AuthNavigationPath
                                .countryPicker(countryCode: Country!.code)
                        )
                },
                onLoginSuccess: {
                    print("Login successful! Navigate to main app...")
                },
                onCreateAccountClick: {
                    path.append(AuthNavigationPath.registration)
                }
            )
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(
                for: AuthNavigationPath.self
            ) { destination in
                switch destination {
                case .registration:
                    RegistrationView(viewModel: registrationHelper
                        .registrationViewModel,
                                     onBackClick: {
                                         path.removeLast()
                                     },
                                     onNavigateToCountryPicker: { Country in
                                         path.append(
                                             AuthNavigationPath.countryPicker(countryCode: Country!.code))
                                     }, onRegistrationSuccess: {
                        print("Registration successful! Navigate to main app...")
                    }
                    )
                    .navigationBarBackButtonHidden(true)
                case .countryPicker(let countryCode):
                    NativeCountryPickerView(
                        selectedCountryCode: countryCode,
                        onCountrySelected: { country in
                            loginHelper.onCountrySelected(country: country)
                            registrationHelper
                                .onCountrySelected(country: country)
                        }
                    )
                }
            }
        }
        .task {
            await loginHelper.activate()
            await registrationHelper.activate()
        }
    }
}
