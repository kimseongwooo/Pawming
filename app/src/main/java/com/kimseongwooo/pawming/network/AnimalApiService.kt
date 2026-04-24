package com.kimseongwooo.pawming.network

import com.kimseongwooo.pawming.network.model.ApiResponseNetworkModel
import com.kimseongwooo.pawming.network.model.SidoNetworkModel
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalApiService {

    @GET("abandonmentPublicService_v2/sido_v2")
    suspend fun getSido(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("numOfRows") numOfRows: Int = 20,
        @Query("_type") type: String = "json"
    ): ApiResponseNetworkModel<SidoNetworkModel>
}
