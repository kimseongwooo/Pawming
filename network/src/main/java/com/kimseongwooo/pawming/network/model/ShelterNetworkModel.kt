package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ShelterNetworkModel(
    val careRegNo: String,
    val careNm: String,
    val orgNm: String? = null,
    val divisionNm: String? = null,
    val saveTrgtAnimal: String? = null,
    val careAddr: String? = null,
    val careTel: String? = null,
    val weekOprStime: String? = null,
    val weekOprEtime: String? = null,
    val weekendOprStime: String? = null,
    val weekendOprEtime: String? = null,
    val closeDay: String? = null
)
