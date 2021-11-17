package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivityCartBinding
import br.senac.culturai.databinding.CardCartBinding
import br.senac.culturai.databinding.CardHomeBinding
import br.senac.culturai.model.Cart
import br.senac.culturai.model.Product
import br.senac.culturai.model.User
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateCart()

        binding.buttonCart.setOnClickListener{
            val i = Intent(this, CheckoutPaymentActivity::class.java)
            startActivity(i)
        }
    }

    fun updateCart() {
        val callback = object: Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                val cart = response.body()
                if(response.isSuccessful && cart != null){
                    updateCartUI(cart)

                }else if(response.code() == 401) {
                    Snackbar.make(binding.LinearLayout3, "Falha na autentição, faça o login novamente", Snackbar.LENGTH_LONG)
                        .show()
                } else{
                    var msg = response.message().toString()
                    if(msg == "") {
                        msg = "Não foi possivel entrar na conta"
                    }

                    Snackbar.make(binding.LinearLayout3, "Produto adicionado ao carrinho", Snackbar.LENGTH_LONG)
                        .show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                Snackbar.make(binding.LinearLayout3, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()
                Log.e("Error", "Falha ao executar serviço", t)
            }

        }

        API(this).cart.list().enqueue(callback)
    }

    fun updateCartUI(List: List<Cart>?) {
        List?.forEach() {
            val cardBinding = CardCartBinding.inflate(layoutInflater)


            Picasso.get().load("http://10.0.2.2:8000/${it.product.image}")
                .error(R.drawable.no_image)
                .into(cardBinding.CardImageCart)

            cardBinding.CardNameCart.text = it.product.name
            cardBinding.CardQuantityCart.text = it.quantity.toString()
            cardBinding.CardPriceCart.text = "Valor: R$${it.product.price} - Un"

            cardBinding.ButtonCartAdd.setOnClickListener{view ->
                addCart(it.product_id)
            }

            cardBinding.ButtonCartRemove.setOnClickListener{view ->
                removeCart(it.product_id)
            }

            binding.containerCart.addView(cardBinding.root)
        }
    }

    fun addCart(id: Int) {
        val callback = object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.LinearLayout3,"Pedido adicionado ao carrinho", Snackbar.LENGTH_LONG)
                        .show()

                }else {
                    var msg = response.message().toString()
                    if(msg == "") {
                        msg = "Não adicionar o produto ao carrinho"
                    }

                    Snackbar.make(binding.LinearLayout3, msg, Snackbar.LENGTH_LONG)
                        .show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(binding.LinearLayout3, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()
                Log.e("Error", "Falha ao executar serviço", t)
            }

        }

        API(this).cart.add(id).enqueue(callback)
    }

    fun removeCart(id: Int) {
        val callback = object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.LinearLayout3,"Pedido removido do carrinho", Snackbar.LENGTH_LONG)
                        .show()

                }else {
                    var msg = response.message().toString()
                    if(msg == "") {
                        msg = "Não foi possivel entrar na conta"
                    }

                    Snackbar.make(binding.LinearLayout3, msg, Snackbar.LENGTH_LONG)
                        .show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(binding.LinearLayout3, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()
                Log.e("Error", "Falha ao executar serviço", t)
            }

        }

        API(this).cart.remove(id).enqueue(callback)
    }

}