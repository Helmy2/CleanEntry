import SwiftUI
import shared

struct FeedView: View {
    @StateObject var viewModel: FeedViewModelHelper = FeedViewModelHelper()

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
                let shimmerItems = (0..<10).map { _ in ShimmerItem() }
                StaggeredGrid(columns: 2, list: shimmerItems) { _ in
                    RoundedRectangle(cornerRadius: 10)
                        .fill(Color.gray.opacity(0.3))
                        .frame(height: CGFloat.random(in: 100...300))
                }
                .padding(.horizontal)

            } else {
                StaggeredGrid(columns: 2, list: viewModel.currentState.images) { image in
                    ImageCard(image: image)
                }
                .padding(.horizontal)
            }
        }
        .navigationTitle("Feed")
        .onAppear {
            if viewModel.currentState.images.isEmpty {
                viewModel.handleEvent(event: HomeFeedReducerEventLoadImages())
            }
        }
    }
}

private struct ShimmerItem: Identifiable, Hashable {
    let id = UUID()
}

struct ImageCard: View {
    let image: shared.HomeImage

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
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

            Text("Photo by \(image.photographer)")
                .font(.caption)
                .padding(8)
        }
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 10))
        .shadow(radius: 2)
    }
}

extension HomeImage: @retroactive Identifiable{}
