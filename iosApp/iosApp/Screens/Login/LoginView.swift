import SwiftUI
import shared

struct LoginView: View {
    @StateObject var viewModel = LoginViewModelHelper()

    var body: some View {
        VStack(spacing: 16) {
            if viewModel.currentState.verificationId == nil {
                Picker("Auth Method", selection: $viewModel.authMethod) {
                    Text("Email").tag(AuthAuthMethod.emailPassword)
                    Text("Phone").tag(AuthAuthMethod.phone)
                }
                .pickerStyle(SegmentedPickerStyle())
                .disabled(viewModel.currentState.verificationId != nil)

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
                } else {
                    VStack(alignment: .leading) {
                        PhoneTextFieldWithCountry(
                            countryFlag: viewModel.currentState.selectedCountry.flagEmoji,
                            countryDialCode: viewModel.currentState.selectedCountry.dialCode,
                            phoneNumber: $viewModel.phone,
                            onCountryCodeClick: {
                                viewModel.handleEvent(event: AuthLoginReducerEventCountryButtonClick())
                            },
                            onPhoneNumberChange: { newValue in
                                viewModel.handleEvent(event: AuthLoginReducerEventPhoneChanged(value: newValue))
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
                viewModel.handleEvent(event: AuthLoginReducerEventLoginClicked())
            }) {
                Text(buttonText)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(viewModel.currentState.isLoginButtonEnabled ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .disabled(!viewModel.currentState.isLoginButtonEnabled)

            HStack {
                Text("You don\'t have an account?")
                    .font(.body)
                Button(action: {
                    viewModel.handleEvent(event: AuthLoginReducerEventCreateAccountClicked())
                }) {
                    Text("Create Account")
                        .font(.body)
                        .foregroundColor(.blue)
                }
            }
        }
        .padding()
        .navigationTitle("Login")
    }

    private var buttonText: String {
        if viewModel.currentState.verificationId != nil {
            return "Verify OTP"
        } else if viewModel.authMethod == .phone {
            return "Send OTP"
        } else {
            return "Login"
        }
    }
}

struct PhoneTextFieldWithCountry: View {
    var countryFlag: String
    var countryDialCode: String
    @Binding var phoneNumber: String
    var onCountryCodeClick: () -> Void
    var onPhoneNumberChange: (String) -> Void

    var body: some View {
        HStack {
            Button(action: onCountryCodeClick) {
                HStack {
                    Text(countryFlag)
                    Text(countryDialCode)
                }
                .padding(.horizontal, 8)
                .padding(.vertical, 12)
                .cornerRadius(8)
            }

            TextField("Phone", text: $phoneNumber)
            .keyboardType(.phonePad)
            .onChange(of: phoneNumber) { _, newValue in
                onPhoneNumberChange(newValue)
            }
            .textFieldStyle(PlainTextFieldStyle())
        }
        .padding(.horizontal, 8)
        .background(Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
}

struct OtpInputView: View {
    @Binding var otp: String
    let otpDigitCount: Int
    @FocusState private var isFocused: Bool

    var body: some View {
        ZStack {
            TextField("", text: $otp)
                .keyboardType(.numberPad)
                .textContentType(.oneTimeCode)
                .frame(width: 0, height: 0)
                .focused($isFocused)
                .onChange(of: otp) { _, newValue in
                    if newValue.count > otpDigitCount {
                        otp = String(newValue.prefix(otpDigitCount))
                    }
                }

            HStack(spacing: 10) {
                ForEach(0..<otpDigitCount, id: \.self) { index in
                    Text(character(at: index))
                        .frame(width: 40, height: 50)
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(8)
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(isFocused && otp.count == index ? Color.blue : Color.clear, lineWidth: 2)
                        )
                }
            }
            .onTapGesture {
                isFocused = true
            }
        }
        .onAppear {
            isFocused = true
        }
    }

    private func character(at index: Int) -> String {
        guard otp.count > index else {
            return ""
        }
        return String(otp[otp.index(otp.startIndex, offsetBy: index)])
    }
}

extension LoginViewModelHelper {
    @MainActor var authMethod: AuthAuthMethod {
        get {
            currentState.authMethod
        }
        set {
            handleEvent(event: AuthLoginReducerEventAuthMethodChanged(method: newValue))
        }
    }

    @MainActor var email: String {
        get {
            currentState.email
        }
        set {
            handleEvent(event: AuthLoginReducerEventEmailChanged(value: newValue))
        }
    }

    @MainActor var password: String {
        get {
            currentState.password
        }
        set {
            handleEvent(event: AuthLoginReducerEventPasswordChanged(value: newValue))
        }
    }

    @MainActor var phone: String {
        get {
            currentState.phone
        }
        set {
            handleEvent(event: AuthLoginReducerEventPhoneChanged(value: newValue))
        }
    }

    @MainActor var otp: String {
        get {
            currentState.otp
        }
        set {
            handleEvent(event: AuthLoginReducerEventOtpChanged(value: newValue))
        }
    }
}
