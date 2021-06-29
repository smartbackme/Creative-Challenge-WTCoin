package com.kangaroo.wtcoin.data.source.remote

import com.kangaroo.miaosha.data.model.params.AlitimeParams
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.model.toNetMap
import com.kangraoo.basektlib.data.source.remote.BaseRemoteDataSource
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.kangraoo.basektlib.exception.LibNetWorkException
import com.kangaroo.wtcoin.data.source.AppService
import com.kangaroo.wtcoin.data.source.AppDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.kangaroo.wtcoin.data.model.params.*
import com.kangaroo.wtcoin.data.model.responses.*
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult
import com.qdedu.baselibcommon.data.netSuccess
import com.kangraoo.basektlib.data.netError

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * AppRemoteDataSource
 */
 class AppRemoteDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
 ) : BaseRemoteDataSource(), AppDataSource {



    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    override suspend fun tokenPost(param: TokenPostParams)= withContext(ioDispatcher) {
        try {
            val data = AppService.getApiService("http://a1.easemob.com/").tokenPostAsync(param)
            if(data.isSuccessful) {
                return@withContext DataResult.Success(data.body()!!).netSuccess()
            }else{
                return@withContext DataResult.Error(LibNetWorkException(data.code(),data.message())).netError()
            }
        } catch (e: Exception) {
            return@withContext DataResult.Error(e).netError()
        }
    }

    /**
     * 自动生成：by WaTaNaBe on 2021-06-10 15:14.
     * #alitime#
     * #阿里时间#
     */
    override suspend fun alitime(param: AlitimeParams)= withContext(ioDispatcher) {
        try {
            val data = AppService.getApiFromService("http://api.m.taobao.com/").alitimeAsync(param.toNetMap())
            if(data.isSuccessful) {
                var tx = data.raw().receivedResponseAtMillis
                var rx = data.raw().sentRequestAtMillis
                var time = rx - tx
                return@withContext DataResult.Success(data.body()!!,time).netSuccess()
            }else{
                return@withContext DataResult.Error(LibNetWorkException(data.code(),data.message())).netError()
            }
        } catch (e: Exception) {
            return@withContext DataResult.Error(e).netError()
        }
    }
//#06#
 }
