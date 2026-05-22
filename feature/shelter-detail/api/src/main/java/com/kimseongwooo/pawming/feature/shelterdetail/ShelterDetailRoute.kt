package com.kimseongwooo.pawming.feature.shelterdetail

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ShelterDetailRoute(val careRegNo: String) : NavKey
