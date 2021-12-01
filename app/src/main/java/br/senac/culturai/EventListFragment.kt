package br.senac.culturai


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import br.senac.culturai.api.API
import br.senac.culturai.databinding.CardHomeBinding
import br.senac.culturai.databinding.FragmentEventListBinding
import br.senac.culturai.model.Category
import br.senac.culturai.model.Product
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.chip.Chip

class EventListFragment() : Fragment() {
    lateinit var binding: FragmentEventListBinding
    var category = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventListBinding.inflate(inflater)

        val searchItem = binding.search
        val searchView = searchItem

        searchView.let {
            val queryListener = object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchView.clearFocus()
                        val searchFragment = SearchFragment.newInstance(it)
                        parentFragmentManager.beginTransaction().replace(R.id.container, searchFragment).commit()
                    }
                   return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            }
            searchView.setOnQueryTextListener(queryListener)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateProduct()
        updateCategory()
    }

    fun updateProduct() {
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                if(response.isSuccessful) {
                    val listProduct = response.body()
                    updateUI(listProduct)
                }else {
                    Snackbar.make(binding.container, "Não é possivel atualizar os eventos", Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(binding.container, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG).show()
                Log.e("Error", "Falha ao executar serviço", t)
            }
        }

        val cxt = getActivity()?.getApplicationContext()
        if (cxt != null) {
            API(cxt).product.list().enqueue(callback)
        }
    }

    fun updateCategory() {
        val callback = object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {

                if(response.isSuccessful) {
                    val listCategory = response.body()
                    updateCategoryUI(listCategory)
                }else {
                    Snackbar.make(binding.container, "Não é possivel carregar os eventos", Snackbar.LENGTH_LONG).show()
                    Log.e("Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Snackbar.make(binding.container, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG).show()
                Log.e("Error", "Falha ao executar serviço", t)
            }
        }

        val cxt = getActivity()?.getApplicationContext()
        if (cxt != null) {
            API(cxt).category.list().enqueue(callback)
        }
    }

    fun filter(id : Int) {
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                if(response.isSuccessful) {
                    val filterProduct = response.body()
                    filterProductUI(filterProduct)
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
            API(cxt).filter.filter(id).enqueue(callback)
        }
    }

    fun updateUI(List: List<Product>?) {
        binding.container.removeAllViews()
        List?.forEach() {
            val cardBinding = CardHomeBinding.inflate(layoutInflater)

            val address = """
                | ${it.address.street}, ${it.address.district}, ${it.address.number}, ${it.address.city}, ${it.address.city}
            """.trimMargin()

            Picasso.get().load("http://10.0.2.2:8000/${it.image}")
                .error(R.drawable.no_image)
                .into(cardBinding.imageCardHome)

            cardBinding.textCardHomeTime.text = it.time
            cardBinding.textCardHomeTitle.text = it.name
            cardBinding.textCardHomeAddress.text = address


            cardBinding.root.setOnClickListener{view ->
                val i = Intent(activity, EventActivity::class.java)

                i.putExtra("product_id", it.id)
                i.putExtra("image", it.image)
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

    fun updateCategoryUI(List: List<Category>?) {
        binding.chipGroup.removeAllViews()
        val chipGet = binding.chip4

        List?.forEach() {
            val genres = it.name
            val chip = Chip(chipGet.getContext())
            chip.text = genres

            chip.setOnClickListener{view ->
                filter(it.id)
            }

            binding.chipGroup.addView(chip)
        }
    }

    fun filterProductUI(List: List<Product>?) {
        binding.container.removeAllViews()
        List?.forEach() {
            val cardBinding = CardHomeBinding.inflate(layoutInflater)
            val address = """
                | ${it.address.street}, ${it.address.district}, ${it.address.number}, ${it.address.city}, ${it.address.city}
            """.trimMargin()

            Picasso.get().load("http://10.0.2.2:8000/${it.image}")
                .error(R.drawable.no_image)
                .into(cardBinding.imageCardHome)

            cardBinding.textCardHomeTime.text = it.time
            cardBinding.textCardHomeTitle.text = it.name
            cardBinding.textCardHomeAddress.text = address


            cardBinding.root.setOnClickListener{view ->
                val i = Intent(activity, EventActivity::class.java)

                i.putExtra("product_id", it.id)
                i.putExtra("image", it.image)
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