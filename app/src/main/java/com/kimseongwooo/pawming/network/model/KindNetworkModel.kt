package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class KindNetworkModel(
    val kindCd: String,
    val kindNm: String
)