package com.kangaroo.wtcoin.data.source

import com.kangaroo.miaosha.data.model.params.AlitimeParams
import com.kangraoo.basektlib.data.source.BaseRepository
import com.kangaroo.wtcoin.data.source.local.AppLocalDataSource
import com.kangaroo.wtcoin.data.source.remote.AppRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.kangaroo.wtcoin.data.model.params.*
import com.kangaroo.wtcoin.data.model.responses.*

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * AppRepository
 */
class AppRepository : BaseRepository<AppLocalDataSource,AppRemoteDataSource>(AppLocalDataSource(),AppRemoteDataSource()),AppDataSource {

    companion object{
        val instance: AppRepository by lazy {
            AppRepository()
        }
    }
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    override suspend fun tokenPost( param: TokenPostParams) = remoteDataSource.tokenPost(param)

    /**
     * 自动生成：by WaTaNaBe on 2021-06-10 15:14.
     * #alitime#
     * #阿里时间#
     */
    override suspend fun alitime( param: AlitimeParams) = remoteDataSource.alitime(param)
//#06#
}
