package com.kimseongwooo.pawming.domain.repository

import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.ShelterDetail

interface ShelterRepository {
    suspend fun getShelters(uprCd: String? = null, orgCd: String? = null): Result<List<Shelter>>
    suspend fun getShelterDetail(careRegNo: String): Result<ShelterDetail>
}
