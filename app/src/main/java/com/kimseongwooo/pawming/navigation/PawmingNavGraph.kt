package com.kimseongwooo.pawming.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.kimseongwooo.pawming.designsystem.component.PawmingBottomNav
import com.kimseongwooo.pawming.designsystem.component.PawmingBottomNavItem
import com.kimseongwooo.pawming.designsystem.component.PawmingHeartIcon
import com.kimseongwooo.pawming.designsystem.component.PawmingHomeIcon
import com.kimseongwooo.pawming.designsystem.component.PawmingShelterIcon

private enum class Tab(
    val label: String,
    val startRoute: PawmingRoute
) {
    Home("홈", HomeRoute),
    Favorites("즐겨찾기", FavoritesRoute),
    Shelter("보호센터", ShelterRoute)
}

@Composable
fun PawmingNavGraph() {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.Home) }

    // 탭마다 독립적인 백스택 — 탭 전환 시 각 탭의 네비게이션 기록 유지
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
                entry<HomeRoute> {
                    PlaceholderScreen("홈")
                }
                entry<FavoritesRoute> {
                    PlaceholderScreen("즐겨찾기")
                }
                entry<ShelterRoute> {
                    PlaceholderScreen("보호센터")
                }
                entry<AnimalDetailRoute> { route ->
                    PlaceholderScreen("동물 상세\n${route.desertionNo}")
                }
                entry<ShelterDetailRoute> { route ->
                    PlaceholderScreen("보호소 상세\n${route.careRegNo}")
                }
            }
        )
    }
}

@Composable
private fun PlaceholderScreen(label: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label)
    }
}
