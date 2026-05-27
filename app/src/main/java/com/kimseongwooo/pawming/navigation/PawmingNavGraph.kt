package com.kimseongwooo.pawming.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
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
    var pendingShelterId by rememberSaveable { mutableStateOf("") }
    var pendingShelterNm by rememberSaveable { mutableStateOf("") }

    // 탭별 독립 backstack — @Serializable NavKey 덕분에 프로세스 재시작 후에도 복원됨
    val homeBackStack = rememberNavBackStack(HomeRoute)
    val favoritesBackStack = rememberNavBackStack(FavoritesRoute)
    val shelterBackStack = rememberNavBackStack(ShelterRoute)

    val activeBackStack = when (selectedTab) {
        TopLevelTab.Home -> homeBackStack
        TopLevelTab.Favorites -> favoritesBackStack
        TopLevelTab.Shelter -> shelterBackStack
    }

    // NavDisplay 내부 BackHandler보다 낮은 우선순위. NavDisplay가 루트에서 back을
    // 비활성화할 때(previousEntries가 빈 경우)만 동작하여 Home 탭으로 전환.
    BackHandler(enabled = selectedTab != TopLevelTab.Home) {
        selectedTab = TopLevelTab.Home
    }

    NavDisplay(
        backStack = activeBackStack,
        onBack = { activeBackStack.removeLastOrNull() },
        sceneStrategies = listOf(
            PawmingSceneStrategy(
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
            )
        ),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            homeNavEntries(
                onNavigateToAnimalDetail = { homeBackStack.add(AnimalDetailRoute(it)) },
                pendingShelterId = pendingShelterId,
                pendingShelterNm = pendingShelterNm,
                onShelterFilterApplied = {
                    pendingShelterId = ""
                    pendingShelterNm = ""
                },
                metadata = topLevelMetadata()
            )
            favoritesNavEntries(
                onNavigateToAnimalDetail = { favoritesBackStack.add(AnimalDetailRoute(it)) },
                metadata = topLevelMetadata()
            )
            shelterNavEntries(
                onNavigateToShelterDetail = { shelterBackStack.add(ShelterDetailRoute(it)) },
                metadata = topLevelMetadata()
            )
            animalDetailNavEntries(
                onBack = { activeBackStack.removeLastOrNull() }
            )
            shelterDetailNavEntries(
                onBack = { activeBackStack.removeLastOrNull() },
                onViewAnimalsWithShelterFilter = { careRegNo, careNm ->
                    pendingShelterId = careRegNo
                    pendingShelterNm = careNm
                    while (shelterBackStack.size > 1) shelterBackStack.removeLastOrNull()
                    selectedTab = TopLevelTab.Home
                }
            )
        }
    )
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
