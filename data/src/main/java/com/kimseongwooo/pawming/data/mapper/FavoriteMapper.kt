package com.kimseongwooo.pawming.data.mapper

import com.kimseongwooo.pawming.data.db.FavoriteAnimalEntity
import com.kimseongwooo.pawming.model.FavoriteAnimal

fun FavoriteAnimalEntity.toDomain() = FavoriteAnimal(
    desertionNo = desertionNo,
    kindNm = kindNm,
    sexCd = sexCd,
    age = age,
    happenPlace = happenPlace,
    processState = processState,
    imageUrl = imageUrl,
    savedAt = savedAt
)

fun FavoriteAnimal.toEntity() = FavoriteAnimalEntity(
    desertionNo = desertionNo,
    kindNm = kindNm,
    sexCd = sexCd,
    age = age,
    happenPlace = happenPlace,
    processState = processState,
    imageUrl = imageUrl,
    savedAt = savedAt
)
