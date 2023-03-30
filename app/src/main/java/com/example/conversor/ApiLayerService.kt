package com.example.conversor

import com.example.conversor.model.CurrencyResultData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

//ENDPOINT
interface ApiLayerService {
    @GET("convert")
    fun searchConvert(
        @Query("apikey") apikey: String,
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: BigDecimal
    ) : Call<CurrencyResultData>
}