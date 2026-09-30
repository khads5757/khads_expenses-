package com.family.moneytracker

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : Activity() {
    private lateinit var web: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this)
        setContentView(web)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.addJavascriptInterface(Bridge(), "Android")
        web.webViewClient = WebViewClient()
        web.loadUrl("file:///android_asset/index.html")
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_SMS), 1)
        }
    }

    override fun onRequestPermissionsResult(code: Int, perms: Array<out String>, res: IntArray) {
        super.onRequestPermissionsResult(code, perms, res)
        web.evaluateJavascript("if(window.scan)scan(14)", null)
    }

    override fun onResume() {
        super.onResume()
        if (::web.isInitialized) web.evaluateJavascript("if(window.scan)scan(14)", null)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (web.canGoBack()) web.goBack() else super.onBackPressed()
    }

    inner class Bridge {
        @JavascriptInterface
        fun getSms(days: Int): String {
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) return "denied"
            val out = JSONArray()
            val since = System.currentTimeMillis() - days * 86400000L
            contentResolver.query(
                Telephony.Sms.Inbox.CONTENT_URI, arrayOf("body", "date"),
                "date>?", arrayOf(since.toString()), "date DESC"
            )?.use { c ->
                var n = 0
                while (c.moveToNext() && n < 500) {
                    out.put(JSONObject().put("b", c.getString(0) ?: "").put("d", c.getLong(1)))
                    n++
                }
            }
            return out.toString()
        }
    }
}
