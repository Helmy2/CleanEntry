import SwiftUI
import ComposeApp


enum AuthNavigationPath: Hashable {
    case registration
    case countryPicker
}

/**
 * A coordinator view that manages the navigation and state for the authentication flow.
 * It is the single source of truth, owning the LoginViewModelHelper.
 */
struct AuthCoordinatorView: View {
    
    @StateObject private var loginViewModel = LoginViewModelHelper()
    
    @State private var path = NavigationPath()
    
    var body: some View {
        NavigationStack(path: $path) {
            LoginView(
                viewModel: loginViewModel.loginViewModel,
                onNavigateToCountryPicker: { Country in
                    path.append(AuthNavigationPath.countryPicker)
                },
                onLoginSuccess: {
                    print("Login successful! Navigate to main app...")
                },
                onCreateAccountClick: {
                    path.append(AuthNavigationPath.registration)
                },
                onClearCountryResult : {

                }
            )
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(for: AuthNavigationPath.self) { destination in
                switch destination {
                case .registration:
                    Text("Registration Screen")
                case .countryPicker:
                    NativeCountryPickerView(
                        selectedCountryCode: loginViewModel.selectedCountryCode,
                        onCountrySelected: loginViewModel.onCountrySelected
                    )
                }
            }
        }
        .task {
            await loginViewModel.activate()
        }
    }
}
