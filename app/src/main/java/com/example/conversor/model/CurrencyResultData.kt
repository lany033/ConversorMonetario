package com.example.conversor.model

import com.squareup.moshi.Json

data class CurrencyResultData(
    @field:Json(name="result") val result: Double,

)
