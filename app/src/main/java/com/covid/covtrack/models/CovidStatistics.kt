package com.covid.covtrack.models

import android.os.Parcel
import android.os.Parcelable

data class CovidStatistics(
    val `data`: List<CovidDivision>,
    val err_code: String,
    val graph_image: String,
    val message: String,
    val title: String
){
    data class CovidDivision(
        //val confirmed: String,
        val active: String,
        val deaths: String,
        //val home_quarantine: String,
        //val hospital_quarantine: String,
        val total_tested: String,
        val id: String,
        val last_updated: String,
        val name: String,
        val place_name: String,
        val recovered: String,
        //val result_awaited: String,
        //val results: String,
        val subs: List<CovidSubDivision>,
        //val tested: String,
        val source: String,
        val positive: String,
        val negative: String,
        val result_awaited: String
    )

    data class CovidSubDivision(
        val city: String?,
        val confirmed: String?,
        val covid_statistics_id: String?,
        val created_at: String?,
        val deaths: String?,
        val id: String?,
        val recovered: String?,
        val tested: String?,
        val updated_at: String?
    ):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(city)
            parcel.writeString(confirmed)
            parcel.writeString(covid_statistics_id)
            parcel.writeString(created_at)
            parcel.writeString(deaths)
            parcel.writeString(id)
            parcel.writeString(recovered)
            parcel.writeString(tested)
            parcel.writeString(updated_at)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<CovidSubDivision> {
            override fun createFromParcel(parcel: Parcel): CovidSubDivision {
                return CovidSubDivision(parcel)
            }

            override fun newArray(size: Int): Array<CovidSubDivision?> {
                return arrayOfNulls(size)
            }
        }
    }
}