import SwiftUI
import ComposeApp

// Defines the navigation destinations for the auth flow.
enum AuthNavigationPath: Hashable {
    case registration
    case countryPicker(countryCode: String)
}

/**
 A coordinator view that manages navigation and state for the authentication flow.
 It owns the LoginViewModelHelper and RegistrationViewModelHelper and routes user actions.
 */
struct AuthCoordinatorView: View {

    @StateObject private var loginHelper = LoginViewModelHelper()
    @StateObject private var registrationHelper = RegistrationViewModelHelper()
    @StateObject private var navigatorHelper = NavigatorHelper()

    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            // Root: Login view
            LoginView(
                viewModel: loginHelper.loginViewModel,
            )
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(for: AuthNavigationPath.self) { destination in
                switch destination {
                case .registration:
                    RegistrationView(
                        viewModel: registrationHelper.registrationViewModel,
                    )
                    .navigationBarBackButtonHidden(true)

                case let .countryPicker(countryCode):
                    NativeCountryPickerView(
                        selectedCountryCode: countryCode,
                        onCountrySelected: { country in
                            loginHelper.onCountrySelected(country: country)
                            registrationHelper.onCountrySelected(country: country)
                        }
                    )
                }
            }
        }
        // Lifecycle-aware state collection
        .onAppear {
            loginHelper.start()
            registrationHelper.start()
            navigatorHelper.start()
        }
        .onDisappear {
            loginHelper.stop()
            registrationHelper.stop()
            navigatorHelper.stop()
        }
        .onChange(of: navigatorHelper.command) { oldCommand, newCommand in
            guard let command = newCommand else {
                return
            }

            switch command {
            case let navigateTo as CoreCommand.NavigateTo:
                handleNavigateTo(destination: navigateTo.destination)

            case is CoreCommand.NavigateBack:
                if !path.isEmpty {
                    path.removeLast()
                }

            case is CoreCommand.NavigateBackWithResult:
                if !path.isEmpty {
                    path.removeLast()
                }

            case let navigateAsRoot as CoreCommand.NavigateAsRoot:
                // This command implies a new navigation stack.
                // For example, after login, you leave the auth flow entirely.
                // You would typically call a delegate/closure to notify the parent
                // coordinator to swap the view hierarchy.
                if navigateAsRoot.destination is CoreAppDestination.Dashboard {
                    print("Navigate to Dashboard as root!")
                    // In a real app, you would have a callback here to change the root view.
                    // For now, we can clear the auth path.
                    path = NavigationPath()
                }

                // You can add more cases for other command types like NavigateBackWithResult
            default:
                print("Received an unhandled navigation command.")
            }
        }
    }

    private func handleNavigateTo(destination: CoreAppDestination) {

        // Map KMP AppDestination to SwiftUI AuthNavigationPath
        if destination is CoreAppDestination.Registration {
            path.append(AuthNavigationPath.registration)
        } else if let countryPickerDest = destination as? CoreAppDestination.CountryCodePicker {
            // Note: The KMP destination doesn't know about the "target",
            // so you may need to adjust how you handle this or pass more info.
            // For now, we can assume a default target or handle it as needed.
            path.append(AuthNavigationPath.countryPicker(
                countryCode: countryPickerDest.code ?? "",
                ))
        }
        // Add other destination mappings here
    }
}
