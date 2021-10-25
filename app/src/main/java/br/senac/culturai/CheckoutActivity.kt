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


        val editCheckoutCardNumber = binding.editCheckoutCardNumber
        val  editCheckoutCardVal = binding.editCheckoutCardVal
        val  editCheckoutCardCvv = binding.editCheckoutCardCvv
        val editCheckoutCardName = binding.editCheckoutCardName
        val editCheckoutCardCpf = binding.editCheckoutCardCpf
        val buttonFinishPayment = binding.buttonFinishPayment



    }
}