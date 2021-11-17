package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.culturai.api.API
import br.senac.culturai.databinding.ActivitySingUpBinding
import br.senac.culturai.model.SingUp
import br.senac.culturai.model.Token
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class singUp : AppCompatActivity() {

    lateinit var binding: ActivitySingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editCreateName = binding.editCreateName
        val editCreateEmail = binding.editCreateEmail
        val editConfirmEmail = binding.editConfirmEmail
        val editCreatePass = binding.editCreatePass
        val editConfirmPass = binding.editConfirmPass
        val buttonCreateAccount= binding.buttonCreateAccount

        buttonCreateAccount.setOnClickListener{
            val i = Intent(this, LoginActivity::class.java)
            val name = editCreateName.text.toString()
            val email = editCreateEmail.text.toString()
            val pass = editCreatePass.text.toString()

            val callback = object: Callback<Token>{
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if(response.isSuccessful){
                        Snackbar.make(binding.layouCreateAccount, "Cadastro efetuado", Snackbar.LENGTH_LONG)
                            .show()

                        startActivity(i)
                    }else{
                        Snackbar.make(binding.layouCreateAccount, "Não é possivel atualizar os produtos", Snackbar.LENGTH_LONG)
                            .show()

                        Log.e("Error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Snackbar.make(binding.layouCreateAccount, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("Error", "Falha ao executar serviço", t)
                }

            }



            API(this).singUp.singUp(SingUp(name, email, pass)).enqueue(callback)
        }
    }
}