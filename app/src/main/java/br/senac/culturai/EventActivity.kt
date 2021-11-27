package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import androidx.fragment.app.Fragment
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivityEventBinding
import br.senac.culturai.databinding.ActivityHomeBinding
import br.senac.culturai.databinding.CardHomeBinding
import br.senac.culturai.model.Cart
import br.senac.culturai.model.Product
import br.senac.culturai.model.User
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.myToolBar))

        val imageEvent = binding.imageEvent
        val textEventTitle = binding.textEventTitle
        val textEventDate = binding.textEventDate
        val textEventAddress = binding.textEventAddress
        val textEventPrice = binding.textEventPrice
        val textEventClassification = binding.textEventClassification
        val textEventDescription = binding.textEventDescription
        val textEventCategory = binding.textEventCategory
        val textEventTime = binding.textEventTime
        val buttonTickets = binding.buttonTickets

        val id = intent.getIntExtra("product_id", -1)
        val image = intent.getStringExtra("image")
        val name = intent.getStringExtra("name")
        val date = intent.getStringExtra("date")
        val address = intent.getStringExtra("address")
        val price = intent.getStringExtra("price")
        val classification = intent.getStringExtra("classification")
        val description = intent.getStringExtra("description")
        val category = intent.getStringExtra("category")
        val time = intent.getStringExtra("time")


        Picasso.get().load("http://10.0.2.2:8000/${image}")
            .error(R.drawable.no_image)
            .into(imageEvent)

        textEventTitle.text = name
        textEventDate.text = date
        textEventAddress.text = address
        textEventPrice.text = price
        textEventClassification.text = classification
        textEventDescription.text = description
        textEventCategory.text = category
        textEventTime.text = time

        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                if(response.isSuccessful) {
                    val listProduct = response.body()
                    updateUI(listProduct)
                }else {
                    Snackbar.make(binding.LinearLayout, "Não é possivel carregar as inforções desse evento", Snackbar.LENGTH_LONG).show()
                    Log.e("Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(binding.LinearLayout, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG).show()
                Log.e("Error", "Falha ao executar serviço", t)
            }
        }

        val idProduct = intent.getStringExtra("qrcode")?.toInt()

        if (idProduct != null) {
            API(this).product.search(idProduct).enqueue(callback)
        }

        buttonTickets.setOnClickListener{view ->
            updateCart(id)
        }
    }

    override fun onResume() {
        super.onResume()
        updateUser()
    }

    fun updateUI(List: List<Product>?) {
        List?.forEach() {

            val address = """
                |${it.address.state}, ${it.address.district}, ${it.address.number} - ${it.address.city}
            """.trimMargin()

            Picasso.get().load("http://10.0.2.2:8000/${it.image}")
                .error(R.drawable.no_image)
                .into(binding.imageEvent)

            binding.textEventTitle.text = it.name
            binding.textEventDate.text = it.date
            binding.textEventAddress.text = address
            binding.textEventPrice.text = "R$ ${it.price}"
            binding.textEventClassification.text = it.classification
            binding.textEventDescription.text = it.description
            binding.textEventCategory.text = it.category.name
            binding.textEventTime.text = it.time

            binding.buttonTickets.setOnClickListener{view ->
                updateCart(it.id)
            }
        }
    }

    fun updateCart(id: Int) {
        val callback = object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val cart = response.body()
                if(response.isSuccessful && cart != null){

                    Snackbar.make(binding.LinearLayout, "Ingresso adicionado ao carrinho", Snackbar.LENGTH_LONG)
                        .show()

                }else if(response.code() == 401) {
                    Snackbar.make(binding.LinearLayout, "Falha na autentição, faça o login novamente", Snackbar.LENGTH_LONG)
                        .show()
                } else{
                    var msg = response.message().toString()
                    if(msg == "") {
                        msg = "Não foi possivel entrar na conta"
                    }

                    Snackbar.make(binding.LinearLayout, "Ingresso adicionado ao carrinho", Snackbar.LENGTH_LONG)
                        .show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(binding.LinearLayout, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()
                Log.e("Error", "Falha ao executar serviço", t)
            }

        }

        API(this).cart.add(id).enqueue(callback)

    }

    fun updateUser() {
        val callback = object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if(response.isSuccessful && user != null){
                    showUserUI(user)

                }else if(response.code() == 401) {
                    Snackbar.make(binding.LinearLayout, "Falha na autentição, faça o login novamente!", Snackbar.LENGTH_LONG).show()
                } else{
                    var msg = response.message().toString()

                    if(msg == "") {
                        msg = "Não foi possível acessar sua conta, faça o login novamente!"
                    }

                    Snackbar.make(binding.LinearLayout, msg, Snackbar.LENGTH_LONG).show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {}
        }

        API(this).account.user().enqueue(callback)
    }

    fun showUserUI(user: User?) {
        if(user != null) {
            binding.textButtonEnable.visibility = View.GONE
            binding.buttonTickets.visibility = View.VISIBLE
        }else {
            binding.buttonTickets.visibility = View.INVISIBLE

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.carrinho -> startActivity(Intent(this, CartActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}