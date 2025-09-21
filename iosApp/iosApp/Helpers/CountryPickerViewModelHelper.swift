import Foundation
import Combine
import shared

/// Bridges the Kotlin CountryCodePickerViewModel to SwiftUI and exposes local published state.
class CountryPickerViewModelHelper: ObservableObject {
    private let countryPickerViewModel: AuthCountryCodePickerViewModel

    @Published private(set) var searchQuery: String = ""
    @Published private(set) var countries: [AuthCountry] = []
    @Published private(set) var status: CoreStatus = CoreStatus.Loading()

    private var stateTask: Task<Void, Never>?

    init(initialCountryCode: String?) {
        self.countryPickerViewModel = iOSApp.dependenciesHelper.countryCodePickerViewModel
        if (initialCountryCode != nil) {
            countryPickerViewModel.handleEvent(event: AuthCountryCodePickerReducerEventInitCountrySelectedCode(code: initialCountryCode!))
        }
        start()
    }

    deinit {
        stop()
    }

    /// Begins observing the view model state.
    private func start() {
        guard stateTask == nil else {
            return
        }
        stateTask = Task { [weak self] in
            await self?.activate()
        }
    }

    /// Stops observing the view model state.
    private func stop() {
        stateTask?.cancel()
        stateTask = nil
    }

    /// Collects the state stream and updates published properties on the main actor.
    @MainActor
    private func activate() async {
        for await state in countryPickerViewModel.state {
            self.searchQuery = state.searchQuery
            self.status = state.status
            for await countries in state.countryFlow {
                self.countries = countries
            }
        }
    }

    func onSearchQueryChanged(query: String) {
        countryPickerViewModel.handleEvent(event: AuthCountryCodePickerReducerEventSearchQueryChanged(query: query))
    }

    func onCountrySelected(country: AuthCountry) {
        countryPickerViewModel.handleEvent(event: AuthCountryCodePickerReducerEventCountrySelectedCode(code: country.code))
    }

    func onBackButtonClicked() {
        countryPickerViewModel.handleEvent(event: AuthCountryCodePickerReducerEventBackButtonClicked())
    }
}
