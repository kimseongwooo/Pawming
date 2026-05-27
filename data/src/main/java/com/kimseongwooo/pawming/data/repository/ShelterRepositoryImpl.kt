package com.kimseongwooo.pawming.data.repository

import com.kimseongwooo.pawming.data.datasource.ShelterRemoteDataSource
import com.kimseongwooo.pawming.data.mapper.toDomain
import com.kimseongwooo.pawming.data.mapper.toShelter
import com.kimseongwooo.pawming.domain.repository.ShelterRepository
import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.ShelterDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ShelterRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShelterRemoteDataSource
) : ShelterRepository {

    override suspend fun getShelters(uprCd: String?, orgCd: String?): Result<List<Shelter>> =
        runCatching {
            val basicList = remoteDataSource.getShelters(uprCd, orgCd).distinctBy { it.careRegNo }
            coroutineScope {
                basicList.map { basic ->
                    async {
                        runCatching { remoteDataSource.getShelterDetail(basic.careRegNo)?.toShelter() }
                            .getOrNull() ?: basic.toDomain()
                    }
                }.awaitAll()
            }
        }

    override suspend fun getShelterDetail(careRegNo: String): Result<ShelterDetail> =
        runCatching {
            remoteDataSource.getShelterDetail(careRegNo)
                ?.toDomain()
                ?: error("보호소 정보를 찾을 수 없습니다: $careRegNo")
        }
}
