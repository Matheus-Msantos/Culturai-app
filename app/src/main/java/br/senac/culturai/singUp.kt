package br.senac.culturai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        val editConfirmEmail = binding.editCreateConfirmEmail
        val editCreatePass = binding.editCreatePassword
        val editConfirmPass = binding.editCreateConfirmPassword
        val buttonCreateAccount= binding.buttonCreateAccount

        buttonCreateAccount.setOnClickListener{

            when {

                /*--- Validação dos campos ---*/

                editCreateName.text.isNullOrEmpty() -> {
                    editCreateName.error = "Preencha o campo de Nome"
                    Toast.makeText(this@singUp, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                editCreateEmail.text.isNullOrEmpty() -> {
                    editCreateEmail.error = "Preencha o campo de e-mail"
                    Toast.makeText(this@singUp, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                editCreateEmail.text.isNullOrEmpty() -> {
                    editConfirmEmail.error = "Preencha o campo de confirmação de e-mail"
                    Toast.makeText(this@singUp, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                editCreateEmail.text.toString() != editConfirmEmail.text.toString() -> {
                    editCreateEmail.error = "Os E-mail estão diferentes"
                    editConfirmEmail.error = "Os E-mail estão diferentes"
                    Toast.makeText(this@singUp, "E-mail e confirmação de e-mail estão diferentes", Toast.LENGTH_LONG).show()
                }

                editCreatePass.text.isNullOrEmpty() -> {
                    editCreatePass.error = "Preencha o campo de senha"
                    Toast.makeText(this@singUp, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
                }

                editConfirmPass.text.isNullOrEmpty() -> {
                    editConfirmPass.error = "Preencha o campo de confirmação de senha"
                }

                editCreatePass.text.toString() != editConfirmPass.text.toString() -> {
                    editCreatePass.error = "As senhas estão diferentes"
                    editConfirmPass.error = "As senhas estão diferentes"
                    Toast.makeText(this@singUp, "Senha e confirmação de senha estão diferentes", Toast.LENGTH_LONG).show()
                }

                else -> {

                    /*--- Chamada do serviço ---*/

                    val i = Intent(this, LoginActivity::class.java)
                    val name = editCreateName.text.toString()
                    val email = editCreateEmail.text.toString()
                    val pass = editCreatePass.text.toString()

                    val callback = object: Callback<Token>{
                        override fun onResponse(call: Call<Token>, response: Response<Token>) {
                            if(response.isSuccessful){
                                i.putExtra("cadastro", "Cadastro efeituado, Por favor realize o login!")
                                startActivity(i)
                            }else{
                                Snackbar.make(binding.layouCreateAccount, "Não foi possível fazer o cadastro. Por favor tente mais tarde", Snackbar.LENGTH_LONG).show()
                                Log.e("Error", response.errorBody().toString())
                            }
                        }

                        override fun onFailure(call: Call<Token>, t: Throwable) {
                            Snackbar.make(binding.layouCreateAccount, "Estamos com problemas para se conectar com o servidor!", Snackbar.LENGTH_LONG).show()
                            Log.e("Error", "Falha ao executar serviço de cadastro!", t)
                        }

                    }

                    API(this).singUp.singUp(SingUp(name, email, pass)).enqueue(callback)

                }
            }

        }
    }
}