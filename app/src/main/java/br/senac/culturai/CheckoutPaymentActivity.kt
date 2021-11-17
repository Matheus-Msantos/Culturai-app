package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.culturai.databinding.ActivityCartBinding
import br.senac.culturai.databinding.ActivityCheckoutBinding
import br.senac.culturai.databinding.ActivityCheckoutPaymentBinding

class CheckoutPaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityCheckoutPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCheckoutPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var editCheckoutPaymentName = binding.editCheckoutPaymentName
        var editCheckoutPaymentEmail = binding.editCheckoutPaymentEmail
        var editCheckoutPaymentCPF = binding.editCheckoutPaymentCpf
        var checkCheckoutDebito = binding.checkCheckoutDebito
        var checkCheckoutCredito = binding.checkCheckoutCredito


        binding.buttonFinishPayment.setOnClickListener{
            val i = Intent(this, CheckoutActivity::class.java)
            startActivity(i)
        }
    }
}