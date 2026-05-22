package com.kimseongwooo.pawming.network

import com.kimseongwooo.pawming.network.model.AnimalNetworkModel
import com.kimseongwooo.pawming.network.model.ApiResponseNetworkModel
import com.kimseongwooo.pawming.network.model.KindNetworkModel
import com.kimseongwooo.pawming.network.model.ShelterNetworkModel
import com.kimseongwooo.pawming.network.model.SidoNetworkModel
import com.kimseongwooo.pawming.network.model.SigunguNetworkModel
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalApiService {

    @GET("abandonmentPublic_v2")
    suspend fun getAbandonmentPublic(
        @Query("bgnde") bgnde: String? = null,
        @Query("endde") endde: String? = null,
        @Query("upkind") upkind: String? = null,
        @Query("kind") kind: String? = null,
        @Query("upr_cd") uprCd: String? = null,
        @Query("org_cd") orgCd: String? = null,
        @Query("care_reg_no") careRegNo: String? = null,
        @Query("state") state: String? = null,
        @Query("neuter_yn") neuterYn: String? = null,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 20
    ): ApiResponseNetworkModel<AnimalNetworkModel>

    @GET("sido_v2")
    suspend fun getSido(
        @Query("numOfRows") numOfRows: Int = 20
    ): ApiResponseNetworkModel<SidoNetworkModel>

    @GET("sigungu_v2")
    suspend fun getSigungu(
        @Query("upr_cd") uprCd: String,
        @Query("numOfRows") numOfRows: Int = 100
    ): ApiResponseNetworkModel<SigunguNetworkModel>

    @GET("shelter_v2")
    suspend fun getShelter(
        @Query("upr_cd") uprCd: String? = null,
        @Query("org_cd") orgCd: String? = null,
        @Query("numOfRows") numOfRows: Int = 100
    ): ApiResponseNetworkModel<ShelterNetworkModel>

    @GET("kind_v2")
    suspend fun getKind(
        @Query("up_kind_cd") upKindCd: String? = null,
        @Query("numOfRows") numOfRows: Int = 100
    ): ApiResponseNetworkModel<KindNetworkModel>
}
