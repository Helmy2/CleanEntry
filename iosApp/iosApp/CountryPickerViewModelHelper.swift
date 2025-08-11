import ComposeApp
import SwiftUI
import Combine

/**
 * An ObservableObject that acts as the ViewModel for the native SwiftUI CountryCodePicker.
 * This version correctly handles asynchronous data loading and search cancellation.
 */

class CountryPickerViewModelHelper: ObservableObject {

    private let countryRepository: CountryRepository

    @Published private(set) var countries: [Country] = []
    @Published var searchQuery: String = ""

    private var searchTask: Task<Void, Never>? = nil

    init() {
        self.countryRepository = iOSApp.dependenciesHelper.countryRepository

        // We now use a property observer to trigger a new search task
        // whenever the searchQuery changes.
        $searchQuery
            .debounce(for: .milliseconds(300), scheduler: RunLoop.main)
            .removeDuplicates()
            .assign(to: \.searchQuery, on: self)
            .store(in: &cancellables) // Still need Combine for debouncing
    }

    // We can use a property observer on searchQuery to trigger re-loading
    // This is a common pattern in SwiftUI
    private var cancellables = Set<AnyCancellable>()

    func startObserving() {
        $searchQuery
        .debounce(for: .milliseconds(300), scheduler: RunLoop.main)
        .removeDuplicates()
        .sink { [weak self] query in
            self?.loadCountries(query: query)
        }
        .store(in: &cancellables)
        // Initial load
        loadCountries(query: "")
    }

    /**
     * The main function to load countries. It cancels any previous loading
     * task and starts a new one with the given query.
     */
    func loadCountries(query: String) {
        // 1. Cancel the previous task to prevent race conditions.
        searchTask?.cancel()

        // 2. Start a new Task.
        searchTask = Task {
            do {
                let stream = countryRepository.getCountries(query: query)
                for try await result in stream {
                    // Check if the task has been cancelled before updating the UI
                    if Task.isCancelled {
                        return
                    }

                    self.countries = result
                }
            } catch {
                if !Task.isCancelled {
                    print("Failed to observe countries stream: \(error.localizedDescription)")
                    self.countries = []
                }
            }
        }
    }
}
