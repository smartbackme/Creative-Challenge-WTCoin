package com.kangaroo.wtcoin.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.kangaroo.miaosha.data.model.params.AlitimeParams
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangaroo.wtcoin.R;
import com.kangaroo.wtcoin.app.LIAN_LEVEL1
import com.kangaroo.wtcoin.app.LIAN_LEVEL2
import com.kangaroo.wtcoin.app.LIAN_LEVEL3
import com.kangaroo.wtcoin.app.LIAN_LEVEL4
import com.kangaroo.wtcoin.data.model.CoinNode
import com.kangaroo.wtcoin.data.model.CoinNodeModel
import com.kangaroo.wtcoin.data.model.User
import com.kangaroo.wtcoin.data.source.AppRepository
import com.kangaroo.wtcoin.tools.MqttUtil
import com.kangaroo.wtcoin.tools.UStore
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.succeeded
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.ui.BFragment
import kotlinx.android.synthetic.main.fragment_main_loc.*
import kotlinx.coroutines.launch

/**
 * 自动生成：by WaTaNaBe on 2021-06-22 09:12
 * #主页#
 */
class MainLocFragment : BFragment(){

    companion object{

        @JvmStatic
        fun newInstance() = MainLocFragment()
        
    }

    override fun getLayoutId() = R.layout.fragment_main_loc

    private var mHandler: Handler? = null
    var at :Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

        mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    1 ->{
                        sendEmptyMessageDelayed(1, 1000)
                        //每秒来获取当前活跃用户
                        userData.setText("活跃用户数量："+UStore.getUserListFromQt()?.user?.size)
                        lianData.setText("区块链长："+UStore.length)
                        if(UStore.length==0){
                            button.setText("构建创始块")
                        }else{
//                            说明：总空气币数量为200枚，一共有4个算力节点50，100，150，200 \n
  //                          点击次数分别为1，3，5，7\n

                            when(UStore.length){
                                in 0 until 50 ->{
                                    if(UStore.length==0){
                                        button.setText("构建创始币")
                                    }else{
                                        button.setText("需要点击${LIAN_LEVEL1}次构建币")
                                    }
                                }
                                in 50 until 100 ->{
                                    button.setText("需要点击${LIAN_LEVEL2}次构建币")

                                }
                                in 100 until  150 ->{
                                    button.setText("需要点击${LIAN_LEVEL3}次构建币")

                                }
                                in 150 until  200->{
                                    button.setText("需要点击${LIAN_LEVEL4}次构建币")

                                }
                                else ->{
                                    button.setText("所有的币都被抢完了")
                                    button.isEnabled = false
                                }
                            }
                        }
                    }
                }
            }
        }
        mHandler?.sendEmptyMessageDelayed(1, 1000)


        button.setOnClickListener {
            MqttUtil.threadExecutor.execute(Runnable {
                if(UStore.getUser()!=null){
                    if(UStore.length>0){
                        when(UStore.length){
                            in 0 until 50 ->{
                                i++
                                if(i>=LIAN_LEVEL1){
                                    i=0
                                    UStore.putOne(UStore.lastNode,UStore.length,UStore.node)
                                }else{
                                    showToastMsg(Tip.Warning,"还有${LIAN_LEVEL1-i}次")
                                }

                            }
                            in 50 until 100 ->{
                                i++
                                if(i>=LIAN_LEVEL2){
                                    i=0
                                    UStore.putOne(UStore.lastNode,UStore.length,UStore.node)
                                }else{
                                    showToastMsg(Tip.Warning,"还有${LIAN_LEVEL2-i}次")
                                }
                            }
                            in 100 until  150 ->{
                                i++
                                if(i>= LIAN_LEVEL3){
                                    i=0
                                    UStore.putOne(UStore.lastNode,UStore.length,UStore.node)
                                }else{
                                    showToastMsg(Tip.Warning,"还有${LIAN_LEVEL3-i}次")
                                }
                            }
                            in 150 until  200->{
                                i++
                                if(i>= LIAN_LEVEL4){
                                    i=0
                                    UStore.putOne(UStore.lastNode,UStore.length,UStore.node)
                                }else{
                                    showToastMsg(Tip.Warning,"还有${LIAN_LEVEL4-i}次")
                                }
                            }
                            else ->{
                                showToastMsg(Tip.Error,"无法再添加区块币，链已满")
                            }
                        }

                    }else {
                        UStore.putOne(null)
                    }
                }
            })



        }


    }

    var i = 0

    override fun onDestroyView() {
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
        super.onDestroyView()
    }

}
