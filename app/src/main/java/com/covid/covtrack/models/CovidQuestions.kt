package com.covid.covtrack.models

data class CovidQuestions(
    val data: Data,
    val err_code: String,
    val message: String
) {
    data class Question(
        val id: String,
        val question: String
    )

    data class Data(
        val form_enable: Boolean,
        val information: String,
        val questions: List<Question>,
        val user_age: String
    )
}