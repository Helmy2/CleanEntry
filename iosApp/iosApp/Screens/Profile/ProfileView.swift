import SwiftUI
import shared

struct ProfileView: View {
    @StateObject var viewModelHelper = ProfileViewModelHelper()

    var body: some View {
        VStack(spacing: 16) {
            // Check if the actual 'isLoading' property exists on ProfileReducerState
            // The name must match what's generated from KMP.
            // For example, if KMP ProfileReducer.State has 'val isLoading: Boolean',
            // Swift might see it as 'isIsLoading' or just 'isLoading'.
            // Let's assume 'isLoading' for now.
            if viewModelHelper.currentState.isLoading {
                ProgressView("Logging out...")
            } else {
                Button("Logout") {
                    // Ensure ProfileReducerEventOnLogoutClicked is the correct Swift name
                    // for the KMP ProfileReducer.Event.OnLogoutClicked object.
                    viewModelHelper.handleEvent(event: AuthProfileReducerEventOnLogoutClicked())
                }
                .padding()
                // Consider applying styling similar to your LoginView buttons
            }

            // Check if the 'error' property exists on ProfileReducerState
            // and is a String?.
            if let error = viewModelHelper.currentState.error {
                Text(error)
                    .font(.caption)
                    .foregroundColor(.red)
                    .padding()
            }
        }
        .padding()
        .navigationTitle("Profile")
        // Effects are handled by NavigatorHelper for navigation.
        // If ProfileViewModel had other effects (e.g., show Toast),
        // you would observe viewModelHelper.effect here.
        // .onReceive(viewModelHelper.$effect) { effect in
        //     // Handle other specific effects if any
        // }
    }
}
