import UIKit
import SwiftUI
import ComposeApp


struct LoginView: UIViewControllerRepresentable {

    var viewModel: AuthLoginViewModel
    var countryResult: AuthCountry?

    
    func makeUIViewController(context: Context) -> UIViewController {
        return LoginViewControllerKt.LoginViewController(
            viewModel: viewModel,
            countryResult: countryResult
        )
    }
    public func updateUIViewController(_ uiViewController: UIViewController, context: Context){
    }
}
