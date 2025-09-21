import SwiftUI
import ComposeApp

struct LoginView: View {
    @StateObject private var helper = LoginViewModelHelper()
    @State private var phone: String = ""
    @State private var password: String = ""

    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Spacer()
                Text("Login")
                    .font(.headline)
                Spacer()
            }
            .padding()

            VStack(alignment: .leading) {
                PhoneTextFieldWithCountry(
                    countryFlag: helper.currentState.selectedCountry.flagEmoji,
                    countryDialCode: helper.currentState.selectedCountry.dialCode,
                    phoneNumber: $phone,
                    onCountryCodeClick: {
                        helper.handleEvent(event: AuthLoginReducerEventCountryButtonClick())
                    },
                    onPhoneNumberChange: { newValue in
                        helper.handleEvent(event: AuthLoginReducerEventPhoneChanged(value: newValue))
                    }
                )
                .textFieldStyle(RoundedBorderTextFieldStyle())

                if let error = helper.currentState.phoneError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                SecureField("Password", text: $password)
                .onChange(of: password) { _, newValue in
                    helper.handleEvent(event: AuthLoginReducerEventPasswordChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())

                if let error = helper.currentState.passwordError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            Spacer()

            Button(action: {
                helper.handleEvent(event: AuthLoginReducerEventLoginClicked())
            }) {
                Text("Continue")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(helper.currentState.isLoginButtonEnabled ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .disabled(!helper.currentState.isLoginButtonEnabled)

            HStack {
                Text("You don't have an account?")
                    .font(.body)
                Button(action: {
                    helper.handleEvent(event: AuthLoginReducerEventCreateAccountClicked())
                }) {
                    Text("Create Account")
                        .font(.body)
                        .foregroundColor(.blue)
                }
            }
        }
        .padding()
        .onAppear {
            helper.start()
        }
        .onDisappear {
            helper.stop()
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
