package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ShelterNetworkModel(
    val careRegNo: String,
    val careNm: String
)