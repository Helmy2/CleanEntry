//
//  HomeView.swift
//  iosApp
//
//  Created by Platinum on 20/06/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct FeedView: View {
    @StateObject var viewModel: FeedViewModelHelper = FeedViewModelHelper()

    var body: some View {
        Text("Home View")
    }
}
