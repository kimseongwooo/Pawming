package com.kimseongwooo.pawming.model

data class Animal(
    // 기본 정보
    val desertionNo: String,
    val happenDt: String,
    val happenPlace: String,
    val kindCd: String,
    val kindFullNm: String,
    val kindNm: String,
    val upKindCd: String,
    val upKindNm: String,
    val colorCd: String,
    val age: String,
    val weight: String,
    val sexCd: String,
    val neuterYn: String,
    val rfidCd: String,
    val processState: String,
    val endReason: String,
    val updTm: String,

    // 이미지 (popfile1~8 통합)
    val images: List<String>,

    // 공고 정보
    val noticeNo: String,
    val noticeSdt: String,
    val noticeEdt: String,

    // 특징 및 건강
    val specialMark: String,
    val sfeSoci: String,
    val sfeHealth: String,
    val etcBigo: String,
    val vaccinationChk: String,
    val healthChk: String,

    // 보호소 정보
    val careRegNo: String,
    val careNm: String,
    val careTel: String,
    val careAddr: String,
    val careOwnerNm: String,
    val orgNm: String,

    // 입양 절차
    val adptnTitle: String,
    val adptnSDate: String,
    val adptnEDate: String,
    val adptnConditionLimitTxt: String,
    val adptnTxt: String,
    val adptnImg: String,

    // 입양 지원
    val sprtTitle: String,
    val sprtSDate: String,
    val sprtEDate: String,
    val sprtConditionLimitTxt: String,
    val sprtTxt: String,
    val sprtImg: String,

    // 봉사 안내
    val srvcTitle: String,
    val srvcSDate: String,
    val srvcEDate: String,
    val srvcConditionLimitTxt: String,
    val srvcTxt: String,
    val srvcImg: String,

    // 행사 안내
    val evntTitle: String,
    val evntSDate: String,
    val evntEDate: String,
    val evntConditionLimitTxt: String,
    val evntTxt: String,
    val evntImg: String
)
