import UIKit
import SwiftUI
import ComposeApp

struct RegistrationView: UIViewControllerRepresentable {
    var viewModel: AuthRegistrationViewModel
    var countryResult: AuthCountry?

    func makeUIViewController(context: Context) -> UIViewController {
        return RegistrationViewControllerKt.RegistrationViewController(
            viewModel: viewModel,
            countryResult: countryResult,
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
