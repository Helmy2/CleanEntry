import SwiftUI
import shared

struct CountryPickerView: View {
    @StateObject var helper: CountryPickerViewModelHelper
    @State private var searchText = ""


    var body: some View {
        VStack {
            TextField("Search", text: $searchText)
                .padding()
                .background(Color(.systemGray6))
                .cornerRadius(10)
                .onChange(of: searchText) { _, newValue in
                    helper.handleEvent(event: AuthCountryCodePickerReducerEventSearchQueryChanged(query: newValue))
                }

            if helper.currentState.errorMessage != nil {
                Text("Error").foregroundColor(.red)
            } else {
                List {
                    ForEach(helper.currentState.countries, id: \.self.code) { country in
                        CountryRow(country: country, isSelected: country.code == helper.currentState.selectedCountryCode) {
                            helper.handleEvent(event: AuthCountryCodePickerReducerEventCountrySelectedCode(code: country.code))
                        }
                    }
                }
            }
        }
        .navigationTitle("Select Country")
    }
}

// Helper view for a single country row
struct CountryRow: View {
    let country: AuthCountry
    let isSelected: Bool
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack {
                Text(country.flagEmoji)
                    .font(.largeTitle)
                Text(country.dialCode)
                    .font(.body)
                    .foregroundColor(.secondary)
                Text(country.name)
                    .font(.body)
                    .foregroundColor(.primary)
                Spacer()
                if isSelected {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.blue)
                }
            }
            .padding(.vertical, 8)
        }
    }
}
