package com.kangaroo.wtcoin.ui.activity

import android.R.attr.password
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.kangaroo.wtcoin.R
import com.kangaroo.wtcoin.data.model.UserModel
import com.kangaroo.wtcoin.tools.UStore
import com.kangaroo.wtcoin.ui.presenter.LoginPresenter
import com.kangaroo.wtcoin.ui.view.LoginView
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.tools.encryption.MessageDigestUtils
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.ui.mvp.BMvpActivity
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import kotlinx.android.synthetic.main.activity_login.*


/**
 * 自动生成：by WaTaNaBe on 2021-06-21 11:03
 * #登录#
 */
class LoginActivity : BMvpActivity<LoginView, LoginPresenter>(),LoginView{

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, LoginActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_login


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "登录"
        libToolBarOptions.setNeedNavigate(false)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener() {})
        UFont.setTextViewFont(icon, R.string.lib_icon_github)

        login.setOnClickListener {
            showProgressingDialog()

            if((!TextUtils.isEmpty(user.text?.toString()?.trim()))||(!TextUtils.isEmpty(pass.text?.toString()?.trim()))){
                EMClient.getInstance().login(user.text.toString().trim(), MessageDigestUtils.sha1(pass.text.toString().trim()), object : EMCallBack {
                    //回调
                    override fun onSuccess() {
                        dismissProgressDialog()

                        UStore.putUser(UserModel(user.text.toString().trim(),pass.text.toString().trim()))
                        MainActivity.startFrom(visitActivity())
                        finish()
//                        EMClient.getInstance().groupManager().loadAllGroups()
//                        EMClient.getInstance().chatManager().loadAllConversations()
                    }

                    override fun onProgress(progress: Int, status: String) {
                    }
                    override fun onError(code: Int, message: String) {
                        dismissProgressDialog()
                        showToastMsg(Tip.Error,"用户名密码错误")
                    }
                })
            }else{
                dismissProgressDialog()
                showToastMsg(Tip.Error,"用户名密码不能为空")
            }
        }

        register.setOnClickListener {
            RegisterActivity.startFrom(visitActivity())
        }
    }

    override fun createPresenterInstance(): LoginPresenter {
        return LoginPresenter()
    }

}
