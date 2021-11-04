package br.senac.culturai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.culturai.databinding.ActivityCheckoutFinishBinding

class CheckoutFinishActivity : AppCompatActivity() {

    lateinit var binding: ActivityCheckoutFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}