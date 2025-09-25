import SwiftUI
import shared

struct CountryPickerView: View {
    @StateObject var viewModel: CountryPickerViewModelHelper = CountryPickerViewModelHelper()
    @State private var searchText = ""
    private let _initialCountryCode: String

    init(initialCountryCode: String) {
        _initialCountryCode = initialCountryCode
    }

    var body: some View {
        VStack {
            TextField("Search", text: $searchText)
                .padding()
                .background(Color(.systemGray6))
                .cornerRadius(10)
                .onChange(of: searchText) { _, newValue in
                    viewModel.handleEvent(event: AuthCountryCodePickerReducerEventSearchQueryChanged(query: newValue))
                }

            if viewModel.currentState.status is CoreStatus.Loading {
                ProgressView("Loading...")
            } else if let errorStatus = viewModel.currentState.status as? CoreStatus.Error {
                Text(errorStatus.message)
                    .foregroundColor(.red)
            } else {
                List {
                    ForEach(viewModel.currentState.countries, id: \.self.code) { country in
                        CountryRow(country: country, isSelected: country.code == _initialCountryCode) {
                            viewModel.handleEvent(event: AuthCountryCodePickerReducerEventCountrySelectedCode(code: country.code))
                        }
                    }
                }
            }
        }
        .navigationTitle("Select Country")
        .onAppear {
            viewModel.handleEvent(event: AuthCountryCodePickerReducerEventInitCountrySelectedCode(code: _initialCountryCode))
        }
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
