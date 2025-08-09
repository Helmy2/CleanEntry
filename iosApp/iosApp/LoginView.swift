import UIKit
import SwiftUI
import ComposeApp


struct LoginView: UIViewControllerRepresentable {
    
    var viewModel: LoginViewModel
    var onNavigateToCountryPicker: (Country?) -> Void
    var onLoginSuccess: () -> Void
    var onCreateAccountClick: () -> Void
    var countryResult: Country?

    
    func makeUIViewController(context: Context) -> UIViewController {
        return LoginViewControllerKt.LoginViewController(
            nativeViewFactory: IOSNativeViewFactory.shared,
            viewModel: viewModel,
            onNavigateToCountryPicker: onNavigateToCountryPicker,
            onLoginSuccess: onLoginSuccess,
            onCreateAccountClick: onCreateAccountClick,
            countryResult: countryResult
        )
    }
    public func updateUIViewController(_ uiViewController: UIViewController, context: Context){
    }
}
