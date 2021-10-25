package br.senac.culturai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.culturai.databinding.ActivityCartBinding
import br.senac.culturai.databinding.ActivityEventBinding

class EventActivity : AppCompatActivity() {

    lateinit var binding: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageEvent = binding.imageEvent
        val textEventTitle = binding.textEventTitle
        val textEventDate = binding.textEventDate
        val  textEventAddress = binding.textEventAddress
        val textEventPrice = binding.textEventPrice
        val  textEventClassification = binding.textEventClassification
        val  textEventDescription = binding.textEventDescription
        val  buttonTickets = binding.buttonTickets

    }
}