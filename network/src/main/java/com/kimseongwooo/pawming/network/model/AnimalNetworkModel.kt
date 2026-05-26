package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimalNetworkModel(
    // 기본 정보
    val desertionNo: String,
    val happenDt: String? = null,
    val happenPlace: String? = null,
    val kindCd: String? = null,
    val kindFullNm: String? = null,
    val kindNm: String? = null,
    val upKindCd: String? = null,
    val upKindNm: String? = null,
    val colorCd: String? = null,
    val age: String? = null,
    val weight: String? = null,
    val sexCd: String? = null,
    val neuterYn: String? = null,
    val rfidCd: String? = null,
    val processState: String? = null,
    val endReason: String? = null,
    val updTm: String? = null,

    // 이미지 (최대 8장, 동물마다 개수 상이)
    val popfile1: String? = null,
    val popfile2: String? = null,
    val popfile3: String? = null,
    val popfile4: String? = null,
    val popfile5: String? = null,
    val popfile6: String? = null,
    val popfile7: String? = null,
    val popfile8: String? = null,

    // 공고 정보
    val noticeNo: String? = null,
    val noticeSdt: String? = null,
    val noticeEdt: String? = null,

    // 특징 및 건강
    val specialMark: String? = null,
    val sfeSoci: String? = null,
    val sfeHealth: String? = null,
    val etcBigo: String? = null,
    val vaccinationChk: String? = null,
    val healthChk: String? = null,

    // 보호소 정보
    val careRegNo: String? = null,
    val careNm: String? = null,
    val careTel: String? = null,
    val careAddr: String? = null,
    val careOwnerNm: String? = null,
    val orgNm: String? = null,

    // 입양 절차
    val adptnTitle: String? = null,
    val adptnSDate: String? = null,
    val adptnEDate: String? = null,
    val adptnConditionLimitTxt: String? = null,
    val adptnTxt: String? = null,
    val adptnImg: String? = null,

    // 입양 지원
    val sprtTitle: String? = null,
    val sprtSDate: String? = null,
    val sprtEDate: String? = null,
    val sprtConditionLimitTxt: String? = null,
    val sprtTxt: String? = null,
    val sprtImg: String? = null,

    // 봉사 안내
    val srvcTitle: String? = null,
    val srvcSDate: String? = null,
    val srvcEDate: String? = null,
    val srvcConditionLimitTxt: String? = null,
    val srvcTxt: String? = null,
    val srvcImg: String? = null,

    // 행사 안내
    val evntTitle: String? = null,
    val evntSDate: String? = null,
    val evntEDate: String? = null,
    val evntConditionLimitTxt: String? = null,
    val evntTxt: String? = null,
    val evntImg: String? = null,
)
