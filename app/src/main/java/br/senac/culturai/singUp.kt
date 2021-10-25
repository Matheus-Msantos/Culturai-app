package br.senac.culturai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.culturai.databinding.ActivitySingUpBinding

class singUp : AppCompatActivity() {

    lateinit var binding: ActivitySingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editCreateName = binding.editCreateName
        val editCreateEmail = binding.editCreateEmail
        val  editConfirmEmail = binding.editConfirmEmail
        val  editCreatePass = binding.editCreatePass
        val  editConfirmPass = binding.editConfirmPass
        val  buttonCreateAccount= binding.buttonCreateAccount
    }
}