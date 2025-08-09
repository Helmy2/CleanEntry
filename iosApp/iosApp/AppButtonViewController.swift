import Foundation
import UIKit
import SwiftUI
import ComposeApp

/**
 * A custom UIViewController that hosts a native UIButton and manages its state.
 * This class acts as the bridge that allows our shared Compose code to control
 * a native iOS button.
 */
class AppButtonViewController: UIViewController, NativeButtonController {
    
    private var button: UIButton!
    private var activityIndicator: UIActivityIndicatorView!
    private var buttonText: String = ""
    private var onClick: () -> Void
    
    init(text: String, onClick: @escaping () -> Void) {
        self.buttonText = text
        self.onClick = onClick
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        button = UIButton(type: .system)
        button.setTitle(buttonText, for: .normal)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(self, action: #selector(buttonTapped), for: .touchUpInside)
        
        button.backgroundColor = UIColor.darkGray
        button.setTitleColor(UIColor.white, for: .normal)
        button.layer.cornerRadius = 8
        
        activityIndicator = UIActivityIndicatorView(style: .medium)
        activityIndicator.translatesAutoresizingMaskIntoConstraints = false
        activityIndicator.hidesWhenStopped = true
        
        view.addSubview(button)
        view.addSubview(activityIndicator)
        
        NSLayoutConstraint.activate([
            button.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            button.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            button.topAnchor.constraint(equalTo: view.topAnchor),
            button.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            activityIndicator.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            activityIndicator.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
    }
    
    @objc private func buttonTapped() {
        onClick()
    }
        
    /**
     * Called from Kotlin to update the button's enabled state.
     */
    public func setEnabled(enabled: Bool) {
        button.isEnabled = enabled
        button.alpha = enabled ? 1.0 : 0.5
    }
    
    /**
     * Called from Kotlin to update the button's loading state.
     */
    public func setIsLoading(isLoading: Bool) {
        if isLoading {
            button.setTitle("", for: .normal)
            activityIndicator.startAnimating()
        } else {
            button.setTitle(buttonText, for: .normal)
            activityIndicator.stopAnimating()
        }
        button.isEnabled = !isLoading
    }
}
