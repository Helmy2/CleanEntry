package com.example.clean.entry.feature_auth.util

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createProgressView(): UIViewController
    fun createAppButton(text: String, onClick: () -> Unit): UIViewController
}

