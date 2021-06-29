package com.kangaroo.wtcoin.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * @author shidawei
 * 创建日期：2021/6/18
 * 描述：
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class CoinNode (val preCode:String?,val id:String,val time:Long,val user: User,var next:CoinNode?):Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class CoinNodeModel(var username:String? = null,var extusername:String? = null,var lianchang:Int = 0,var lastTime:Long = 0,val coinNode:CoinNode):Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class CoinNodeCount (val ids:String,val count : Int,val user: User):Parcelable
