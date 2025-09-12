import UIKit
import SwiftUI
import ComposeApp

struct RegistrationView: UIViewControllerRepresentable {
    var viewModel: RegistrationViewModel
    var onBackClick: () -> Void
    var countryResult: Country?
    var onNavigateToCountryPicker: (Country?) -> Void
    var onRegistrationSuccess: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        return RegistrationViewControllerKt.RegistrationViewController(
            viewModel: viewModel,
            onBackClick: onBackClick,
            countryResult: countryResult,
            onNavigateToCountryPicker: onNavigateToCountryPicker,
            onRegistrationSuccess: onRegistrationSuccess
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
