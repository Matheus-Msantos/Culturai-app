package br.senac.culturai


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.senac.culturai.api.API

import br.senac.culturai.databinding.ActivityHomeBinding
import br.senac.culturai.databinding.NavHeaderMainBinding
import br.senac.culturai.model.User

import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout = binding.drawerLayout
        var frag: Fragment


        setSupportActionBar(findViewById(R.id.myToolBar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu, R.string.close_menu)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        frag = EventListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, frag)
            .commit()

        binding.navgationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            when(it.itemId) {
                R.id.home -> {
                    frag = EventListFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, frag)
                        .commit()
                }

                R.id.ingressos -> {
                    frag = OrderFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, frag)
                        .commit()
                }

                R.id.minhaConta -> {
                    frag = AccountFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, frag)
                        .commit()
                }

                R.id.login -> {
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                }
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()
        updateUser()
    }

    fun updateUser() {
        val callback = object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if(response.isSuccessful && user != null){
                    showUserUI(user)

                }else if(response.code() == 401) {
                    Snackbar.make(binding.drawerLayout, "Falha na autentição, faça o login novamente!", Snackbar.LENGTH_LONG).show()
                } else{
                    var msg = response.message().toString()

                    if(msg == "") {
                        msg = "Não foi possível acessar sua conta, faça o login novamente!"
                    }

                    Snackbar.make(binding.drawerLayout, msg, Snackbar.LENGTH_LONG).show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Snackbar.make(binding.drawerLayout, "Estamos com problemas para se conectar com o servidor!", Snackbar.LENGTH_LONG).show()
                Log.e("Error", "Falha ao executar serviço login na home!", t)
            }
        }

        API(this).account.user().enqueue(callback)
    }

    fun showUserUI(user: User?) {
        val h = binding.navgationView.getHeaderView(0)
        val name = h.findViewById<TextView>(R.id.textNameUser)
        val image = h.findViewById<ImageView>(R.id.imageUser)

        name.setText(user?.name)
        image.setImageResource(R.drawable.no_user)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.carrinho -> startActivity(Intent(this, CartActivity::class.java))
            R.id.qrcode -> {
                val i = Intent(this, QrCodeActivity::class.java)
                startActivityForResult(i, 1)
            }
        }
        return toggle.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val qrcodeId = data?.getStringExtra("qrcode") as String

            val i = Intent(this, EventActivity::class.java)
            i.putExtra("qrcode", qrcodeId)
            startActivity(i)

        }
    }
}