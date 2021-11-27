package br.senac.culturai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.culturai.api.API
import br.senac.culturai.databinding.CardHomeBinding
import br.senac.culturai.databinding.FragmentSearchBinding
import br.senac.culturai.model.Product
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM_SEARCH = "searchValue"

class SearchFragment : Fragment() {
    private var searchValue: String? = null
    lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        arguments?.let {
            searchValue = it.getString(ARG_PARAM_SEARCH)
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater)

        binding.textView11.text = "Você pesquisou por: $searchValue"

        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                if(response.isSuccessful) {
                    val listProduct = response.body()
                   listProduct?.forEach() {Product ->

                       if(Product.name == searchValue || Product.category.name == searchValue || Product.classification == searchValue) {
                           val cardBinding = CardHomeBinding.inflate(layoutInflater)

                           val address = """
                                        |${Product.address.state}, ${Product.address.district}, ${Product.address.number} - ${Product.address.city}
                           """.trimMargin()

                           Picasso.get().load("http://10.0.2.2:8000/${Product.image}")
                               .error(R.drawable.no_image)
                               .into(cardBinding.imageCardHome)

                           cardBinding.textCardHomeTime.text = Product.time
                           cardBinding.textCardHomeTitle.text = Product.name
                           cardBinding.textCardHomeAddress.text = address

                           cardBinding.root.setOnClickListener{view ->
                               val i = Intent(activity, EventActivity::class.java)

                               i.putExtra("product_id", Product.id)
                               i.putExtra("image", Product.image)
                               i.putExtra("name", Product.name)
                               i.putExtra("date", Product.date)
                               i.putExtra("address", address)
                               i.putExtra("price", "R$ ${Product.price}")
                               i.putExtra("classification", Product.classification)
                               i.putExtra("description", Product.description)
                               i.putExtra("category", Product.category.name)
                               i.putExtra("time", Product.time)

                               startActivity(i)
                           }

                           binding.container.addView(cardBinding.root)
                       }
                   }
                }else {
                    Snackbar.make(binding.container, "Não é possivel atualizar os produtos", Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(binding.container, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()

                Log.e("Error", "Falha ao executar serviço", t)
            }
        }

        val cxt = getActivity()?.getApplicationContext()
        if (cxt != null) {
            API(cxt).product.list().enqueue(callback)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(searchValue: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_SEARCH, searchValue)
                }
            }
    }

    fun updateProduct() {

    }

    fun updateUI(List: List<Product>?) {
        binding.container.removeAllViews()

    }
}

