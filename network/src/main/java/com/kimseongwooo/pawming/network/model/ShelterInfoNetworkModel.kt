package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ShelterInfoNetworkModel(
    val careNm: String? = null,
    val careRegNo: String? = null,
    val orgNm: String? = null,
    val divisionNm: String? = null,
    val saveTrgtAnimal: String? = null,
    val careAddr: String? = null,
    val jibunAddr: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val dsignationDate: String? = null,
    val careTel: String? = null,
    val dataStdDt: String? = null,

    val weekOprStime: String? = null,
    val weekOprEtime: String? = null,
    val weekCellStime: String? = null,
    val weekCellEtime: String? = null,
    val weekendOprStime: String? = null,
    val weekendOprEtime: String? = null,
    val weekendCellStime: String? = null,
    val weekendCellEtime: String? = null,
    val closeDay: String? = null,

    val vetPersonCnt: String? = null,
    val specsPersonCnt: String? = null,
    val medicalCnt: String? = null,
    val breedCnt: String? = null,
    val quarabtineCnt: String? = null,
    val feedCnt: String? = null,
    val transCarCnt: String? = null,
)
