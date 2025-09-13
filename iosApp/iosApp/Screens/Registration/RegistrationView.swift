import UIKit
import SwiftUI
import ComposeApp

struct RegistrationView: UIViewControllerRepresentable {
    var viewModel: RegistrationViewModel
    var countryResult: Country?

    func makeUIViewController(context: Context) -> UIViewController {
        return RegistrationViewControllerKt.RegistrationViewController(
            viewModel: viewModel,
            countryResult: countryResult,
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
