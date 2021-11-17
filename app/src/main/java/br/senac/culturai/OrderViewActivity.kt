package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivityOrderViewBinding
import br.senac.culturai.databinding.CardOrderBinding
import br.senac.culturai.model.Order
import br.senac.culturai.model.OrderItem
import br.senac.culturai.model.Product
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrderViewBinding
    var product_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("orderItem_id", -1)
        updateOrderItem(id)

    }

    fun updateOrderItem(id: Int) {


        val callback = object : Callback<List<OrderItem>> {
            override fun onResponse(call: Call<List<OrderItem>>, response: Response<List<OrderItem>>) {

                if(response.isSuccessful) {
                    val listOrderItem = response.body()
                    updateOrderItemUI(listOrderItem)
                }else {
                    Snackbar.make(binding.ContraintLayout, "Não é possivel atualizar os produtos", Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<OrderItem>>, t: Throwable) {
                Snackbar.make(binding.ContraintLayout, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()

                Log.e("Error", "Falha ao executar serviço", t)
            }
        }

        API(this).order.orderItem(id).enqueue(callback)
    }

    fun updateProduct(id: Int) {
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                if(response.isSuccessful) {
                    val product = response.body()
                    updateProductUI(product)
                }else {
                    Snackbar.make(binding.ContraintLayout, "Não é possivel atualizar os produtos", Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(binding.ContraintLayout, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()

                Log.e("Error", "Falha ao executar serviço de produto", t)
            }
        }



        API(this).product.search(id).enqueue(callback)
    }

    fun updateOrderItemUI(List: List<OrderItem>?) {
        List?.forEach() {
            binding.textOrderViewPriceBuy.text = "Valor da compra: R$${it.price}"
            binding.textOrderViewNumberCard.text = "Numero do cartão: xxxx.xxxx.xxxx.${it.order.cc_number}"
            binding.textOrderViewDateBuy.text = "Data da compra: ${it.order.created_at}"
            updateProduct(it.product_id)
        }
    }

    fun updateProductUI(List: List<Product>?) {
        List?.forEach() {

            val address = """
                |${it.address.state}, ${it.address.district}, ${it.address.number} - ${it.address.city}
            """.trimMargin()


            Picasso.get().load("http://10.0.2.2:8000/${it.image}")
                .error(R.drawable.no_image)
                .into(binding.imageView)

            binding.textOrderViewName.text = it.name
            binding.textOrderViewAddress.text = address
            binding.textOrderViewDate.text = "${it.date} - ${it.time}"
        }
    }
}