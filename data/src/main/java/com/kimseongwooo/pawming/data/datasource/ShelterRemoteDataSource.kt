package com.kimseongwooo.pawming.data.datasource

import com.kimseongwooo.pawming.network.model.ShelterInfoNetworkModel
import com.kimseongwooo.pawming.network.model.ShelterNetworkModel

interface ShelterRemoteDataSource {
    suspend fun getShelters(uprCd: String?, orgCd: String?): List<ShelterNetworkModel>
    suspend fun getShelterDetail(careRegNo: String): ShelterInfoNetworkModel?
}
