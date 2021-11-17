package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivityEventBinding
import br.senac.culturai.databinding.ActivityHomeBinding
import br.senac.culturai.model.Cart
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventBinding
    lateinit var toggle: ActionBarDrawerToggle

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

        buttonTickets.setOnClickListener{view ->
            updateCart(id)
        }
    }

    fun updateCart(id: Int) {
        val callback = object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val cart = response.body()
                if(response.isSuccessful && cart != null){

                    Snackbar.make(binding.LinearLayout, "Produto adicionado ao carrinho", Snackbar.LENGTH_LONG)
                        .show()

                }else if(response.code() == 401) {
                    Snackbar.make(binding.LinearLayout, "Falha na autentição, faça o login novamente", Snackbar.LENGTH_LONG)
                        .show()
                } else{
                    var msg = response.message().toString()
                    if(msg == "") {
                        msg = "Não foi possivel entrar na conta"
                    }

                    Snackbar.make(binding.LinearLayout, "Produto adicionado ao carrinho", Snackbar.LENGTH_LONG)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.carrinho -> startActivity(Intent(this, CartActivity::class.java))
        }
        return toggle.onOptionsItemSelected(item)
    }
}