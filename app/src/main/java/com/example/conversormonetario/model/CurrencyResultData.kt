package com.example.conversormonetario.model

import com.squareup.moshi.Json

data class CurrencyResultData(
    @field:Json(name="result") val result: Double,

)
