import Foundation
import Combine
import shared

/// Bridges the Kotlin CountryCodePickerViewModel to SwiftUI and exposes local published state.
class CountryPickerViewModelHelper: BaseViewModelHelper<AuthCountryCodePickerReducerState, AuthCountryCodePickerReducerEvent, AuthCountryCodePickerReducerEffect> {

    init() {
        let vm = iOSApp.dependenciesHelper.countryCodePickerViewModel
        super.init(viewModel: vm, initialState: vm.state.value)
    }
}
