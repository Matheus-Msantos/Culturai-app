package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivityCheckoutFinishBinding
import br.senac.culturai.model.User
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutFinishActivity : AppCompatActivity() {

    lateinit var binding: ActivityCheckoutFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val status = intent.getStringExtra("status")
        val card = intent.getStringExtra("card")
        val date = intent.getStringExtra("data")

        binding.textCheckoutStatus.text = "Status do pedido: ${status}"
        binding.textCheckoutCard.text = "Pago com o cartão: XXXX.XXXX.XXXX.${card}"
        binding.textCheckoutData.text = "Data da compra: ${date}"

        binding.buttonCheckoutHome.setOnClickListener{
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }


        val callback = object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if(response.isSuccessful && user != null){
                    showUserUI(user)

                }else if(response.code() == 401) {
                    Snackbar.make(binding.scrollView2, "Falha na autentição, faça o login novamente", Snackbar.LENGTH_LONG)
                        .show()
                } else{
                    var msg = response.message().toString()
                    if(msg == "") {
                        msg = "Não foi possivel entrar na conta"
                    }

                    Snackbar.make(binding.scrollView2, msg, Snackbar.LENGTH_LONG)
                        .show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Snackbar.make(binding.scrollView2, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()
                Log.e("Error", "Falha ao executar serviço", t)
            }

        }

        API(this).account.user().enqueue(callback)

    }

    fun showUserUI(user: User?) {
        binding.textCheckoutTitle.text = "Comprado pelo usuário: ${user?.email}"
    }

}