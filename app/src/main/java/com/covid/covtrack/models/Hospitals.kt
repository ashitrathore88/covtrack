package com.covid.covtrack.models

data class Hospitals(
    val `data`: List<Hospital>,
    val err_code: String,
    val message: String,
    val title: String
){
    data class Hospital(
        val id: String,
        val hospital: String,
        val hospital_pb: String,
        val address: String,
        val address_pb: String,
        val beds: String
    )
}