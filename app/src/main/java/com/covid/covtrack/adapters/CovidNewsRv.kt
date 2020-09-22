package com.covid.covtrack.adapters

import android.graphics.Bitmap
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.models.CovidNews
import java.util.ArrayList

class CovidNewsRv(
    val newsList: ArrayList<CovidNews.NewsData>,
    val listener: OnNewsListener,
    val flag: Boolean = false
) :
    RecyclerView.Adapter<CovidNewsRv.ViewHolder>() {

    val TAG: String = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_covid_news, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = newsList[position]
        if (flag) {
            holder.rv_news_videos.visibility = View.VISIBLE
            loadVideo(model, holder)
        } else {
            holder.rv_news.visibility = View.VISIBLE
            holder.tv_desc.setText(setTextHTML(model.description!!))
            holder.tv_source.setText(model.soruceTitle)
            holder.tv_date.setText(model.dateTime)
            holder.tv_title.setText(model.title)
        }
        holder.itemView.setOnClickListener {
            if (!flag) listener.onItemClick(model.soruceLink!!,position)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rv_news = itemView.findViewById(R.id.rv_news) as RelativeLayout
        val rv_news_videos = itemView.findViewById(R.id.rv_news_videos) as RelativeLayout
        val tv_title = itemView.findViewById(R.id.tv_title) as TextView
        val tv_desc = itemView.findViewById(R.id.tv_desc) as TextView
        val tv_source = itemView.findViewById(R.id.tv_source) as TextView
        val tv_date = itemView.findViewById(R.id.tv_date) as TextView

        val videoLayout = itemView.findViewById(R.id.webview_video) as WebView
        val pgb = itemView.findViewById(R.id.pgb) as ProgressBar
        val playButton = itemView.findViewById(R.id.btnYoutube_player) as ImageView
    }

    interface OnNewsListener {
        fun onItemClick(url: String, position: Int)
    }

    fun loadVideo(
        model: CovidNews.NewsData,
        holder: ViewHolder
    ) {
        val webSettings = holder.videoLayout.getSettings()
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)

        webSettings.setJavaScriptEnabled(true)
        val videoUrl =
            "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + model.videoUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
        holder.videoLayout.loadData(videoUrl, "text/html", "utf-8")
        //holder.videoLayout.loadUrl(model.videoUrl)
        Log.d(TAG, "VideoUrl: $videoUrl")


        holder.videoLayout.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                holder.pgb.setVisibility(View.VISIBLE)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                holder.pgb.setVisibility(View.GONE)
            }

        })

        holder.playButton.setOnClickListener(View.OnClickListener {

            if (flag) listener.onItemClick("https://www.youtube.com/watch?v="+model.videoUrl!!,-1)
        })
    }

    fun setTextHTML(html: String): Spanned
    {
        val result: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
        return result
    }
}