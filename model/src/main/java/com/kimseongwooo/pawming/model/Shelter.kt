package com.kimseongwooo.pawming.model

data class Shelter(
    val careRegNo: String,
    val careNm: String,
    val orgNm: String = "",
    val divisionNm: String = "",
    val saveTrgtAnimal: String = "",
    val careAddr: String = "",
    val careTel: String = "",
    val weekOprStime: String = "",
    val weekOprEtime: String = "",
    val weekendOprStime: String = "",
    val weekendOprEtime: String = "",
    val closeDay: String = ""
)
