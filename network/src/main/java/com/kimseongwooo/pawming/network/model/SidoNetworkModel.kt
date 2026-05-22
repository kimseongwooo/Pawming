package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SidoNetworkModel(
    val orgCd: String,
    val orgdownNm: String
)