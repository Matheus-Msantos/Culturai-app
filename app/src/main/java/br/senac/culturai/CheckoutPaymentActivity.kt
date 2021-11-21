package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.senac.culturai.databinding.ActivityCartBinding
import br.senac.culturai.databinding.ActivityCheckoutBinding
import br.senac.culturai.databinding.ActivityCheckoutPaymentBinding

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
                    cpf.error = "Preencha o campo de e-mail"
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
}