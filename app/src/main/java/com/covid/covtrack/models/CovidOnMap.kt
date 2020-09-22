package com.covid.covtrack.models

data class CovidOnMap(
    val data: List<MapData>
) : BaseResponse() {
    data class MapData(
        val id: String,
        val logo_image: String,
        val name: String,
        val patients: List<PatientsData>
    )

    data class PatientsData(
        val id: String,
        val category_id: String,
        val title: String,
        val latitude: String,
        val longitude: String
    )

}