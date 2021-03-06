package br.senac.culturai

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.culturai.api.API
import br.senac.culturai.databinding.CardOrderBinding
import br.senac.culturai.databinding.FragmentOrderBinding
import br.senac.culturai.model.Order
import com.google.android.material.snackbar.Snackbar

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderFragment : Fragment() {
    lateinit var binding: FragmentOrderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrderBinding.inflate(inflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateOrder()
    }


    fun updateOrder() {
        val callback = object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {

                if(response.isSuccessful) {
                    val listOrder = response.body()
                    updateOrderUI(listOrder)
                }else {
                    Snackbar.make(binding.container, "Não é possivel atualizar os produtos", Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("Error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Snackbar.make(binding.container, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()

                Log.e("Error", "Falha ao executar serviço", t)
            }
        }

        val cxt = getActivity()?.getApplicationContext()
        if (cxt != null) {
            API(cxt).order.list().enqueue(callback)
        }
    }

    fun updateOrderUI(List: List<Order>?) {
        binding.container.removeAllViews()
        List?.forEach() {
            val cardBinding = CardOrderBinding.inflate(layoutInflater)

            binding.textOrderEmpty.visibility = View.INVISIBLE

            cardBinding.textCardOrderId.text = "N°${it.id.toString()}"
            cardBinding.textCardOrderPrice.text = "R$ ${it.order_item[0].price}"
            cardBinding.textCardOrderDate.text = it.created_at
            cardBinding.textCardOrderStatus.text = it.status

            if(cardBinding.textCardOrderStatus.text == "Aprovado") {
                cardBinding.textCardOrderStatus.setTextColor(Color.GREEN)
            }

            if(cardBinding.textCardOrderStatus.text == "Cancelado") {
                cardBinding.textCardOrderStatus.setTextColor(Color.RED)
            }

            val item = it.order_item?.forEach {orderItem ->

                cardBinding.root.setOnClickListener{view ->
                    val i = Intent(activity, OrderViewActivity::class.java)
                    i.putExtra("orderItem_id", orderItem.id)
                    startActivity(i)
                }
            }

            binding.container.addView(cardBinding.root)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()
    }
}