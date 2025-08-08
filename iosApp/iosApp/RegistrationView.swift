import UIKit
import SwiftUI
import ComposeApp

struct RegistrationView: UIViewControllerRepresentable {
    var viewModel: RegistrationViewModel
    var onBackClick: () -> Void
    var countryResult: Country?
    var clearCountryResult: () -> Void
    var onNavigateToCountryPicker: (Country?) -> Void
    var onRegistrationSuccess: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        return RegistrationViewControllerKt.RegistrationViewController(
            nativeViewFactory: IOSNativeViewFactory.shared,
            viewModel: viewModel,
            onBackClick: onBackClick,
            countryResult: countryResult,
            clearCountryResult: clearCountryResult,
            onNavigateToCountryPicker: onNavigateToCountryPicker,
            onRegistrationSuccess: onRegistrationSuccess
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
