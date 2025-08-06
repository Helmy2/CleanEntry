import SwiftUI
import ComposeApp


/**
 * The main view for the native SwiftUI country picker.
 * It now uses the reusable CountryPickerRow.
 */
struct NativeCountryPickerView: View {
    // The currently selected country code, passed from the helper.
    var selectedCountryCode: String
    
    // The callback to the helper's onCountrySelected function.
    var onCountrySelected: (Country) -> Void
    
    @Environment(\.presentationMode) var presentationMode
    
    // A hardcoded list for the example. This would come from the shared ViewModel.
    let allCountries = [
        Country(name: "Egypt", dialCode: "+20", code: "EG", flagEmoji: "🇪🇬"),
        Country(name: "Saudi Arabia", dialCode: "+966", code: "SA", flagEmoji: "🇸🇦"),
        Country(name: "United States", dialCode: "+1", code: "US", flagEmoji: "🇺🇸")
    ]

    var body: some View {
        List {
            ForEach(allCountries, id: \.code) { country in
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
        .navigationTitle("Select Country")
    }
}


/**
 * A reusable SwiftUI view that displays a single row in the country picker list.
 * It shows the country's flag, name, and dial code, along with a checkmark
 * if it is the currently selected country.
 */
struct CountryPickerRow: View {
    let country: Country
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
