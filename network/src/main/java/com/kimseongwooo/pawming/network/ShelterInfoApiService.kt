package com.kimseongwooo.pawming.network

import com.kimseongwooo.pawming.network.model.ApiResponseNetworkModel
import com.kimseongwooo.pawming.network.model.ShelterInfoNetworkModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ShelterInfoApiService {

    @GET("shelterInfo_v2")
    suspend fun getShelterInfo(
        @Query("care_reg_no") careRegNo: String? = null,
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int = 1
    ): ApiResponseNetworkModel<ShelterInfoNetworkModel>
}