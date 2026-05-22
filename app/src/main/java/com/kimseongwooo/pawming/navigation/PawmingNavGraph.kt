package com.kimseongwooo.pawming.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.kimseongwooo.pawming.designsystem.component.PawmingBottomNav
import com.kimseongwooo.pawming.designsystem.component.PawmingBottomNavItem
import com.kimseongwooo.pawming.designsystem.component.PawmingHeartIcon
import com.kimseongwooo.pawming.designsystem.component.PawmingHomeIcon
import com.kimseongwooo.pawming.designsystem.component.PawmingShelterIcon
import com.kimseongwooo.pawming.feature.animaldetail.AnimalDetailRoute
import com.kimseongwooo.pawming.feature.animaldetail.navigation.animalDetailNavEntries
import com.kimseongwooo.pawming.feature.favorites.FavoritesRoute
import com.kimseongwooo.pawming.feature.favorites.navigation.favoritesNavEntries
import com.kimseongwooo.pawming.feature.home.HomeRoute
import com.kimseongwooo.pawming.feature.home.navigation.homeNavEntries
import com.kimseongwooo.pawming.feature.shelter.ShelterRoute
import com.kimseongwooo.pawming.feature.shelter.navigation.shelterNavEntries
import com.kimseongwooo.pawming.feature.shelterdetail.ShelterDetailRoute
import com.kimseongwooo.pawming.feature.shelterdetail.navigation.shelterDetailNavEntries

@Composable
fun PawmingNavGraph() {
    var selectedTab by rememberSaveable { mutableStateOf(TopLevelTab.Home) }

    // 탭별 독립 backstack — @Serializable NavKey 덕분에 프로세스 재시작 후에도 복원됨
    val homeBackStack = rememberNavBackStack(HomeRoute)
    val favoritesBackStack = rememberNavBackStack(FavoritesRoute)
    val shelterBackStack = rememberNavBackStack(ShelterRoute)

    val activeBackStack = when (selectedTab) {
        TopLevelTab.Home -> homeBackStack
        TopLevelTab.Favorites -> favoritesBackStack
        TopLevelTab.Shelter -> shelterBackStack
    }

    Scaffold(
        bottomBar = {
            PawmingBottomNav {
                TopLevelTab.entries.forEach { tab ->
                    PawmingBottomNavItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = tab.label,
                        icon = { active -> tab.Icon(active) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = activeBackStack,
            modifier = Modifier.padding(innerPadding),
            onBack = { activeBackStack.removeLastOrNull() },
            entryProvider = entryProvider {
                homeNavEntries(
                    onNavigateToAnimalDetail = { homeBackStack.add(AnimalDetailRoute(it)) }
                )
                favoritesNavEntries(
                    onNavigateToAnimalDetail = { favoritesBackStack.add(AnimalDetailRoute(it)) }
                )
                shelterNavEntries(
                    onNavigateToShelterDetail = { shelterBackStack.add(ShelterDetailRoute(it)) }
                )
                animalDetailNavEntries(
                    onBack = { activeBackStack.removeLastOrNull() }
                )
                shelterDetailNavEntries(
                    onBack = { activeBackStack.removeLastOrNull() }
                )
            }
        )
    }
}

private enum class TopLevelTab(val label: String) {
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
