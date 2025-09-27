import SwiftUI
import shared

class ProfileViewModelHelper: BaseViewModelHelper<AuthProfileReducerState, AuthProfileReducerEvent, AuthProfileReducerEffect> {
    init() {
        let vm = iOSApp.dependenciesHelper.profileViewModel
        super.init(viewModel: vm, initialState: vm.state.value)
    }
}
