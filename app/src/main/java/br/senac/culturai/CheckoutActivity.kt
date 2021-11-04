package br.senac.culturai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.culturai.databinding.ActivityCartBinding
import br.senac.culturai.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editCheckoutCardNumber = binding.editCheckoutNumber
        val editCheckoutCardVal = binding.editCheckoutValidate
        val editCheckoutCardCvv = binding.editCheckoutCardCvv
        val editCheckoutCardName = binding.editCheckoutNameHolder
        val buttonFinishPayment = binding.buttonFinishCheckout



    }
}