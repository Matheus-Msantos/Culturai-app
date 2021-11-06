package br.senac.culturai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import br.senac.culturai.databinding.ActivityCartBinding
import br.senac.culturai.databinding.ActivityEventBinding

class EventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.myToolBar))


        val imageEvent = binding.imageEvent
        val textEventTitle = binding.textEventTitle
        val textEventDate = binding.textEventDate
        val textEventAddress = binding.textEventAddress
        val textEventPrice = binding.textEventPrice
        val textEventClassification = binding.textEventClassification
        val textEventDescription = binding.textEventDescription
        val textEventCategory = binding.textEventCategory
        val textEventTime = binding.textEventTime
        val buttonTickets = binding.buttonTickets

        val name = intent.getStringExtra("name")
        val date = intent.getStringExtra("date")
        val address = intent.getStringExtra("address")
        val price = intent.getStringExtra("price")
        val classification = intent.getStringExtra("classification")
        val description = intent.getStringExtra("description")
        val category = intent.getStringExtra("category")
        val time = intent.getStringExtra("time")

        textEventTitle.text = name
        textEventDate.text = date
        textEventAddress.text = address
        textEventPrice.text = price
        textEventClassification.text = classification
        textEventDescription.text = description
        textEventCategory.text = category
        textEventTime.text = time

    }
}