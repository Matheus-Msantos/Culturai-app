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
import br.senac.culturai.databinding.FragmentEventListBinding
import br.senac.culturai.model.Product
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventListFragment : Fragment() {
    lateinit var binding: FragmentEventListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventListBinding.inflate(inflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateProduct()
    }

    fun updateProduct() {
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                if(response.isSuccessful) {
                    val listProduct = response.body()
                    updateUI(listProduct)
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

        API(this).product.list().enqueue(callback)
    }

    fun updateUI(List: List<Product>?) {
        binding.container.removeAllViews()
        List?.forEach() {
            val cardBinding = CardHomeBinding.inflate(layoutInflater)

            val address = """
                |${it.address.state}, ${it.address.district}, ${it.address.number} - ${it.address.city}
            """.trimMargin()

            Picasso.get().load("${it.image}")
                .error(R.drawable.no_image)
                .into(cardBinding.imageCardHome)

            cardBinding.textCardHomeTime.text = it.time
            cardBinding.textCardHomeTitle.text = it.name
            cardBinding.textCardHomeAddress.text = address

            cardBinding.root.setOnClickListener{view ->
                val i = Intent(activity, EventActivity::class.java)

                i.putExtra("name", it.name)
                i.putExtra("date", it.date)
                i.putExtra("address", address)
                i.putExtra("price", "R$ ${it.price}")
                i.putExtra("classification", it.classification)
                i.putExtra("description", it.description)
                i.putExtra("category", it.category.name)
                i.putExtra("time", it.time)

                startActivity(i)
            }

            binding.container.addView(cardBinding.root)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventListFragment()
    }
}