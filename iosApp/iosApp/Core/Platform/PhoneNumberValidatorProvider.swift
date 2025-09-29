import Foundation
import PhoneNumberKit
import shared

class IosPhoneNumberValidatorProvider: CorePhoneNumberVerifier {
    private let phoneNumberUtility = PhoneNumberUtility()

    func isValidNumber(phone: String, regionCode: String) -> Bool {
        do {
            let phoneNumber = try phoneNumberUtility.parse(phone, withRegion: regionCode, ignoreType: true)
            return phoneNumberUtility.isValidPhoneNumber(phoneNumber.numberString)
        } catch {
            return false
        }
    }
}


