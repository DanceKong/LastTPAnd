package com.example.endpwebapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.os.Process
import android.view.KeyEvent
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.endpwebapp.R
import java.net.URISyntaxException


class BrowserActivity : Activity() {
    private val TAG = "ITouch_WebActivity"
    private var mWeb: WebView? = null
    private val mBackPressed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkRestart(savedInstanceState)) {
            //overridePendingTransition(R.anim.anim_right_in, R.anim.anim_stay);
            initLayout()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWeb!!.canGoBack()) {
            mWeb!!.goBack()
            return true
        }

        //Back(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val d = AlertDialog.Builder(this)
            d.setTitle("프로그램을 종료 하시겠습니까?")
            d.setMessage("'카페' \n http://192.168.100.57:8090 으로 컴퓨터에서 접속하면 편리하게 보실 수 있습니다.  \n\n종료하시려면 '예' 버튼을 눌러주세요.")
//            d.setIcon(R.drawable.ic_launcher)
            d.setPositiveButton(
                "아니요"
            ) { dialog, which -> // TODO Auto-generated method stub
                //onStop();
                dialog.cancel()
            }

            d.setNegativeButton("예") { dialog, which ->
                // TODO Auto-generated method stub
                Process.killProcess(Process.myPid())

                //finish();
            }
            d.show()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun checkRestart(savedInstanceState: Bundle?): Boolean {
        // TODO Auto-generated method stub
        return if (savedInstanceState != null) {
            true
        } else false
    }

    override fun onBackPressed() {
        if (mWeb!!.canGoBack()) {
            mWeb!!.goBack()
        } else {
            super.onBackPressed()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
    }
    class HttpUrl {
        val WEBURL = "http://192.168.100.57:8090"
    }

    private fun initLayout() {
        setContentView(R.layout.activity_browser)
        title = getString(R.string.title_web_info)
        mWeb = findViewById<View>(com.example.endpwebapp.R.id.webInfo) as WebView
        mWeb!!.webViewClient = WebClient()
        val set = mWeb!!.settings
        set.javaScriptEnabled = true
        mWeb!!.settings.domStorageEnabled = true
        //set.setCacheMode(WebSettings.LOAD_NO_CACHE);
        var url: String? = null
        val httpUrl = HttpUrl()
        url = String.format(httpUrl.WEBURL)
        mWeb!!.loadUrl(url)
    }

    internal inner class WebClient : WebViewClient() {
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            val builder = AlertDialog.Builder(this@BrowserActivity)
            var message = getString(R.string.msg_cert_error)
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> message = getString(R.string.msg_cert_trust_error)
                SslError.SSL_EXPIRED -> message = getString(R.string.msg_cert_expired_error)
                SslError.SSL_IDMISMATCH -> message = getString(R.string.msg_cert_mismatched_error)
                SslError.SSL_NOTYETVALID -> message = getString(R.string.msg_cert_not_yet_error)
            }
            handler.proceed()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("intent:") || url.startsWith("market:") || url.startsWith("ispmobile:")) {
                var i: Intent
                i = try {
                    Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                    return false
                }
                if (packageManager.resolveActivity(i, 0) == null) {
                    val pkgName = i.getPackage()
                    return if (pkgName != null) {
                        i = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://search?q=pname:$pkgName")
                        )
                        i.addCategory(Intent.CATEGORY_BROWSABLE)
                        startActivity(i)
                        true
                    } else false
                }
                try {
                    startActivity(i)
                } catch (e: Exception) {
                }
            } else {
                view.loadUrl(url)
                return true
            }
            return true
        }
    }



    companion object {
        //private ImageButton mVoiceBtn;;
        private const val REQUEST_VOICE = 4
    }
}