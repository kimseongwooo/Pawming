package com.kimseongwooo.pawming.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface PawmingRoute : NavKey

/** 하단 탭 루트 */
@Serializable
data object HomeRoute : PawmingRoute

@Serializable
data object FavoritesRoute : PawmingRoute

@Serializable
data object ShelterRoute : PawmingRoute

/** 상세 화면 — 인자를 route key에 직접 포함 */
@Serializable
data class AnimalDetailRoute(val desertionNo: String) : PawmingRoute

@Serializable
data class ShelterDetailRoute(val careRegNo: String) : PawmingRoute
