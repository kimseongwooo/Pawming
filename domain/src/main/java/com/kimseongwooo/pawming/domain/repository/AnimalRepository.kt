package com.kimseongwooo.pawming.domain.repository

import com.kimseongwooo.pawming.model.Animal
import com.kimseongwooo.pawming.model.Kind
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu

interface AnimalRepository {
    suspend fun getAbandonmentPublic(
        bgnde: String? = null,
        endde: String? = null,
        upkind: String? = null,
        kind: String? = null,
        uprCd: String? = null,
        orgCd: String? = null,
        careRegNo: String? = null,
        state: String? = null,
        neuterYn: String? = null,
        pageNo: Int = 1,
        numOfRows: Int = 20
    ): Result<List<Animal>>

    suspend fun getSido(): Result<List<Sido>>

    suspend fun getSigungu(uprCd: String): Result<List<Sigungu>>

    suspend fun getKind(upKindCd: String? = null): Result<List<Kind>>
}
