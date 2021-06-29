package com.kangaroo.miaosha.data.model.responses

import com.squareup.moshi.JsonClass

/**
 * 自动生成：by WaTaNaBe on 2021-06-10 15:14.
 * alitime
 */
@JsonClass(generateAdapter = true)
data class AlitimeResponse(
    val api: String,
    val `data`: Data,
    val ret: List<String>,
    val v: String
)

@JsonClass(generateAdapter = true)
data class Data(
    val t: Long
)