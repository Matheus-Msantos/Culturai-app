package br.senac.culturai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.senac.culturai.api.API
import br.senac.culturai.databinding.FragmentAccountBinding
import br.senac.culturai.model.User
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {
lateinit var binding: FragmentAccountBinding

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = FragmentAccountBinding.inflate(inflater)

    val callback = object: Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            val user = response.body()
            if(response.isSuccessful && user != null){
                showUserUI(user)

            }else if(response.code() == 401) {
                Snackbar.make(binding.ConstraintLayout, "Falha na autentição, faça o login novamente", Snackbar.LENGTH_LONG)
                    .show()
            } else{
                var msg = response.message().toString()
                if(msg == "") {
                    msg = "Não foi possivel entrar na conta"
                }

                Snackbar.make(binding.ConstraintLayout, msg, Snackbar.LENGTH_LONG)
                    .show()
                response.errorBody()?.let{
                    Log.e("Error", it.string())
                }

                val i = Intent(activity, LoginActivity::class.java)
                startActivity(i)
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Snackbar.make(binding.ConstraintLayout, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG)
                .show()
            Log.e("Error", "Falha ao executar serviço", t)
        }

    }

    val cxt = getActivity()?.getApplicationContext()
    if (cxt != null) {
        API(cxt).account.user().enqueue(callback)
    }

    return binding.root
}

    fun showUserUI(user: User?) {

        Picasso.get().load("http://10.0.2.2:8000/${user?.image}")
            .error(R.drawable.no_user)
            .into(binding.imageAccountUser)

        binding.textAccountName.text = user?.name
        binding.textAccountName2.text = user?.name
        binding.textAccountEmail.text = user?.email
        if(user?.isAdmin == 0) {
            binding.textAccountAdmin.text = "Não"
        }else {
            binding.textAccountAdmin.text = "Sim"
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountFragment()

    }
}