package com.kangaroo.wtcoin.app

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.AudioFormat
import android.os.Environment
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.kangaroo.wtcoin.R
import com.kangaroo.wtcoin.tools.UStore
import com.kangaroo.wtcoin.ui.activity.LoginActivity
import com.kangaroo.wtcoin.ui.activity.MainActivity
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.tools.HNotification
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.net.OkHttpDns
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import com.kangraoo.basektlib.widget.dialog.LibDebugModeDialog
import com.qdedu.baselibcommon.app.init.BaseAppInit
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.bridge.BaseJsApi
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.qdedu.baselibcommon.data.ShareEntity
import com.qdedu.baselibcommon.tools.UUmeng
import com.qdedu.baselibcommon.ui.activity.WebPageActivity
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class AppInit(init: IInit) : BaseAppInit(init) {

    override fun preMainAppHuanjinInit() {
                AppHuanJingFactory.dev.apply {

        }
        AppHuanJingFactory.online.apply {

        }
        AppHuanJingFactory.test.apply {

        }
        AppHuanJingFactory.uat.apply {

        }
        LibDebugModeDialog.register(AppHuanJingFactory.huanJingSelectList)

    }

    override fun afterMainInit() {
        UUmeng.profileSignIn("demouser")
        ServiceProvider.buglyService?.putUserData("demouser")
        OkHttpDns.instance.preResolveHosts(arrayListOf("v.juhe.cn"))
        WebPageActivity.addJsObject("", BaseJsApi())
        // ???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.transparent, R.color.color_999999) // ????????????????????????
            ClassicsHeader(context) // .setTimeFormat(new DynamicTimeFormat("????????? %s"));//???????????????Header???????????? ???????????????Header
        }
        // ???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> // ???????????????Footer???????????? BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
        HNotification.setNotificationChannel()


        var options = EMOptions();
        options.acceptInvitationAlways = false;
        options.autoTransferMessageAttachments = true;
        options.setAutoDownloadThumbnail(true);
        EMClient.getInstance().init(SApplication.context(), options);
        EMClient.getInstance().setDebugMode(true);
        EMClient.getInstance().addConnectionListener(object : EMConnectionListener{
            override fun onConnected() {
            }

            override fun onDisconnected(error: Int) {
                if (error == EMError.USER_REMOVED) {
                    TipToast.tip(Tip.Error,"???????????????")
                    out()
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    TipToast.tip(Tip.Error,"???????????????????????????")
                    out()
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    TipToast.tip(Tip.Error,"?????????????????????")
                    out()
                } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
                    TipToast.tip(Tip.Error,"???????????????????????????")
                    out()
                } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                    TipToast.tip(Tip.Error,"???????????????????????????")
                    out()
                }

            }

        })
    }


    fun out(){
        UStore.clearUser()
        ActivityLifeManager.finishAllActivity()
        var intent = Intent(SApplication.context(),LoginActivity::class.java)
        intent.flags = (android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        SApplication.context().startActivity(intent)
    }

    override fun afterThreadInit() {
        super.afterThreadInit()
    }
}
