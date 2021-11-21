package br.senac.culturai


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivityCheckoutBinding
import br.senac.culturai.databinding.CardHomeBinding
import br.senac.culturai.model.Order
import br.senac.culturai.model.Payment
import br.senac.culturai.model.Product
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cardNumber = binding.editCheckoutNumber
        val validate = binding.editCheckoutValidate
        val cvv = binding.editCheckoutCvv
        val name = binding.editCheckoutName
        val buttonFinishPayment = binding.buttonFinishCheckout

        buttonFinishPayment.setOnClickListener {

            when {

                /*--- Validação de campos ---*/

                cardNumber.text.isNullOrEmpty() -> {
                    cardNumber.error = "Preencha o campo de Número do cartão"
                    Toast.makeText(this@CheckoutActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                validate.text.isNullOrEmpty() -> {
                    validate.error = "Preencha o campo de validade do cartão"
                    Toast.makeText(this@CheckoutActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                cvv.text.isNullOrEmpty() -> {
                    validate.error = "Preencha o campo de código de segurança"
                    Toast.makeText(this@CheckoutActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                name.text.isNullOrEmpty() -> {
                    name.error = "Preencha o campo de nome do titular"
                    Toast.makeText(this@CheckoutActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                else -> {

                    /*--- Chamada do serviço ---*/

                    val cc_number = Integer.parseInt(cardNumber.getText().toString());
                    val callback = object : Callback<Order> {
                        override fun onResponse(call: Call<Order>, response: Response<Order>) {

                            if (response.isSuccessful) {
                                val listOrder = response.body()
                                orderUI(listOrder)

                            } else {
                                var msg = response.message().toString()

                                if (msg == "") {
                                    msg = "Não foi possivel efutuar login"
                                }

                                Toast.makeText(this@CheckoutActivity, msg, Toast.LENGTH_LONG).show()
                                response.errorBody()?.let {
                                    Log.e("Error", it.string())
                                }
                            }
                        }

                        override fun onFailure(call: Call<Order>, t: Throwable) {
                            Toast.makeText(this@CheckoutActivity,"Não foi possivel se conectar ao servidor", Toast.LENGTH_LONG).show()
                            Log.e("Error", "Falha ao executar serviço", t)
                        }

                    }
                    API(this).order.add(Payment(cc_number)).enqueue(callback)

                }

            }

        }

    }

    fun orderUI(List: Order?) {

            binding.buttonFinishCheckout.setOnClickListener{view ->
                val i = Intent(this@CheckoutActivity, CheckoutFinishActivity::class.java)
                i.putExtra("card", List?.cc_number)
                i.putExtra("status", List?.status)
                i.putExtra("data", List?.created_at)
                startActivity(i)
            }


    }
}