package com.kimseongwooo.pawming.data.datasource

import com.kimseongwooo.pawming.network.AnimalApiService
import com.kimseongwooo.pawming.network.ShelterInfoApiService
import com.kimseongwooo.pawming.network.model.ShelterInfoNetworkModel
import com.kimseongwooo.pawming.network.model.ShelterNetworkModel
import javax.inject.Inject

class ShelterRemoteDataSourceImpl @Inject constructor(
    private val animalApiService: AnimalApiService,
    private val shelterInfoApiService: ShelterInfoApiService
) : ShelterRemoteDataSource {

    override suspend fun getShelters(uprCd: String?, orgCd: String?): List<ShelterNetworkModel> =
        animalApiService.getShelter(uprCd, orgCd)
            .response.body?.items?.item ?: emptyList()

    override suspend fun getShelterDetail(careRegNo: String): ShelterInfoNetworkModel? =
        shelterInfoApiService.getShelterInfo(careRegNo)
            .response.body?.items?.item?.firstOrNull()
}
