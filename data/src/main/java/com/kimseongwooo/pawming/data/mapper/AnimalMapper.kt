package com.kimseongwooo.pawming.data.mapper

import com.kimseongwooo.pawming.model.Animal
import com.kimseongwooo.pawming.model.Kind
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import com.kimseongwooo.pawming.network.model.AnimalNetworkModel
import com.kimseongwooo.pawming.network.model.KindNetworkModel
import com.kimseongwooo.pawming.network.model.SidoNetworkModel
import com.kimseongwooo.pawming.network.model.SigunguNetworkModel

fun AnimalNetworkModel.toDomain(): Animal = Animal(
    desertionNo = desertionNo,
    happenDt = happenDt.orEmpty(),
    happenPlace = happenPlace.orEmpty(),
    kindCd = kindCd.orEmpty(),
    kindFullNm = kindFullNm.orEmpty(),
    kindNm = kindNm.orEmpty(),
    upKindCd = upKindCd.orEmpty(),
    upKindNm = upKindNm.orEmpty(),
    colorCd = colorCd.orEmpty(),
    age = age.orEmpty(),
    weight = weight.orEmpty(),
    sexCd = sexCd.orEmpty(),
    neuterYn = neuterYn.orEmpty(),
    rfidCd = rfidCd.orEmpty(),
    processState = processState.orEmpty(),
    endReason = endReason.orEmpty(),
    updTm = updTm.orEmpty(),
    images = listOfNotNull(popfile1, popfile2, popfile3, popfile4, popfile5, popfile6, popfile7, popfile8)
        .filter { it.isNotEmpty() },
    noticeNo = noticeNo.orEmpty(),
    noticeSdt = noticeSdt.orEmpty(),
    noticeEdt = noticeEdt.orEmpty(),
    specialMark = specialMark.orEmpty(),
    sfeSoci = sfeSoci.orEmpty(),
    sfeHealth = sfeHealth.orEmpty(),
    etcBigo = etcBigo.orEmpty(),
    vaccinationChk = vaccinationChk.orEmpty(),
    healthChk = healthChk.orEmpty(),
    careRegNo = careRegNo.orEmpty(),
    careNm = careNm.orEmpty(),
    careTel = careTel.orEmpty(),
    careAddr = careAddr.orEmpty(),
    careOwnerNm = careOwnerNm.orEmpty(),
    orgNm = orgNm.orEmpty(),
    adptnTitle = adptnTitle.orEmpty(),
    adptnSDate = adptnSDate.orEmpty(),
    adptnEDate = adptnEDate.orEmpty(),
    adptnConditionLimitTxt = adptnConditionLimitTxt.orEmpty(),
    adptnTxt = adptnTxt.orEmpty(),
    adptnImg = adptnImg.orEmpty(),
    sprtTitle = sprtTitle.orEmpty(),
    sprtSDate = sprtSDate.orEmpty(),
    sprtEDate = sprtEDate.orEmpty(),
    sprtConditionLimitTxt = sprtConditionLimitTxt.orEmpty(),
    sprtTxt = sprtTxt.orEmpty(),
    sprtImg = sprtImg.orEmpty(),
    srvcTitle = srvcTitle.orEmpty(),
    srvcSDate = srvcSDate.orEmpty(),
    srvcEDate = srvcEDate.orEmpty(),
    srvcConditionLimitTxt = srvcConditionLimitTxt.orEmpty(),
    srvcTxt = srvcTxt.orEmpty(),
    srvcImg = srvcImg.orEmpty(),
    evntTitle = evntTitle.orEmpty(),
    evntSDate = evntSDate.orEmpty(),
    evntEDate = evntEDate.orEmpty(),
    evntConditionLimitTxt = evntConditionLimitTxt.orEmpty(),
    evntTxt = evntTxt.orEmpty(),
    evntImg = evntImg.orEmpty()
)

fun SidoNetworkModel.toDomain(): Sido = Sido(
    orgCd = orgCd,
    orgdownNm = orgdownNm
)

fun SigunguNetworkModel.toDomain(): Sigungu = Sigungu(
    orgCd = orgCd,
    orgdownNm = orgdownNm,
    uprCd = uprCd
)

fun KindNetworkModel.toDomain(): Kind = Kind(
    kindCd = kindCd,
    kindNm = kindNm
)
