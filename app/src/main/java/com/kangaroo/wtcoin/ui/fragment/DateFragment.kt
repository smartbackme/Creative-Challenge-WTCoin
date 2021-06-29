package com.kangaroo.wtcoin.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SimpleAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangaroo.wtcoin.R
import com.kangaroo.wtcoin.app.LIAN_LEVEL1
import com.kangaroo.wtcoin.app.LIAN_LEVEL2
import com.kangaroo.wtcoin.app.LIAN_LEVEL3
import com.kangaroo.wtcoin.app.LIAN_LEVEL4
import com.kangaroo.wtcoin.data.model.CoinNode
import com.kangaroo.wtcoin.data.model.CoinNodeCount
import com.kangaroo.wtcoin.data.model.User
import com.kangaroo.wtcoin.tools.UStore
import com.kangaroo.wtcoin.ui.adapter.DataAdapter
import com.kangaroo.wtcoin.ui.adapter.UserAdapter
import com.kangaroo.wtcoin.ui.view.DateView
import com.kangaroo.wtcoin.ui.presenter.DatePresenter
import com.kangraoo.basektlib.data.model.NULLTYPE
import com.kangraoo.basektlib.tools.log.ULog
import kotlinx.android.synthetic.main.fragment_date.*
import com.kangraoo.basektlib.ui.mvp.BMvpFragment
import kotlinx.android.synthetic.main.fragment_main_loc.*
import java.lang.StringBuilder

/**
 * 自动生成：by WaTaNaBe on 2021-06-22 15:12
 * #数据展示#
 */
class DateFragment : BMvpFragment<DateView ,DatePresenter>(),DateView{

    companion object{

        @JvmStatic
        fun newInstance() = DateFragment()
        
    }
    private var mHandler: Handler? = null

    override fun getLayoutId() = R.layout.fragment_date
    var adapter : DataAdapter? = null
    var adapter2 : UserAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DataAdapter()
        adapter2 = UserAdapter()
        users.layoutManager = LinearLayoutManager(visitActivity())
        users.adapter = adapter2
        data.layoutManager = LinearLayoutManager(visitActivity())
        data.adapter = adapter
        mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    1 ->{
                        sendEmptyMessageDelayed(1, 1000)
                        var set = UStore.getUserList()?.user
                        var list = arrayListOf<User>()
                        set?.forEach {
                            list.add(it)
                        }
                        adapter2?.setNewInstance(list)
                        var map = hashMapOf<String,MutableList<CoinNode>>()
                        var temp: CoinNode? = UStore.node ?: return
                        while (temp!=null){
                            if(map[temp.user.name]==null){
                                map[temp.user.name] = arrayListOf()
                            }
                            map[temp.user.name]!!.add(temp)
                            temp = temp.next
                        }
                        var list2 = arrayListOf<CoinNodeCount>()
                        map.forEach { t, u ->
                            var s = StringBuilder()
                            u.forEach {
                                s.append(it.id+";")
                            }
                            list2.add(CoinNodeCount(s.toString(),u.size,User(t)))
                        }
                        adapter?.setNewInstance(list2)
                    }
                }
            }
        }
        mHandler?.sendEmptyMessageDelayed(1, 1000)
    }

    override fun onDestroyView() {
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
        super.onDestroyView()
    }
    override fun createPresenterInstance(): DatePresenter {
        return DatePresenter()
    }
}
