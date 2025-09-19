import SwiftUI
import ComposeApp

struct LoginView: View {
    @ObservedObject var viewModel: LoginViewModelHelper
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
                    countryFlag: viewModel.selectedCountry.flagEmoji,
                    countryDialCode: viewModel.selectedCountry.dialCode,
                    phoneNumber: $phone,
                    onCountryCodeClick: {
                        viewModel.loginViewModel.handleEvent(event: AuthLoginReducerEventCountryButtonClick())
                    },
                    onPhoneNumberChange: { newValue in
                        viewModel.loginViewModel.handleEvent(event: AuthLoginReducerEventPhoneChanged(value: newValue))
                    }
                )
                .textFieldStyle(RoundedBorderTextFieldStyle())

                if let error = viewModel.phoneError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                SecureField("Password", text: $password)
                .onChange(of: password) { _, newValue in
                    viewModel.loginViewModel.handleEvent(event: AuthLoginReducerEventPasswordChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())

                if let error = viewModel.passwordError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            Spacer()

            Button(action: {
                viewModel.loginViewModel.handleEvent(event: AuthLoginReducerEventLoginClicked())
            }) {
                Text("Continue")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(viewModel.loginViewModel.state.value.isLoginButtonEnabled ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .disabled(!viewModel.loginViewModel.state.value.isLoginButtonEnabled)

            HStack {
                Text("You don't have an account?")
                    .font(.body)
                Button(action: {
                    viewModel.loginViewModel.handleEvent(event: AuthLoginReducerEventCreateAccountClicked())
                }) {
                    Text("Create Account")
                        .font(.body)
                        .foregroundColor(.blue)
                }
            }
        }
        .padding()
        .onAppear {
            viewModel.start()
        }
        .onDisappear {
            viewModel.stop()
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
