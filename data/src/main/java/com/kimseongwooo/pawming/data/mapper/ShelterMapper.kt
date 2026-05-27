package com.kimseongwooo.pawming.data.mapper

import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.ShelterDetail
import com.kimseongwooo.pawming.network.model.ShelterInfoNetworkModel
import com.kimseongwooo.pawming.network.model.ShelterNetworkModel

fun ShelterNetworkModel.toDomain(): Shelter = Shelter(
    careRegNo = careRegNo,
    careNm = careNm,
    orgNm = orgNm.orEmpty(),
    divisionNm = divisionNm.orEmpty(),
    saveTrgtAnimal = saveTrgtAnimal.orEmpty(),
    careAddr = careAddr.orEmpty(),
    careTel = careTel.orEmpty(),
    weekOprStime = weekOprStime.orEmpty(),
    weekOprEtime = weekOprEtime.orEmpty(),
    weekendOprStime = weekendOprStime.orEmpty(),
    weekendOprEtime = weekendOprEtime.orEmpty(),
    closeDay = closeDay.orEmpty()
)

fun ShelterInfoNetworkModel.toShelter(): Shelter = Shelter(
    careRegNo = careRegNo.orEmpty(),
    careNm = careNm.orEmpty(),
    orgNm = orgNm.orEmpty(),
    divisionNm = divisionNm.orEmpty(),
    saveTrgtAnimal = saveTrgtAnimal.orEmpty(),
    careAddr = careAddr.orEmpty(),
    careTel = careTel.orEmpty(),
    weekOprStime = weekOprStime.orEmpty(),
    weekOprEtime = weekOprEtime.orEmpty(),
    weekendOprStime = weekendOprStime.orEmpty(),
    weekendOprEtime = weekendOprEtime.orEmpty(),
    closeDay = closeDay.orEmpty()
)

fun ShelterInfoNetworkModel.toDomain(): ShelterDetail = ShelterDetail(
    careRegNo = careRegNo.orEmpty(),
    careNm = careNm.orEmpty(),
    orgNm = orgNm.orEmpty(),
    divisionNm = divisionNm.orEmpty(),
    saveTrgtAnimal = saveTrgtAnimal.orEmpty(),
    careAddr = careAddr.orEmpty(),
    jibunAddr = jibunAddr.orEmpty(),
    lat = lat?.toDoubleOrNull(),
    lng = lng?.toDoubleOrNull(),
    dsignationDate = dsignationDate.orEmpty(),
    careTel = careTel.orEmpty(),
    dataStdDt = dataStdDt.orEmpty(),
    weekOprStime = weekOprStime.orEmpty(),
    weekOprEtime = weekOprEtime.orEmpty(),
    weekCellStime = weekCellStime.orEmpty(),
    weekCellEtime = weekCellEtime.orEmpty(),
    weekendOprStime = weekendOprStime.orEmpty(),
    weekendOprEtime = weekendOprEtime.orEmpty(),
    weekendCellStime = weekendCellStime.orEmpty(),
    weekendCellEtime = weekendCellEtime.orEmpty(),
    closeDay = closeDay.orEmpty(),
    vetPersonCnt = vetPersonCnt?.toIntOrNull() ?: 0,
    specsPersonCnt = specsPersonCnt?.toIntOrNull() ?: 0,
    medicalCnt = medicalCnt?.toIntOrNull() ?: 0,
    breedCnt = breedCnt?.toIntOrNull() ?: 0,
    quarantineCnt = quarabtineCnt?.toIntOrNull() ?: 0,
    feedCnt = feedCnt?.toIntOrNull() ?: 0,
    transCarCnt = transCarCnt?.toIntOrNull() ?: 0
)
