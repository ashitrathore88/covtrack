package com.covid.covtrack.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class CovidNews : BaseResponse() {
    @SerializedName("data")
    @Expose
    val data: List<NewsData>? = null

    class NewsData : Serializable {
        @SerializedName("id")
        @Expose
        val id: String? = null
        @SerializedName("title")
        @Expose
        val title: String? = null
        @SerializedName("soruce_title")
        @Expose
        val soruceTitle: String? = null
        @SerializedName("soruce_link")
        @Expose
        val soruceLink: String? = null
        @SerializedName("video_url")
        @Expose
        val videoUrl: String? = null
        @SerializedName("date_time")
        @Expose
        val dateTime: String? = null
        @SerializedName("description")
        @Expose
        val description: String? = null
    }
}