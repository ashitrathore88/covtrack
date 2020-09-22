package com.covid.covtrack.models

data class Regions(
    val `data`: List<AllRegions>,
    val err_code: String,
    val message: String,
    val title: String
){
    data class AllRegions(
        val id: String,
        val name: String
    )
}