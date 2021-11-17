package br.senac.culturai.api

import android.content.Context
import br.senac.culturai.model.Login

import okhttp3.*

class AuthenticatorToken(private val context: Context): Interceptor, Authenticator {

    override fun intercept(chain: Interceptor.Chain): Response {
       val prefs = context.getSharedPreferences(FILE_LOGIN, Context.MODE_PRIVATE)

        val token = prefs.getString("token", "") as String

        var request = chain.request()

        request = request?.newBuilder()?.addHeader("Authorization", "Bearer ${token}")?.build()

        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val prefs = context.getSharedPreferences(FILE_LOGIN, Context.MODE_PRIVATE)

        val email = prefs.getString("email", "") as String
        val pass = prefs.getString("pass", "") as String

        val responseRetrofit = API(context).login.login(Login(pass, email)).execute()

        var token = responseRetrofit.body()
        if(responseRetrofit.isSuccessful && token !=  null){
            val editor = prefs.edit()
            editor.putString("token", token.token)
            editor.apply()

            return response?.request()?.newBuilder()?.header("Authorization", token.token)?.build()
        }

        return null
    }

}