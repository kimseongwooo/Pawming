package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ShelterInfoNetworkModel(
    // 기본 정보
    val careNm: String?,
    val careRegNo: String?,
    val orgNm: String?,
    val divisionNm: String?,
    val saveTrgtAnimal: String?,
    val careAddr: String?,
    val jibunAddr: String?,
    val lat: String?,
    val lng: String?,
    val dsignationDate: String?,
    val careTel: String?,
    val dataStdDt: String?,

    // 운영시간
    val weekOprStime: String?,
    val weekOprEtime: String?,
    val weekCellStime: String?,
    val weekCellEtime: String?,
    val weekendOprStime: String?,
    val weekendOprEtime: String?,
    val weekendCellStime: String?,
    val weekendCellEtime: String?,
    val closeDay: String?,

    // 시설 규모
    val vetPersonCnt: String?,
    val specsPersonCnt: String?,
    val medicalCnt: String?,
    val breedCnt: String?,
    val quarabtineCnt: String?,
    val feedCnt: String?,
    val transCarCnt: String?,
)