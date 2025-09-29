import shared


class DetailsViewModelHelper: BaseViewModelHelper<HomeImageDetailsReducerState, HomeImageDetailsReducerEvent, HomeImageDetailsReducerEffect> {

    init(_ imageId: Int64) {
        let vm = iOSApp.dependenciesHelper.detailsViewModel(id: imageId)
        super.init(viewModel: vm, initialState: vm.state.value)
    }
}
