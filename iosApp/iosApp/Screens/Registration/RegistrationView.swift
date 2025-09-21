import SwiftUI
import shared

struct RegistrationView: View {
    @ObservedObject var viewModel: RegistrationViewModelHelper
    @State private var firstName: String = ""
    @State private var surname: String = ""
    @State private var email: String = ""
    @State private var phone: String = ""

    init() {
        self.viewModel = RegistrationViewModelHelper()
    }

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
                    viewModel.registrationViewModel.handleEvent(event: AuthRegistrationReducerEventFirstNameChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())
                if let error = viewModel.firstNameError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                TextField("Surname", text: $surname)
                .onChange(of: surname) { _, newValue in
                    viewModel.registrationViewModel.handleEvent(event: AuthRegistrationReducerEventSurnameChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())
                if let error = viewModel.surnameError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                TextField("Email", text: $email)
                .keyboardType(.emailAddress)
                .onChange(of: email) { _, newValue in
                    viewModel.registrationViewModel.handleEvent(event: AuthRegistrationReducerEventEmailChanged(value: newValue))
                }
                .textFieldStyle(RoundedBorderTextFieldStyle())
                if let error = viewModel.emailError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            VStack(alignment: .leading) {
                PhoneTextFieldWithCountry(
                    countryFlag: viewModel.selectedCountry.flagEmoji,
                    countryDialCode: viewModel.selectedCountry.dialCode,
                    phoneNumber: $phone,
                    onCountryCodeClick: {
                        viewModel.registrationViewModel.handleEvent(event: AuthRegistrationReducerEventCountryButtonClick())
                    },
                    onPhoneNumberChange: { newValue in
                        viewModel.registrationViewModel.handleEvent(event: AuthRegistrationReducerEventPhoneChanged(value: newValue))
                    }
                )
                .textFieldStyle(RoundedBorderTextFieldStyle())
                if let error = viewModel.phoneError {
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            Spacer()

            Button(action: {
                viewModel.registrationViewModel.handleEvent(event: AuthRegistrationReducerEventSubmit())
            }) {
                Text("Continue")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(viewModel.registrationViewModel.state.value.isContinueButtonEnabled ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .disabled(!viewModel.registrationViewModel.state.value.isContinueButtonEnabled)
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
