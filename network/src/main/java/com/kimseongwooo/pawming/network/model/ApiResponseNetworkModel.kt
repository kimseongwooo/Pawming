package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseNetworkModel<T>(
    val response: ResponseBodyNetworkModel<T>
)

@Serializable
data class ResponseBodyNetworkModel<T>(
    val header: HeaderNetworkModel,
    val body: BodyNetworkModel<T>? = null
)

@Serializable
data class HeaderNetworkModel(
    val reqNo: String? = null,
    val resultCode: String,
    val resultMsg: String
)

@Serializable
data class BodyNetworkModel<T>(
    val items: ItemsNetworkModel<T>? = null,
    val numOfRows: Int = 0,
    val pageNo: Int = 0,
    val totalCount: Int = 0
)

@Serializable
data class ItemsNetworkModel<T>(
    val item: List<T> = emptyList()
)
