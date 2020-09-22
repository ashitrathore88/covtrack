package com.covid.covtrack.views.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.covid.covtrack.R


class NewsViewFragment : Fragment() {

    var url: String = ""
    var webView: WebView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            url = arguments!!.getString("newsId", "")
        }
        return inflater.inflate(R.layout.fragment_news_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById<WebView>(R.id.webview)
        val webSettings = webView!!.getSettings()
        webSettings.setJavaScriptEnabled(true)

        val webViewClient = WebViewClientImpl()
        webView!!.setWebViewClient(webViewClient)
        webView!!.loadUrl(url)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (webView!!.canGoBack()) {
                        webView!!.goBack();
                    } else {


                        view.findNavController().popBackStack(R.id.navigation_news_tab, false)
                        //view.findNavController().navigateUp()

                    }
                }
            })
    }

    class WebViewClientImpl() : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }


}
