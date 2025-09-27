import SwiftUI
import shared

struct AppCoordinatorView: View {
    @StateObject private var navigatorHelper = NavigatorHelper()

    var body: some View {
        NavigationStack(path: $navigatorHelper.navigationPath) {
            Group {
                switch navigatorHelper.selectedTab {
                case .login:
                    LoginView()
                default:
                    MainTabView(navigatorHelper: navigatorHelper)
                }
            }
            .navigationDestination(for: AppDestination.self) { destination in
                switch destination {
                case .registration:
                    RegistrationView()
                case .countryCodePicker(let code):
                    CountryPickerView(initialCountryCode: code ?? "eg")
                default:
                    EmptyView() // tabs handled separately
                }
            }
        }
        .onReceive(navigatorHelper.$command) { command in
            navigatorHelper.handleNavigationCommand(command)
        }
        .onAppear {
            navigatorHelper.start()
        }
        .onDisappear {
            navigatorHelper.stop()
        }
    }
}


struct MainTabView: View {
    @ObservedObject var navigatorHelper: NavigatorHelper

    var body: some View {
        TabView(selection: $navigatorHelper.selectedTab) {
            FeedView()
            .tabItem {
                Label("Feed", systemImage: "list.bullet")
            }
            .tag(AppDestination.feed)

            ProfileView()
            .tabItem {
                Label("Profile", systemImage: "person.crop.circle")
            }
            .tag(AppDestination.profile)
        }
    }
}
