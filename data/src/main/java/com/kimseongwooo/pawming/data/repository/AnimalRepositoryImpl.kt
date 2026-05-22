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
            bgnde, endde, upkind, kind, uprCd, orgCd, careRegNo, state, neuterYn, pageNo, numOfRows
        ).map { it.toDomain() }
    }

    override suspend fun getSido(): Result<List<Sido>> = runCatching {
        remoteDataSource.getSido().map { it.toDomain() }
    }

    override suspend fun getSigungu(uprCd: String): Result<List<Sigungu>> = runCatching {
        remoteDataSource.getSigungu(uprCd).map { it.toDomain() }
    }

    override suspend fun getKind(upKindCd: String?): Result<List<Kind>> = runCatching {
        remoteDataSource.getKind(upKindCd).map { it.toDomain() }
    }
}
