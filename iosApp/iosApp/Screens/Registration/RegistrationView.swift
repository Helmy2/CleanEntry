import SwiftUI
import shared

struct RegistrationView: View {
    @ObservedObject var viewModel = RegistrationViewModelHelper()
    @State private var firstName: String = ""
    @State private var surname: String = ""
    @State private var email: String = ""
    @State private var phone: String = ""


    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Spacer()
                Text("Sign Up")
                    .font(.headline)
                Spacer()
            }
            .padding()

            VStack(alignment: .leading) {
                TextField("First Name", text: $firstName)
                .onChange(of: firstName) { _, newValue in
                    viewModel.handleEvent(event: AuthRegistrationReducerEventFirstNameChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())
                if let error = viewModel.currentState.firstNameError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                TextField("Surname", text: $surname)
                .onChange(of: surname) { _, newValue in
                    viewModel.handleEvent(event: AuthRegistrationReducerEventSurnameChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())
                if let error = viewModel.currentState.surnameError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                TextField("Email", text: $email)
                .keyboardType(.emailAddress)
                .onChange(of: email) { _, newValue in
                    viewModel.handleEvent(event: AuthRegistrationReducerEventEmailChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())
                if let error = viewModel.currentState.emailError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                PhoneTextFieldWithCountry(
                    countryFlag: viewModel.currentState.selectedCountry.flagEmoji,
                    countryDialCode: viewModel.currentState.selectedCountry.dialCode,
                    phoneNumber: $phone,
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

            Spacer()

            Button(action: {
                viewModel.handleEvent(event: AuthRegistrationReducerEventSubmit())
            }) {
                Text("Continue")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(viewModel.currentState.isContinueButtonEnabled ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .disabled(!viewModel.currentState.isContinueButtonEnabled)
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
