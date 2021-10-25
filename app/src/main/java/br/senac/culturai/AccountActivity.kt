package br.senac.culturai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.culturai.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val  imageAccountUser = binding.imageAccountUser
        val textAccountName = binding.textAccountName
        val  textAccountTickets  = binding.textAccountTickets
        val textAccountHistoric = binding.textAccountHistoric
        val  textAccountLocation = binding.textAccountLocation
        val  buttonAccountExit = binding.buttonAccountExit


    }
}