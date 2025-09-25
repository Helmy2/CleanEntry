import SwiftUI
import shared

struct FeedView: View {
    @StateObject var viewModel: FeedViewModelHelper = FeedViewModelHelper()

    private let gridItems: [GridItem] = [
        .init(.flexible(), spacing: 8),
        .init(.flexible(), spacing: 8)
    ]

    var body: some View {
        ZStack {
            if viewModel.currentState.error != nil {
                VStack(spacing: 16) {
                    Text("Error")
                    Button("Retry") {
                        viewModel.handleEvent(event: HomeFeedReducerEventLoadImages())
                    }
                }
            } else if viewModel.currentState.isLoading {
                ScrollView {
                    LazyVGrid(columns: gridItems, spacing: 8) {
                        ForEach(0..<20, id: \.self) { _ in
                            RoundedRectangle(cornerRadius: 10)
                                .fill(Color.gray.opacity(0.3))
                                .frame(height: CGFloat.random(in: 100...300))
                        }
                    }
                    .padding()
                }
            } else {
                ScrollView {
                    LazyVGrid(columns: gridItems, spacing: 8) {
                        ForEach(viewModel.currentState.images, id: \.id) { image in
                            ImageCard(image: image)
                        }
                    }
                    .padding()
                }
            }
        }
    }
}

struct ImageCard: View {
    let image: shared.HomeImage

    var body: some View {
        VStack(alignment: .leading) {
            AsyncImage(url: URL(string: image.imageUrl)) { phase in
                switch phase {
                case .empty:
                    Rectangle()
                        .fill(Color.gray.opacity(0.3))
                        .aspectRatio(CGFloat(image.aspectRatio), contentMode: .fit)

                case .success(let img):
                    img.resizable()
                        .aspectRatio(contentMode: .fill)

                case .failure:
                    Rectangle()
                        .fill(Color.red.opacity(0.3))
                        .aspectRatio(CGFloat(image.aspectRatio), contentMode: .fit)
                @unknown default:
                    EmptyView()
                }
            }
            .frame(maxWidth: .infinity)
            .aspectRatio(CGFloat(image.aspectRatio), contentMode: .fit)
            .clipped()

            Text("Photo by \(image.photographer)")
                .font(.caption)
                .padding()
        }
        .background(Color(.secondarySystemBackground))
        .cornerRadius(10)
        .shadow(radius: 2)
    }
}
