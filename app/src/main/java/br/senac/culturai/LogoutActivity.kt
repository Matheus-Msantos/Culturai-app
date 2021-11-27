package br.senac.culturai

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import br.senac.culturai.api.API
import br.senac.culturai.api.FILE_LOGIN
import br.senac.culturai.databinding.ActivityLogoutBinding
import br.senac.culturai.model.Login
import br.senac.culturai.model.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.myToolBar))

        binding.buttonLogout.setOnClickListener {view ->
            val email = "sem@email.com"
            val pass = "111111"

            val i = Intent(this, HomeActivity::class.java)

            val callback = object: Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    val token = response.body()

                    if(response.isSuccessful && token != null) {
                        val editor = getSharedPreferences(FILE_LOGIN, Context.MODE_PRIVATE).edit()
                        editor.putString("email", email)
                        editor.putString("pass", pass)
                        editor.putString("token", token.token)
                        editor.apply()

                        Toast.makeText(this@LogoutActivity, "Você saiu da sua conta!", Toast.LENGTH_LONG).show()

                        startActivity(i)

                    } else {
                        var msg = response.message().toString()
                        if(msg == "") {
                            msg = "Não foi possivel sair da sua conta"
                        }

                        Toast.makeText(this@LogoutActivity, msg, Toast.LENGTH_LONG).show()

                        response.errorBody()?.let{
                            Log.e("Error", it.string())
                        }
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                }

            }
            API(this).login.login(Login(pass, email)).enqueue(callback)

        }

        binding.buttonNotExit.setOnClickListener{
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}