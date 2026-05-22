package com.kimseongwooo.pawming.data.datasource

import com.kimseongwooo.pawming.network.AnimalApiService
import com.kimseongwooo.pawming.network.model.AnimalNetworkModel
import com.kimseongwooo.pawming.network.model.KindNetworkModel
import com.kimseongwooo.pawming.network.model.SidoNetworkModel
import com.kimseongwooo.pawming.network.model.SigunguNetworkModel
import javax.inject.Inject

class AnimalRemoteDataSourceImpl @Inject constructor(
    private val animalApiService: AnimalApiService
) : AnimalRemoteDataSource {

    override suspend fun getAbandonmentPublic(
        bgnde: String?,
        endde: String?,
        upkind: String?,
        kind: String?,
        uprCd: String?,
        orgCd: String?,
        careRegNo: String?,
        state: String?,
        neuterYn: String?,
        pageNo: Int,
        numOfRows: Int
    ): List<AnimalNetworkModel> =
        animalApiService.getAbandonmentPublic(
            bgnde, endde, upkind, kind, uprCd, orgCd, careRegNo, state, neuterYn, pageNo, numOfRows
        ).response.body?.items?.item ?: emptyList()

    override suspend fun getSido(): List<SidoNetworkModel> =
        animalApiService.getSido()
            .response.body?.items?.item ?: emptyList()

    override suspend fun getSigungu(uprCd: String): List<SigunguNetworkModel> =
        animalApiService.getSigungu(uprCd)
            .response.body?.items?.item ?: emptyList()

    override suspend fun getKind(upKindCd: String?): List<KindNetworkModel> =
        animalApiService.getKind(upKindCd)
            .response.body?.items?.item ?: emptyList()
}
