package com.kangaroo.wtcoin.ui.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.kangaroo.wtcoin.R
import com.kangaroo.wtcoin.data.model.UserModel
import com.kangaroo.wtcoin.tools.UStore
import com.kangaroo.wtcoin.ui.presenter.RegisterPresenter
import com.kangaroo.wtcoin.ui.view.RegisterView
import com.kangraoo.basektlib.tools.encryption.MessageDigestUtils
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.tools.task.TaskManager
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.ui.mvp.BMvpActivity
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.pass
import kotlinx.android.synthetic.main.activity_register.register
import kotlinx.android.synthetic.main.activity_register.user

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 11:03
 * #注册#
 */
class RegisterActivity : BMvpActivity<RegisterView, RegisterPresenter>(),RegisterView{

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, RegisterActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_register


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "注册"
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener() {})
        register.setOnClickListener {
            showProgressingDialog()

            if((!TextUtils.isEmpty(user.text?.toString()?.trim()))||(!TextUtils.isEmpty(
                    pass.text?.toString()?.trim()
                ))){
                TaskManager.taskExecutor.execute(Runnable {
                    try {
                        EMClient.getInstance().createAccount(
                            user.text.toString().trim(), MessageDigestUtils.sha1(
                                pass.text.toString().trim()
                            )
                        )
                        dismissProgressDialog()

                        EMClient.getInstance().login(user.text.toString().trim(), MessageDigestUtils.sha1(pass.text.toString().trim()), object : EMCallBack {
                            //回调
                            override fun onSuccess() {

                                UStore.putUser(UserModel(user.text.toString().trim(),pass.text.toString().trim()))
                                MainActivity.startFrom(visitActivity())
//                        EMClient.getInstance().groupManager().loadAllGroups()
//                        EMClient.getInstance().chatManager().loadAllConversations()
                            }

                            override fun onProgress(progress: Int, status: String) {
                            }
                            override fun onError(code: Int, message: String) {
                                showToastMsg(Tip.Error,"用户名密码错误")
                            }
                        })
                    } catch (e: HyphenateException){
                        dismissProgressDialog()
                        showToastMsg(Tip.Error, "注册失败${e.description}")
                    }
                })
            }else{
                dismissProgressDialog()
                showToastMsg(Tip.Error, "用户名密码不能为空")
            }
        }
    }

    override fun createPresenterInstance(): RegisterPresenter {
        return RegisterPresenter()
    }

}
