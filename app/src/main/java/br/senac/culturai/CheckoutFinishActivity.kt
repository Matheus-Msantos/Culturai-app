package br.senac.culturai

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.culturai.databinding.ActivityCheckoutFinishBinding
import br.senac.culturai.databinding.ActivityCheckoutPaymentBinding

class CheckoutFinishActivity : AppCompatActivity() {

    lateinit var binding: ActivityCheckoutFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val imageCheckout = binding.imageCheckout
        val  imageCheckoutQR = binding.imageCheckoutQR
        val textCheckoutTitle = binding.textCheckoutTitle
        val textCheckoutDate = binding.textCheckoutDate
        val textCheckoutAdress = binding.textCheckoutAdress
        val textCheckoutClassification = binding.textCheckoutClassification
        val  buttonCheckoutHome = binding.buttonCheckoutHome
    }
}