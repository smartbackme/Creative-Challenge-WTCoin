package com.kangaroo.wtcoin.data.source

import com.kangaroo.miaosha.data.model.params.AlitimeParams
import com.kangaroo.miaosha.data.model.responses.AlitimeResponse
import com.kangraoo.basektlib.data.DataResult
import com.kangaroo.wtcoin.data.model.params.*
import com.kangaroo.wtcoin.data.model.responses.*
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * AppDataSource
 */
interface AppDataSource {


    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    suspend fun tokenPost( param: TokenPostParams): DataResult<TokenPostResponse>

    /**
     * 自动生成：by WaTaNaBe on 2021-06-10 15:14.
     * #alitime#
     * #阿里时间#
     */
    suspend fun alitime( param: AlitimeParams): DataResult<AlitimeResponse>
//#06#
}
