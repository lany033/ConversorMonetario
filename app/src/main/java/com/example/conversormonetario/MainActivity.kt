package com.example.conversormonetario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.conversormonetario.databinding.ActivityMainBinding
import com.example.conversormonetario.model.CurrencyResultData
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    //We use lazy to make sure the instances are only created when needed.
    private val serverResponseView: TextView by lazy {
        binding.result
    }

    private lateinit var item1: String

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.apilayer.com/currency_data/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val apiLayerService by lazy {
        retrofit.create(ApiLayerService::class.java)
    }

    companion object {
        private const val APP_ID = "ermJivbq91H7RNv7QRYuc4WBDvFf0F7g"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val divisas1 = resources.getStringArray(R.array.divisas)
        val divisas2 = resources.getStringArray(R.array.divisas)

        val adapter1 = ArrayAdapter(
            this,
            R.layout.list_item,
            divisas1
        )

        val adapter2 = ArrayAdapter(
            this,
            R.layout.list_item,
            divisas2
        )

        with(binding.autoCompleteTextView){
            setAdapter(adapter1)
        }

        with(binding.autoCompleteTextView2){
            setAdapter(adapter2)
        }

        binding.buttonConvertir.setOnClickListener {
            getConvertFunctionResponse()
        }
    }

    fun onDropDowmItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long){
        val selectedItem = parent.getItemAtPosition(position) as String
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        item1 = parent?.getItemAtPosition(position).toString()
        when(item1){
            "Sol" -> item1 = "PEN"
            "Dolar" -> item1 = "USD"
            "Euro" -> item1 = "EUR"
            "Won Koreano" -> item1 = "KRW"
            "Yen" -> item1 = "JPY"
            "Pesos MXN" -> item1 = "MXN"
        }

    }

    private fun getConvertFunctionResponse(){
        val call = apiLayerService.searchConvert("ermJivbq91H7RNv7QRYuc4WBDvFf0F7g",item1,"usd",5)
        call.enqueue(object: Callback<CurrencyResultData> {
            override fun onFailure(call: Call<CurrencyResultData>, t: Throwable) {
                Log.e("MainActivity", "Failed to get search results",t)
            }

            override fun onResponse(call: Call<CurrencyResultData>, response: Response<CurrencyResultData>) {
                if (response.isSuccessful){
                    val currencyResult = response.body()
                    val resultado = currencyResult?.result
                    serverResponseView.text = "Result: $resultado $item1"
                } else {
                    Log.e("MainActivity",
                    "Failed to get search result\n ${response.errorBody()?.string() ?: ""}"
                    )
                }
            }
        })
    }

}
