package com.kimseongwooo.pawming.data.repository

import com.kimseongwooo.pawming.data.datasource.AnimalRemoteDataSource
import com.kimseongwooo.pawming.data.mapper.toDomain
import com.kimseongwooo.pawming.domain.repository.AnimalRepository
import com.kimseongwooo.pawming.model.Animal
import com.kimseongwooo.pawming.model.Kind
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import javax.inject.Inject

class AnimalRepositoryImpl @Inject constructor(
    private val remoteDataSource: AnimalRemoteDataSource
) : AnimalRepository {

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
    ): Result<List<Animal>> = runCatching {
        remoteDataSource.getAbandonmentPublic(
            bgnde = bgnde, endde = endde, upkind = upkind, kind = kind,
            uprCd = uprCd, orgCd = orgCd, careRegNo = careRegNo, state = state,
            neuterYn = neuterYn, desertionNo = null, pageNo = pageNo, numOfRows = numOfRows
        ).map { it.toDomain() }
    }

    override suspend fun getSido(): Result<List<Sido>> = runCatching {
        remoteDataSource.getSido().map { it.toDomain() }
    }

    override suspend fun getSigungu(uprCd: String): Result<List<Sigungu>> = runCatching {
        remoteDataSource.getSigungu(uprCd).map { it.toDomain() }
    }

    override suspend fun getAnimalByDesertionNo(desertionNo: String): Result<Animal?> = runCatching {
        remoteDataSource.getAbandonmentPublic(
            bgnde = null, endde = null, upkind = null, kind = null,
            uprCd = null, orgCd = null, careRegNo = null, state = null, neuterYn = null,
            desertionNo = desertionNo, pageNo = 1, numOfRows = 1
        ).firstOrNull()?.toDomain()
    }

    override suspend fun getKind(upKindCd: String?): Result<List<Kind>> = runCatching {
        remoteDataSource.getKind(upKindCd).map { it.toDomain() }
    }
}
