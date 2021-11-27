package br.senac.culturai

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.senac.culturai.api.API
import br.senac.culturai.api.FILE_LOGIN
import br.senac.culturai.api.UserApi
import br.senac.culturai.databinding.ActivityLoginBinding
import br.senac.culturai.model.Login
import br.senac.culturai.model.Token
import br.senac.culturai.model.User
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.myToolBar))

        val editLoginEmail = binding.editLoginEmail
        val editLoginPassword = binding.editLoginPassword
        val buttonLogin = binding.buttonLogin
        val cadastro = intent.getStringExtra("cadastro")

        if(cadastro != null) {
            Toast.makeText(this@LoginActivity, "${cadastro}", Toast.LENGTH_LONG).show()
        }

        buttonLogin.setOnClickListener{

            when {

                /*--- Validação dos campos ---*/

                editLoginEmail.text.isNullOrEmpty() -> {
                    editLoginEmail.error = "Preencha o campo de E-mail"
                    Toast.makeText(this@LoginActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                editLoginPassword.text.isNullOrEmpty() -> {
                    editLoginPassword.error = "Preencha o campo de Senha"
                    Toast.makeText(this@LoginActivity, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                else -> {

                    /*--- Chamada Serviço ---*/

                    val email = editLoginEmail.text.toString()
                    val pass = editLoginPassword.text.toString()

                    val callback = object: Callback<Token> {
                        override fun onResponse(call: Call<Token>, response: Response<Token>) {
                            val token = response.body()

                            if(response.isSuccessful && token != null) {
                                val editor = getSharedPreferences(FILE_LOGIN, Context.MODE_PRIVATE).edit()
                                editor.putString("email", email)
                                editor.putString("pass", pass)
                                editor.putString("token", token.token)
                                editor.apply()

                                Toast.makeText(this@LoginActivity, "Login efetuado!", Toast.LENGTH_LONG).show()

                            } else {
                                var msg = response.message().toString()
                                if(msg == "") {
                                    msg = "Não foi possivel efutuar login"
                                }

                                Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()

                                response.errorBody()?.let{
                                    Log.e("Error", it.string())
                                }
                            }
                        }

                        override fun onFailure(call: Call<Token>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "Estamos com problemas para se conectar com o servidor!", Toast.LENGTH_LONG).show()
                            Log.e("Error", "Falha ao executar serviço Login!", t)
                        }

                    }
                    API(this).login.login(Login(pass, email)).enqueue(callback)

                }
            }
        }

        binding.txtNotRegistered.setOnClickListener{
            val i = Intent(this, singUp::class.java)
            startActivity(i)
        }

    }
}