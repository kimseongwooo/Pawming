package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.AnimalRepository
import com.kimseongwooo.pawming.model.Animal
import javax.inject.Inject

class GetAbandonmentPublicUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(
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
    ): Result<List<Animal>> = animalRepository.getAbandonmentPublic(
        bgnde, endde, upkind, kind, uprCd, orgCd, careRegNo, state, neuterYn, pageNo, numOfRows
    )
}
