import SwiftUI
import ComposeApp


/**
 * The main view for the native SwiftUI country picker.
 * It is now driven by the CountryPickerViewModelHelper.
 */
struct NativeCountryPickerView: View {

    // 1. The helper is now an @StateObject, owned by this view.
    @StateObject private var helper: CountryPickerViewModelHelper
    private var selectedCountryCode: String

    // The callback to the parent coordinator.
    var onCountrySelected: (AuthCountry) -> Void
    
    @Environment(\.presentationMode) var presentationMode

    // Custom initializer to receive the repository
    init(selectedCountryCode: String, onCountrySelected: @escaping (AuthCountry) -> Void) {
        // We create the StateObject with the repository dependency
        _helper = StateObject(wrappedValue: CountryPickerViewModelHelper())
        self.onCountrySelected = onCountrySelected
        self.selectedCountryCode = selectedCountryCode
    }

    var body: some View {
        VStack {
            // Search bar
            TextField("Search...", text: $helper.searchQuery)
                .padding()
                .textFieldStyle(.roundedBorder)

            // List of countries
            List {
                ForEach(helper.countries, id: \.code) { country in
                    CountryPickerRow(
                        country: country,
                        isSelected: country.code == selectedCountryCode,
                        action: {
                            onCountrySelected(country)
                            presentationMode.wrappedValue.dismiss()
                        }
                    )
                }
            }
        }
        .navigationTitle("Select Country")
        .onAppear {
            helper.startObserving()
        }
    }
}

/**
 * A reusable SwiftUI view that displays a single row in the country picker list.
 * It shows the country's flag, name, and dial code, along with a checkmark
 * if it is the currently selected country.
 */
struct CountryPickerRow: View {
    let country: AuthCountry
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack {
                Text(country.flagEmoji)
                    .font(.largeTitle)
                Text(country.name)
                Text("(\(country.dialCode))")
                    .foregroundColor(.secondary)
                
                Spacer()
                
                if isSelected {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.accentColor)
                        .font(.title2)
                }
            }
            .contentShape(Rectangle()) // Makes the whole row tappable
        }
        .buttonStyle(.plain) // Use plain style to make it look like a list item
    }
}
