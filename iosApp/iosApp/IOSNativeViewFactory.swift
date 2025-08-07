import SwiftUI
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {
    static var shared = IOSNativeViewFactory()
    func createProgressView() -> UIViewController {
        let view = ProgressView().progressViewStyle(.circular)

        return UIHostingController(rootView: view)
    }
    
    func createAppButton(text: String, onClick: @escaping () -> Void) -> UIViewController {
            return AppButtonViewController(text: text, onClick: onClick)
    }
}
