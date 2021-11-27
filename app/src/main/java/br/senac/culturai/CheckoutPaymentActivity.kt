package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivityCartBinding
import br.senac.culturai.databinding.ActivityCheckoutBinding
import br.senac.culturai.databinding.ActivityCheckoutPaymentBinding
import br.senac.culturai.model.User
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutPaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = binding.editCheckoutPaymentName
        val email = binding.editCheckoutPaymentEmail
        val cpf = binding.editCheckoutPaymentCpf
        val credito = binding.checkCheckoutCredito
        val debito = binding.checkCheckoutCredito

        binding.buttonFinishPayment.setOnClickListener{

            when {
                name.text.isNullOrEmpty() -> {
                    name.error = "Preencha o campo de nome"
                    Toast.makeText(this@CheckoutPaymentActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }
                email.text.isNullOrEmpty() -> {
                    email.error = "Preencha o campo de e-mail"
                    Toast.makeText(this@CheckoutPaymentActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }
                cpf.text.isNullOrEmpty() -> {
                    cpf.error = "Preencha o campo de CPF"
                    Toast.makeText(this@CheckoutPaymentActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                !credito.isChecked && !debito.isChecked -> {
                    Toast.makeText(this@CheckoutPaymentActivity, "Selecione uma forma de pagamento", Toast.LENGTH_LONG).show()
                }

                else ->{
                    val i = Intent(this, CheckoutActivity::class.java)
                    startActivity(i)
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUser()
    }

    fun updateUser() {
        val callback = object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if(response.isSuccessful && user != null){
                    showUserUI(user)

                } else{
                    var msg = response.message().toString()

                    if(msg == "") {
                        msg = "Não foi possível inserir os dados seus dados, Por favor inserir manualmente!"
                    }

                    Snackbar.make(binding.buttonFinishPayment, msg, Snackbar.LENGTH_LONG).show()
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

        binding.editCheckoutPaymentName.setText(user?.name)
        binding.editCheckoutPaymentEmail.setText(user?.email)

    }
}