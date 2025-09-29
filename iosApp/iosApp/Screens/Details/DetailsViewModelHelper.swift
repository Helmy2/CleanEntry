import UIKit
import shared

class DetailsViewModelHelper: BaseViewModelHelper<
HomeImageDetailsReducerState, HomeImageDetailsReducerEvent,
HomeImageDetailsReducerEffect
> {

    @Published var showSaveAlert = false
    @Published var saveError: String? = nil

    init(_ imageId: Int64) {
        let vm = iOSApp.dependenciesHelper.detailsViewModel(id: imageId)
        super.init(viewModel: vm, initialState: vm.state.value)
    }

    public func triggerImageDownload() {
        guard let url = URL(string: currentState.currentImage?.large ?? "")
        else {
            return
        }

        URLSession.shared.dataTask(with: url) { data, _, error in
            guard let data = data, let image = UIImage(data: data), error == nil
            else {
                DispatchQueue.main.async {
                    self.saveError =
                        error?.localizedDescription ?? "Download failed"
                    self.showSaveAlert = true
                }
                return
            }

            DispatchQueue.main.async {
                let saver = ImageSaver()
                saver.onComplete = { error in
                    if let error = error {
                        self.saveError = error.localizedDescription
                    } else {
                        self.saveError = nil
                    }
                    self.showSaveAlert = true
                }
                saver.save(image)
            }
        }
        .resume()
    }
}

class ImageSaver: NSObject {
    var onComplete: ((Error?) -> Void)?

    func save(_ image: UIImage) {
        UIImageWriteToSavedPhotosAlbum(
            image,
            self,
            #selector(saveCompleted),
            nil
        )
    }

    @objc private func saveCompleted(
        _ image: UIImage,
        didFinishSavingWithError error: Error?,
        contextInfo: UnsafeRawPointer
    ) {
        onComplete?(error)
    }
}
