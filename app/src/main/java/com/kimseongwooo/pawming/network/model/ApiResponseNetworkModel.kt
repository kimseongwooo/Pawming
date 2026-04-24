package com.kimseongwooo.pawming.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseNetworkModel<T>(
    val response: ResponseBodyNetworkModel<T>
)

@Serializable
data class ResponseBodyNetworkModel<T>(
    val header: HeaderNetworkModel,
    val body: BodyNetworkModel<T>?
)

@Serializable
data class HeaderNetworkModel(
    val reqNo: String?,
    val resultCode: String,
    val resultMsg: String
)

@Serializable
data class BodyNetworkModel<T>(
    val items: ItemsNetworkModel<T>?,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

@Serializable
data class ItemsNetworkModel<T>(
    val item: List<T>
)
