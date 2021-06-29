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
data class User(val name:String):Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class UserModel(val name:String,val pass:String,var token:String? = null):Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class UserList(var username:String? = null,var extusername:String? = null,val user:Set<User>):Parcelable

