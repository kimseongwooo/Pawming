package com.kimseongwooo.pawming.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sigungu")
data class SigunguEntity(
    @PrimaryKey val orgCd: String,
    val orgdownNm: String,
    val uprCd: String
)

// uprCd별 조회 완료 여부를 추적 — 빈 결과(세종시 등)도 캐시 히트로 판별하기 위해 별도 관리
@Entity(tableName = "sigungu_fetch_log")
data class SigunguFetchLogEntity(
    @PrimaryKey val uprCd: String
)
