package com.kimseongwooo.pawming.navigation

import androidx.compose.runtime.Composable
import com.kimseongwooo.pawming.designsystem.component.PawmingHeartIcon
import com.kimseongwooo.pawming.designsystem.component.PawmingHomeIcon
import com.kimseongwooo.pawming.designsystem.component.PawmingShelterIcon

internal enum class TopLevelTab(val label: String) {
    Home("홈"),
    Favorites("즐겨찾기"),
    Shelter("보호센터");

    @Composable
    fun Icon(active: Boolean) = when (this) {
        Home -> PawmingHomeIcon(active = active)
        Favorites -> PawmingHeartIcon(active = active)
        Shelter -> PawmingShelterIcon(active = active)
    }
}