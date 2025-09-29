import SwiftUI
import shared

struct DetailsView: View {
    @StateObject var helper: DetailsViewModelHelper

    var body: some View {
        ScrollView(.vertical) {
            VStack(alignment: .leading) {
                AsyncImage(
                    url: URL(
                        string: helper.currentState.currentImage?.large ?? ""
                    )
                ) { phase in
                    switch phase {
                    case .empty:
                        Rectangle()
                            .fill(Color.gray.opacity(0.3))
                            .aspectRatio(
                                CGFloat(
                                    helper.currentState.currentImage?
                                        .aspectRatio ?? 1.0
                                ),
                                contentMode: .fit
                            )

                    case .success(let img):
                        img.resizable()
                            .aspectRatio(
                                CGFloat(
                                    helper.currentState.currentImage?
                                        .aspectRatio ?? 1.0
                                ),
                                contentMode: .fit
                            )

                    case .failure:
                        Rectangle()
                            .fill(Color.red.opacity(0.3))
                            .aspectRatio(
                                CGFloat(
                                    helper.currentState.currentImage?
                                        .aspectRatio ?? 1.0
                                ),
                                contentMode: .fit
                            )
                    @unknown default:
                        EmptyView()
                    }
                }

                Text(
                    "Photographer: \(helper.currentState.currentImage?.photographer ?? "")"
                )
                    .font(.caption)
                    .padding(8)

                Button(action: { helper.triggerImageDownload() }) {
                    Text("Download Image")
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
                .padding(.horizontal)
                .alert(isPresented: $helper.showSaveAlert) {
                    if let error = helper.saveError {
                        return Alert(
                            title: Text("Error"),
                            message: Text(error),
                            dismissButton: .default(Text("OK"))
                        )
                    } else {
                        return Alert(
                            title: Text("Success"),
                            message: Text("Image saved to Photos 🎉"),
                            dismissButton: .default(Text("OK"))
                        )
                    }
                }

                Text(
                    "Description \(String(describing: helper.currentState.currentImage?.alt))"
                )
                    .font(.caption)
                    .padding(8)

                Text("Similar Images")
                    .font(.caption)
                    .padding(8)

                SimilarImagesView(
                    list: helper.currentState.similarImages
                )

            }
        }
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 10))
        .shadow(radius: 2)
    }
}

struct SimilarImagesView: View {

    var list: [HomeImage?]

    var body: some View {
        ScrollView(.horizontal) {
            LazyHStack(spacing: 10) {
                ForEach(list, id: \.self) { item in
                    AsyncImage(url: URL(string: item?.large ?? "")) { phase in
                        switch phase {
                        case .empty:
                            Rectangle()
                                .fill(Color.gray.opacity(0.3))
                                .aspectRatio(
                                    CGFloat(item?.aspectRatio ?? 1.0),
                                    contentMode: .fit
                                )

                        case .success(let img):
                            img.resizable()
                                .aspectRatio(
                                    CGFloat(item?.aspectRatio ?? 1.0),
                                    contentMode: .fit
                                )

                        case .failure:
                            Rectangle()
                                .fill(Color.red.opacity(0.3))
                                .aspectRatio(
                                    CGFloat(item?.aspectRatio ?? 1.0),
                                    contentMode: .fit
                                )
                        @unknown default:
                            EmptyView()
                        }
                    }
                    .frame(height: 200)
                }
            }
            .padding()
        }
    }
}
