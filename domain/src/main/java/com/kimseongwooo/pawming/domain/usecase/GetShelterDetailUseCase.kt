package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.ShelterRepository
import com.kimseongwooo.pawming.model.ShelterDetail
import javax.inject.Inject

class GetShelterDetailUseCase @Inject constructor(
    private val shelterRepository: ShelterRepository
) {
    suspend operator fun invoke(careRegNo: String): Result<ShelterDetail> =
        shelterRepository.getShelterDetail(careRegNo)
}
