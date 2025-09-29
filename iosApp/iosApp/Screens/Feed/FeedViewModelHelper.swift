import SwiftUI
import shared

class FeedViewModelHelper: BaseViewModelHelper<HomeFeedReducerState, HomeFeedReducerEvent, HomeFeedReducerEffect> {

    init() {
        let vm = iOSApp.dependenciesHelper.feedViewModel
        super.init(viewModel: vm, initialState: vm.state.value)
    }
}
