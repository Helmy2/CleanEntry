import SwiftUI
import Combine
import ComposeApp


/// Bridges the Kotlin CountryCodePickerViewModel to SwiftUI and exposes local published state.
class CountryPickerViewModelHelper: ObservableObject {

    let viewModel = iOSApp.dependenciesHelper.countryCodePickerViewModel

    @Published private(set) var countries: [Country] = []
    @Published var searchQuery: String = ""

    private var cancellables = Set<AnyCancellable>()
    private var searchTask: Task<Void, Never>? = nil

    init() {}

    func startObserving() {
        // Observe local searchQuery changes, debounce, then
        // 1) forward to shared VM, 2) load countries from VM's repository.
        $searchQuery
            .debounce(for: .milliseconds(300), scheduler: RunLoop.main)
            .removeDuplicates()
            .sink { [weak self] query in
                guard let self = self else { return }
                // Forward query change to shared VM state
                self.viewModel.handleEvent(
                    event: CountryCodePickerReducerEventSearchQueryChanged(query: query)
                )
                // Load countries using the repository from the shared VM
                self.loadCountries(query: query)
            }
            .store(in: &cancellables)

        // Initial load
        viewModel.handleEvent(event: CountryCodePickerReducerEventSearchQueryChanged(query: ""))
        loadCountries(query: "")
    }

    /**
     * Loads countries using the repository exposed by the shared ViewModel.
     * Cancels any in-flight task to prevent race conditions.
     */
    func loadCountries(query: String) {
        searchTask?.cancel()

        searchTask = Task {
            do {
                let stream = viewModel.countryRepository.getCountries(query: query)
                for try await result in stream {
                    if Task.isCancelled { return }
                    await MainActor.run {
                        self.countries = result
                    }
                }
            } catch {
                if error is CancellationError { return }
                print("Failed to observe countries stream: \(error.localizedDescription)")
                await MainActor.run {
                    self.countries = []
                }
            }
        }
    }

    /**
     * Forwards the selected country to the shared ViewModel (optional for current UI flow).
     */
    func onCountrySelected(country: Country) {
        viewModel.handleEvent(
            event: CountryCodePickerReducerEventCountrySelectedCode(code: country.code)
        )
    }
}
