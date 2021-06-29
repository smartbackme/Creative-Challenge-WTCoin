package com.kangaroo.wtcoin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.kangaroo.wtcoin.R
import com.kangaroo.wtcoin.data.model.User
import com.kangaroo.wtcoin.data.model.params.TokenPostParams
import com.kangaroo.wtcoin.data.source.AppRepository
import com.kangaroo.wtcoin.tools.MqttUtil
import com.kangaroo.wtcoin.tools.UStore
import com.kangaroo.wtcoin.ui.fragment.DateFragment
import com.kangaroo.wtcoin.ui.fragment.MainLocFragment
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.succeeded
import com.kangraoo.basektlib.tools.UFragment
import com.kangraoo.basektlib.tools.encryption.MessageDigestUtils
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.ui.BActivity
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 自动生成：by WaTaNaBe on 2021-06-18 14:19
 * #首页#
 */
class MainActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            val intent: Intent = Intent(
                activity,
                MainActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            LibActivityLauncher.instance
                .launch(activity, intent)
        }

    }

    override fun getLayoutId() = R.layout.activity_main


    private var first = true
    override fun onViewCreated(savedInstanceState: Bundle?) {
        switchDefaultFragment(savedInstanceState)
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "首页"
        libToolBarOptions.setNeedNavigate(false)
        libToolBarOptions.setRightText(R.string.app_logout)
        libToolBarOptions.rightOption?.isText1 =true
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener() {

            override fun onRight(view: View) {
                super.onRight(view)
                showProgressingDialog()
                EMClient.getInstance().logout(true, object : EMCallBack {
                    override fun onSuccess() {
                        dismissProgressDialog()
                        UStore.clearUser()
                        ActivityLifeManager.finishAllActivity()
                        var intent = Intent(SApplication.context(),LoginActivity::class.java)
                        intent.flags = (android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        SApplication.context().startActivity(intent)
                    }

                    override fun onProgress(progress: Int, status: String) {
                    }

                    override fun onError(code: Int, message: String) {
                        dismissProgressDialog()
                    }
                })
            }
        })
        bottombar.setTextSize(12f)
        bottombar.enableAnimation(false)
        bottombar.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        bottombar.isItemHorizontalTranslationEnabled = false
        bottombar.selectedItemId = bottombar.selectedItemId
        bottombar.setOnNavigationItemSelectedListener {

            when (it.title) {
                getString(R.string.app_main) -> {
                    switchFragment(mFragment, mainFragment)
                }
                getString(R.string.app_data) -> {
                    switchFragment(mFragment, dateFragment)
                }

            }
            selectedItemId = it.itemId
            true
        }
        launch {
            val user = UStore.getUser()
            showProgressingDialog("拉取区块链数据")
            var data = AppRepository.instance.tokenPost(
                TokenPostParams(
                    username = user!!.name, password = MessageDigestUtils.sha1(
                        user!!.pass
                    )
                )
            )
            if(data.succeeded){
                if (data is DataResult.Success) {
                    user.token = data.data.access_token
                    UStore.putUser(user)
                    withContext(Dispatchers.IO){
                        MqttUtil.mqttService()
                        MqttUtil.message(MqttUtil.lacn, user.name)
                        MqttUtil.message(MqttUtil.laun, user.name)

                        postDelayed(Runnable {
                            dismissProgressDialog()
                            var userQt = UStore.getUserListFromQt()
                            UStore.bianliLian()
                            if (!userQt.user.contains(User(user.name))) {
                                (userQt.user as HashSet<User>).add(User(user.name))
                                UStore.putUserList(userQt)
                                MqttUtil.message(
                                    MqttUtil.cn, HJson.toJson(
                                        UStore.getUserList()!!.apply {
                                            extusername = user.name
                                        })
                                )
                                ULog.d(
                                    "第一次登录发现数据中心没有自己的数据发送新数据出去",
                                    HJson.toJson(UStore.getUserList())
                                )
                            } else if (first) {
                                first = false
                                MqttUtil.message(
                                    MqttUtil.cn, HJson.toJson(
                                        UStore.getUserList()!!.apply {
                                            extusername = user.name
                                        })
                                )
                                ULog.d("第一次登录发送新数据出去", HJson.toJson(UStore.getUserList()))

                            }
                        }, 10000)
                    }
                } else {
                    dismissProgressDialog()
                    showToastMsg(Tip.Error, "拉取失败")
                }
            }else{
                dismissProgressDialog()
                showToastMsg(Tip.Error, "拉取失败")
            }

        }



    }
    private var mainFragment: Fragment =
        MainLocFragment()

    private var dateFragment: Fragment =
        DateFragment()

    override fun onDestroy() {
        super.onDestroy()
        MqttUtil.unsubscribe()
    }
    private fun switchDefaultFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val fragments = supportFragmentManager.fragments
            fragments.forEach { fragment ->
                when (fragment) {
                    is MainLocFragment -> {
                        mainFragment = fragment
                        if (!fragment.isHidden) {
                            switchFragment(mFragment, mainFragment)
                        }
                    }
                    is DateFragment -> {
                        dateFragment = fragment
                        if (!fragment.isHidden) {
                            switchFragment(mFragment, dateFragment)
                        }
                    }

                }
            }
        } else {
            switchFragment(mFragment, mainFragment)
        }
    }

    var mFragment: Fragment? = null
    var selectedItemId: Int = R.id.navigation_home

    private fun <T : Fragment> switchFragment(
        from: T?,
        to: T
    ) {
        var switchFragment =
            UFragment.switchFragment(supportFragmentManager, R.id.fl_main, from, to)
        mFragment = switchFragment

    }
}
