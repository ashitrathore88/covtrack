package com.covid.covtrack.views.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.covid.covtrack.R
import com.covid.covtrack.models.CovidNews
import com.covid.covtrack.utilities.Constants
import kotlinx.android.synthetic.main.fragment_news_view.*


class NewsWebViewActivity: BaseActivity(){

    var url: String? = ""
    var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_news_view)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        var bundle :Bundle ?=intent.extras
        url = bundle!!.getString("newsId")
        var title = bundle!!.getString("title")
        var is_type_txt: Boolean? = false
        var news: CovidNews.NewsData? = null
        if(bundle!!.getBoolean("TXT") != null){
            is_type_txt = bundle!!.getBoolean("TXT")
            news = bundle!!.getSerializable(Constants.NEWS_CONTENT) as? CovidNews.NewsData
        }

        supportActionBar!!.subtitle = title
        webView = findViewById<WebView>(R.id.webview)
        val webSettings = webView!!.getSettings()
        webSettings.setJavaScriptEnabled(true)

        val webViewClient = WebViewClientImpl()
        webView!!.setWebViewClient(webViewClient)
        webView!!.clearCache(true)


        if(is_type_txt!!){

            val newBody = "<html><body style='padding: 10px'>" + news!!.description!!.replace("\\r\\n","<br>")+ "</body></html>"

            webView!!.loadDataWithBaseURL(null,newBody, "text/html; charset=utf-8", "UTF-8",null);
        }
        else{
            webView!!.loadUrl(url)
        }





    }

    override fun onSupportNavigateUp(): Boolean {
        if (webView!!.canGoBack()) {
            webView!!.goBack();
        } else {


            finish()

        }
        return true
    }

    fun openBrowser(url : String){
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent);
    }



    class WebViewClientImpl() : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

}