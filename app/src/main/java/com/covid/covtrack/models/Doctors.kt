package com.covid.covtrack.models

data class Doctors(
    val `data`: List<Doctors.Doctor>,
    val err_code: String,
    val message: String,
    val title: String
){
    data class Doctor(
        val id: String,
        val doctor_name: String,
        val hospital_name: String,
        val speciality: String,
        val doctor_name_pb: String,
        val hospital_name_pb: String,
        val speciality_pb: String,
        val mobile_no: String
    )
}