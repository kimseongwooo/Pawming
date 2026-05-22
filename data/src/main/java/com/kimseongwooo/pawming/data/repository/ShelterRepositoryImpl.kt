package com.kimseongwooo.pawming.data.repository

import com.kimseongwooo.pawming.data.datasource.ShelterRemoteDataSource
import com.kimseongwooo.pawming.data.mapper.toDomain
import com.kimseongwooo.pawming.domain.repository.ShelterRepository
import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.ShelterDetail
import javax.inject.Inject

class ShelterRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShelterRemoteDataSource
) : ShelterRepository {

    override suspend fun getShelters(uprCd: String?, orgCd: String?): Result<List<Shelter>> =
        runCatching {
            remoteDataSource.getShelters(uprCd, orgCd).map { it.toDomain() }
        }

    override suspend fun getShelterDetail(careRegNo: String): Result<ShelterDetail> =
        runCatching {
            remoteDataSource.getShelterDetail(careRegNo)
                ?.toDomain()
                ?: error("보호소 정보를 찾을 수 없습니다: $careRegNo")
        }
}
