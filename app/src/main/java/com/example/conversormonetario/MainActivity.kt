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
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    //We use lazy to make sure the instances are only created when needed.
    private val serverResponseView: TextView by lazy {
        binding.result
    }
    private lateinit var amount: EditText
    private lateinit var item1: String
    private lateinit var item2: String

    private lateinit var dropDownMenu1: AutoCompleteTextView
    private lateinit var dropDownMenu2: AutoCompleteTextView


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

        amount = binding.value1

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

        dropDownMenu1 = binding.autoCompleteTextView
        dropDownMenu2 = binding.autoCompleteTextView2

        with(dropDownMenu1){
            setAdapter(adapter1)
        }

        with(dropDownMenu2){
            setAdapter(adapter2)
        }
        
        dropDownMenu1.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            onDropDowmItemClick1(parent,view,position,id)
        }

        dropDownMenu2.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            onDropDowmItemClick2(parent,view,position,id)
        }

        binding.buttonConvertir.setOnClickListener {
            getConvertFunctionResponse()
        }
    }

    private fun onDropDowmItemClick1(parent: AdapterView<*>, view: View?, position: Int, id: Long){

        item1 = parent.getItemAtPosition(position).toString()
        when(item1){
            "Sol" -> item1 = "PEN"
            "Dolar" -> item1 = "USD"
            "Euro" -> item1 = "EUR"
            "Won Koreano" -> item1 = "KRW"
            "Yen" -> item1 = "JPY"
            "Pesos MXN" -> item1 = "MXN"
        }
    }

    private fun onDropDowmItemClick2(parent: AdapterView<*>, view: View?, position: Int, id: Long){

        item2 = parent.getItemAtPosition(position).toString()
        when(item2){
            "Sol" -> item2 = "PEN"
            "Dolar" -> item2 = "USD"
            "Euro" -> item2 = "EUR"
            "Won Koreano" -> item2 = "KRW"
            "Yen" -> item2 = "JPY"
            "Pesos MXN" -> item2 = "MXN"
        }
    }

    private fun getConvertFunctionResponse(){
        val call = apiLayerService.searchConvert("ermJivbq91H7RNv7QRYuc4WBDvFf0F7g",item2,item1,amount.text.toString().toBigDecimal())
        call.enqueue(object: Callback<CurrencyResultData> {
            override fun onFailure(call: Call<CurrencyResultData>, t: Throwable) {
                Log.e("MainActivity", "Failed to get search results",t)
            }

            override fun onResponse(call: Call<CurrencyResultData>, response: Response<CurrencyResultData>) {
                if (response.isSuccessful){
                    val currencyResult = response.body()
                    val resultado = currencyResult?.result
                    serverResponseView.text = "Result: $resultado $item2 "
                } else {
                    Log.e("MainActivity",
                    "Failed to get search result\n ${response.errorBody()?.string() ?: ""}"
                    )
                }
            }
        })
    }

}
