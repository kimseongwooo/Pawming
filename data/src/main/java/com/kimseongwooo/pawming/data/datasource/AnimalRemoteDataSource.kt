package com.kimseongwooo.pawming.data.datasource

import com.kimseongwooo.pawming.network.model.AnimalNetworkModel
import com.kimseongwooo.pawming.network.model.KindNetworkModel
import com.kimseongwooo.pawming.network.model.SidoNetworkModel
import com.kimseongwooo.pawming.network.model.SigunguNetworkModel

interface AnimalRemoteDataSource {
    suspend fun getAbandonmentPublic(
        bgnde: String?,
        endde: String?,
        upkind: String?,
        kind: String?,
        uprCd: String?,
        orgCd: String?,
        careRegNo: String?,
        state: String?,
        neuterYn: String?,
        desertionNo: String? = null,
        pageNo: Int,
        numOfRows: Int
    ): List<AnimalNetworkModel>

    suspend fun getSido(): List<SidoNetworkModel>

    suspend fun getSigungu(uprCd: String): List<SigunguNetworkModel>

    suspend fun getKind(upKindCd: String?): List<KindNetworkModel>
}
