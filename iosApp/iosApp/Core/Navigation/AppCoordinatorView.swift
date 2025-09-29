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
                    CountryPickerView(helper: CountryPickerViewModelHelper(code))
                case .details(let imageId):
                    DetailsView(helper: DetailsViewModelHelper(imageId))
                default:
                    EmptyView()
                }
            }
        }
        .onReceive(navigatorHelper.$command) { command in
            navigatorHelper.handleNavigationCommand(command)
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
