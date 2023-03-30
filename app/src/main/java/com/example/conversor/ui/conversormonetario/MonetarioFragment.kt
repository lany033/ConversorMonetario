package com.example.conversor.ui.conversormonetario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.conversor.ApiLayerService
import com.example.conversor.R
import com.example.conversor.databinding.FragmentMonetarioBinding
import com.example.conversor.model.CurrencyResultData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MonetarioFragment : Fragment() {

    private lateinit var binding: FragmentMonetarioBinding

    private val serverResponseView: TextView by lazy {
        binding.result
    }

    private lateinit var amount: EditText
    private lateinit var item1: String
    private lateinit var item2: String

    private lateinit var dropDownMenu1: AutoCompleteTextView
    private lateinit var dropDownMenu2: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_monetario, container, false)
        binding = FragmentMonetarioBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amount = binding.value1
        val context = requireContext()

        val divisas1 = resources.getStringArray(R.array.divisas)
        val divisas2 = resources.getStringArray(R.array.divisas)

        val adapter1 = ArrayAdapter(context,R.layout.list_item,divisas1)
        val adapter2 = ArrayAdapter(context,R.layout.list_item,divisas2)

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

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.apilayer.com/currency_data/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val apiLayerService by lazy {
        retrofit.create(ApiLayerService::class.java)
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