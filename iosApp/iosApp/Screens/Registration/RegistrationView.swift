import SwiftUI
import shared

struct RegistrationView: View {
    @StateObject var viewModel = RegistrationViewModelHelper()

    var body: some View {
        VStack(spacing: 16) {
            if viewModel.currentState.verificationId == nil {
                Picker("Auth Method", selection: $viewModel.authMethod) {
                    Text("Email").tag(AuthAuthMethod.emailPassword)
                    Text("Phone").tag(AuthAuthMethod.phone)
                }
                .pickerStyle(SegmentedPickerStyle())

                if viewModel.authMethod == .emailPassword {
                    VStack(alignment: .leading) {
                        TextField("Email", text: $viewModel.email)
                            .keyboardType(.emailAddress)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                        if let error = viewModel.currentState.emailError {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                    }

                    VStack(alignment: .leading) {
                        SecureField("Password", text: $viewModel.password)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                        if let error = viewModel.currentState.passwordError {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                    }

                    VStack(alignment: .leading) {
                        SecureField("Confirm Password", text: $viewModel.confirmPassword)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                        if let error = viewModel.currentState.confirmPasswordError {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                    }
                } else {
                    VStack(alignment: .leading) {
                        PhoneTextFieldWithCountry(
                            countryFlag: viewModel.currentState.selectedCountry.flagEmoji,
                            countryDialCode: viewModel.currentState.selectedCountry.dialCode,
                            phoneNumber: $viewModel.phone,
                            onCountryCodeClick: {
                                viewModel.handleEvent(event: AuthRegistrationReducerEventCountryButtonClick())
                            },
                            onPhoneNumberChange: { newValue in
                                viewModel.handleEvent(event: AuthRegistrationReducerEventPhoneChanged(value: newValue))
                            }
                        )
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        if let error = viewModel.currentState.phoneError {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                    }
                }
            } else {
                OtpInputView(otp: $viewModel.otp, otpDigitCount: Int(viewModel.currentState.otpCount))
            }

            if let error = viewModel.currentState.error {
                Text(error)
                    .font(.caption)
                    .foregroundColor(.red)
            }

            Spacer()

            Button(action: {
                viewModel.handleEvent(event: AuthRegistrationReducerEventSubmit())
            }) {
                Text(buttonText)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(viewModel.currentState.isContinueButtonEnabled ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .disabled(!viewModel.currentState.isContinueButtonEnabled)
        }
        .padding()
        .navigationTitle("Sign Up")
    }

    private var buttonText: String {
        if viewModel.currentState.verificationId != nil {
            return "Verify OTP"
        } else if viewModel.authMethod == .phone {
            return "Send OTP"
        } else {
            return "Continue"
        }
    }
}

extension RegistrationViewModelHelper {
    @MainActor var authMethod: AuthAuthMethod {
        get {
            currentState.authMethod
        }
        set {
            handleEvent(event: AuthRegistrationReducerEventAuthMethodChanged(method: newValue))
        }
    }

    @MainActor var email: String {
        get {
            currentState.email
        }
        set {
            handleEvent(event: AuthRegistrationReducerEventEmailChanged(value: newValue))
        }
    }

    @MainActor var password: String {
        get {
            currentState.password
        }
        set {
            handleEvent(event: AuthRegistrationReducerEventPasswordChanged(value: newValue))
        }
    }

    @MainActor var confirmPassword: String {
        get {
            currentState.confirmPassword
        }
        set {
            handleEvent(event: AuthRegistrationReducerEventConfirmPasswordChanged(value: newValue))
        }
    }

    @MainActor var phone: String {
        get {
            currentState.phone
        }
        set {
            handleEvent(event: AuthRegistrationReducerEventPhoneChanged(value: newValue))
        }
    }

    @MainActor var otp: String {
        get {
            currentState.otp
        }
        set {
            handleEvent(event: AuthRegistrationReducerEventOtpChanged(value: newValue))
        }
    }
}
