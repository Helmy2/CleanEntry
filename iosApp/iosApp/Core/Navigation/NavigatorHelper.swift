import Foundation
import SwiftUI
import shared
import Combine

/// Observes navigation commands from the KMP AppNavigator and publishes them for SwiftUI consumption.
@MainActor
class NavigatorHelper: ObservableObject {

    // Published property to hold the latest navigation command.
    @Published private(set) var command: AppCommand?
    @Published var selectedTab: AppDestination = .login
    @Published var navigationPath = NavigationPath()

    private let navigator = iOSApp.dependenciesHelper.navigator
    private var task: Task<Void, Never>?


    init() {
        start()
    }

    deinit {
        task?.cancel()
        task = nil
    }

    /// Begins observing navigation commands.
    private func start() {
        guard task == nil else {
            return
        }
        task = Task {
            do {
                let isUserAuthenticated = try await iOSApp.dependenciesHelper.isUserAuthenticated().boolValue
                if isUserAuthenticated == true {
                    selectedTab = .feed
                } else {
                    selectedTab = .login
                }
            } catch {
                print("Error in isUserAuthenticated: \(error)")
            }
            for await cmd in navigator.commands {
                // When a new command is emitted, publish it.
                self.command = getCommand(kmpCommand: cmd)

                if Task.isCancelled {
                    break
                }
            }
        }
    }


    func handleNavigationCommand(_ command: AppCommand?) {
        switch command {
        case .navigateBack:
            if !navigationPath.isEmpty {
                navigationPath.removeLast()
            }
        case .navigateAsRoot(let destination):
            if destination == .feed || destination == .profile {
                selectedTab = destination
                navigationPath = NavigationPath()
            } else if destination == .login {
                selectedTab = .login
                navigationPath = NavigationPath()
            } else {
                navigationPath = NavigationPath([destination])
            }
        case .navigateTo(let destination):
            if destination == .feed || destination == .profile {
                selectedTab = destination
            } else {
                navigationPath.append(destination)
            }
        case .none:
            break
        }
    }

    private func getDestination(kmpDestination: shared.CoreAppDestination) -> AppDestination {
        switch kmpDestination {
        case is CoreAppDestination.Feed:
            return .feed
        case is CoreAppDestination.Profile:
            return .profile
        case is CoreAppDestination.Auth:
            return .login
        case is CoreAppDestination.Login:
            return .login
        case is CoreAppDestination.Registration:
            return .registration
        case let countryPicker as CoreAppDestination.CountryCodePicker:
            return .countryCodePicker(selectedCountryCode: countryPicker.code)
        case let imageDetails as CoreAppDestination.ImageDetails:
            return .details(imageId: imageDetails.imageId)
        default:
            return .login
        }
    }

    private func getCommand(kmpCommand: shared.CoreCommand) -> AppCommand? {
        switch kmpCommand {
        case is CoreCommand.NavigateBack:
            return .navigateBack
        case let command as CoreCommand.NavigateTo:
            return .navigateTo(destination: getDestination(kmpDestination: command.destination))
        case let command as CoreCommand.NavigateAsRoot:
            return .navigateAsRoot(destination: getDestination(kmpDestination: command.destination))
        default:
            return nil
        }
    }
}
