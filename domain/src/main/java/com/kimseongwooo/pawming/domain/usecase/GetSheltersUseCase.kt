package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.ShelterRepository
import com.kimseongwooo.pawming.model.Shelter
import javax.inject.Inject

class GetSheltersUseCase @Inject constructor(
    private val shelterRepository: ShelterRepository
) {
    suspend operator fun invoke(uprCd: String? = null, orgCd: String? = null): Result<List<Shelter>> =
        shelterRepository.getShelters(uprCd, orgCd)
}
