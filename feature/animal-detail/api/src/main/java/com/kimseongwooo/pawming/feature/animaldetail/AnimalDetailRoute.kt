package com.kimseongwooo.pawming.feature.animaldetail

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AnimalDetailRoute(val desertionNo: String) : NavKey
