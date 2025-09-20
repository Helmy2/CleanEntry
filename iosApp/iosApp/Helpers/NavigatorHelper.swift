import Foundation
import ComposeApp
import Combine

/// Observes navigation commands from the KMP AppNavigator and publishes them for SwiftUI consumption.
@MainActor
class NavigatorHelper: ObservableObject {

    // Published property to hold the latest navigation command.
    @Published private(set) var command: CoreCommand?

    private let navigator = iOSApp.dependenciesHelper.navigator
    private var task: Task<Void, Never>?

    /// Begins observing navigation commands.
    func start() {
        guard task == nil else {
            return
        }
        task = Task {
            for await cmd in navigator.commands {
                // When a new command is emitted, publish it.
                self.command = cmd

                if Task.isCancelled {
                    break
                }
            }
        }
    }

    /// Stops observing navigation commands.
    func stop() {
        task?.cancel()
        task = nil
    }
}
