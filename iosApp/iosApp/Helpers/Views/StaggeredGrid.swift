import SwiftUI

struct StaggeredGrid<Content: View, T: Identifiable>: View where T: Hashable {
    let content: (T) -> Content
    let list: [T]
    var columns: Int
    var showsIndicators: Bool
    var spacing: CGFloat

    init(columns: Int, showsIndicators: Bool = false, spacing: CGFloat = 8, list: [T], @ViewBuilder content: @escaping (T) -> Content) {
        self.content = content
        self.list = list
        self.spacing = spacing
        self.showsIndicators = showsIndicators
        self.columns = columns
    }

    func setUpList() -> [[T]] {
        var gridArray: [[T]] = Array(repeating: [], count: columns)
        var currentIndex = 0

        for object in list {
            gridArray[currentIndex].append(object)

            if currentIndex == (columns - 1) {
                currentIndex = 0
            } else {
                currentIndex += 1
            }
        }

        return gridArray
    }

    var body: some View {
        ScrollView(.vertical, showsIndicators: showsIndicators) {
            HStack(alignment: .top, spacing: spacing) {
                ForEach(setUpList(), id: \.self) { columnData in
                    LazyVStack(spacing: spacing) {
                        ForEach(columnData) { object in
                            content(object)
                        }
                    }
                }
            }
            .padding(.vertical)
        }
    }
}
