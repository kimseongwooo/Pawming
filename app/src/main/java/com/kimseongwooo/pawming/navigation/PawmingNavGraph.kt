package com.kimseongwooo.pawming.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
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

private enum class Tab(
    val label: String,
    val startRoute: NavKey
) {
    Home("홈", HomeRoute),
    Favorites("즐겨찾기", FavoritesRoute),
    Shelter("보호센터", ShelterRoute)
}

@Composable
fun PawmingNavGraph() {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.Home) }

    val homeBackStack = rememberNavBackStack(HomeRoute)
    val favoritesBackStack = rememberNavBackStack(FavoritesRoute)
    val shelterBackStack = rememberNavBackStack(ShelterRoute)

    val activeBackStack = when (selectedTab) {
        Tab.Home -> homeBackStack
        Tab.Favorites -> favoritesBackStack
        Tab.Shelter -> shelterBackStack
    }

    Scaffold(
        bottomBar = {
            PawmingBottomNav {
                Tab.entries.forEach { tab ->
                    PawmingBottomNavItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = tab.label,
                        icon = { active ->
                            when (tab) {
                                Tab.Home -> PawmingHomeIcon(active = active)
                                Tab.Favorites -> PawmingHeartIcon(active = active)
                                Tab.Shelter -> PawmingShelterIcon(active = active)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = activeBackStack,
            modifier = Modifier.padding(innerPadding),
            onBack = { _ -> activeBackStack.removeLastOrNull() },
            entryProvider = entryProvider {
                homeNavEntries(
                    onNavigateToAnimalDetail = { desertionNo ->
                        homeBackStack.add(AnimalDetailRoute(desertionNo))
                    }
                )
                favoritesNavEntries(
                    onNavigateToAnimalDetail = { desertionNo ->
                        favoritesBackStack.add(AnimalDetailRoute(desertionNo))
                    }
                )
                shelterNavEntries(
                    onNavigateToShelterDetail = { careRegNo ->
                        shelterBackStack.add(ShelterDetailRoute(careRegNo))
                    }
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
