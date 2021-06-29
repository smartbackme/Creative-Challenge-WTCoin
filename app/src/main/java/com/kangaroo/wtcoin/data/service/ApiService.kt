package com.kangaroo.wtcoin.data.service

import com.kangaroo.miaosha.data.model.responses.AlitimeResponse
import com.kangaroo.wtcoin.data.model.responses.*
import com.kangaroo.wtcoin.data.model.params.*
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {



    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    @POST(ApiMethods.tokenPost)
    suspend fun tokenPostAsync(@Body params:TokenPostParams): Response<TokenPostResponse>


    /**
     * 自动生成：by WaTaNaBe on 2021-06-10 15:14.
     * #alitime#
     * #阿里时间#
     */
    @Headers(
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.4098.3 Safari/537.36"
    )
    @GET(ApiMethods.alitime)
    suspend fun alitimeAsync(@QueryMap params:Map<String,String>): Response<AlitimeResponse>
//#06#
}
