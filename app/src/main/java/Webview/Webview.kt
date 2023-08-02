package Webview

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity

class Webview(activity: FragmentActivity) : WebViewClient() {
    private val activity = activity

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url: String = request?.url.toString();
        view?.loadUrl(url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
        return true
    }

    override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
        webView.loadUrl(url)
        return true
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        var message = "SSL Certificate error."
        when (error.primaryError) {
            SslError.SSL_UNTRUSTED ->
                message = "The certificate authority is not trusted."
            SslError.SSL_EXPIRED ->
                message = "The certificate has expired.";
            SslError.SSL_IDMISMATCH ->
                message = "The certificate Hostname mismatch.";
            SslError.SSL_NOTYETVALID ->
                message = "The certificate is not yet valid.";
        }
        message += "\"SSL Certificate Error\" Do you want to continue anyway?";
        //Log your message
        handler.proceed()

    }
}