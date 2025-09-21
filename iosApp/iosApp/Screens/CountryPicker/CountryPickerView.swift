import SwiftUI
import shared

struct CountryPickerView: View {
    @StateObject var viewModel: CountryPickerViewModelHelper
    @State private var searchText = ""

    init(initialCountryCode: String) {
        _viewModel = StateObject(wrappedValue: CountryPickerViewModelHelper(initialCountryCode: initialCountryCode))
    }

    var body: some View {
        VStack {
            // Search field
            TextField("Search", text: $searchText)
                .padding()
                .background(Color(.systemGray6))
                .cornerRadius(10)
                .onChange(of: searchText) { _, newValue in
                    viewModel.onSearchQueryChanged(query: newValue)
                }

            // Status content
            if viewModel.status is CoreStatus.Loading {
                ProgressView("Loading...")
            } else if let errorStatus = viewModel.status as? CoreStatus.Error {
                Text(errorStatus.message)
                    .foregroundColor(.red)
            } else {
                List {
                    ForEach(viewModel.countries, id: \.code) { country in
                        CountryRow(country: country) {
                            viewModel.onCountrySelected(country: country)
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
//                if country.code == viewModel.selectedCountry.code { // This part needs to be handled in your view model
//                    Image(systemName: "checkmark.circle.fill")
//                        .foregroundColor(.blue)
//                }
            }
            .padding(.vertical, 8)
        }
    }
}
