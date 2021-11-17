package br.senac.culturai


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
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

        val callback = object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if(response.isSuccessful && user != null){
                    showUserUI(user)

                }else if(response.code() == 401) {
                    Snackbar.make(binding.drawerLayout, "Falha na autentição, faça o login novamente", Snackbar.LENGTH_LONG)
                        .show()
                } else{
                    var msg = response.message().toString()
                    if(msg == "") {
                        msg = "Não foi possivel entrar na conta"
                    }

                    Snackbar.make(binding.drawerLayout, msg, Snackbar.LENGTH_LONG)
                        .show()
                    response.errorBody()?.let{
                        Log.e("Error", it.string())
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Snackbar.make(binding.drawerLayout, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                    .show()
                Log.e("Error", "Falha ao executar serviço", t)
            }

        }


        API(this).account.user().enqueue(callback)



    }

    fun showUserUI(user: User?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.carrinho -> startActivity(Intent(this, CartActivity::class.java))
        }
        return toggle.onOptionsItemSelected(item)
    }
}