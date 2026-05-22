package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SigunguNetworkModel(
    val orgCd: String,
    val orgdownNm: String,
    val uprCd: String
)