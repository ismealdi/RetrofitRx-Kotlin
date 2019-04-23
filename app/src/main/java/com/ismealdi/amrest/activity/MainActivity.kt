package com.ismealdi.amrest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.ismealdi.amrest.R
import com.ismealdi.amrest.api.Auth
import com.ismealdi.amrest.model.request.SignInRequest
import com.ismealdi.amrest.model.request.SignUpRequest
import com.ismealdi.amrest.model.response.BaseResponse
import com.ismealdi.amrest.model.schema.User
import com.ismealdi.amrest.util.Dialogs
import com.ismealdi.amrest.util.Preferences
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val auth by lazy { Auth.init(this) }
    private var disposable : Disposable? = null
    private var progress: KProgressHUD? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initData()
        initListener()

        isSignIn(Preferences(this).getToken().isEmpty())
    }

    private fun initData() {
        progress = Dialogs().initProgressDialog(this)
    }

    private fun initListener() {
        buttonSignIn.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            
            if(validate(email, password)) {
                val signInRequest = SignInRequest(email, password)
                signIn(signInRequest)
            }
        }
        
        buttonSignUp.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val confirm = inputPasswordConfirm.text.toString()
            val name = inputName.text.toString()
            
            if(validate(email, password, confirm, name, true)) {
                val singUpRequest = SignUpRequest(name, email, password, confirm)
                signUp(singUpRequest)
            }
        }

        buttonSignOut.setOnClickListener { 
            signOut()
        }

        buttonLoadUser.setOnClickListener { 
            getUsers()
        }
    }
    
    private fun getUsers() {
        labelUsers.text = "Processing .."

        disposable = auth.users().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { result ->
                    hideProgress()

                    result?.let {
                        labelUsers.text = it.data.joinToString { user -> user.name }
                        
                    } ?: run {
                        message("Failed, ada harus login atau data kosong!")
                    }

                }, { error ->
                    hideProgress()
                    labelUsers.text = ""
                    message(error.message.toString())
                }
            )
        
    }

    private fun signUp(singUpRequest: SignUpRequest) {
        showProgress()

        disposable = auth.register(singUpRequest).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { result ->
                    hideProgress()

                    result?.let {
                        message("Berhasi, Hallo ${it.data.name}")
                        Preferences(this).storeName(it.data.name)
                        Preferences(this).storeToken(it.data.token)
                        isSignIn(false)
                    } ?: run {
                        message("Gagal Login")
                    }

                }, { error ->
                    hideProgress()
                    message(error.message.toString())
                }
            )
    }

    private fun signIn(signInRequest: SignInRequest) {
        showProgress()
        
        disposable = auth.signIn(signInRequest).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { result ->
                    hideProgress()

                    result?.let {
                        message("Berhasi, Hallo ${it.data.name}")
                        Preferences(this).storeName(it.data.name)
                        Preferences(this).storeToken(it.data.token)
                        isSignIn(false)
                    } ?: run {
                        message("Gagal Login")
                    }
                    
                }, { error ->
                    hideProgress()
                    message(error.message.toString())
                }
            )
    }
    
    private fun signOut() {
        showProgress()

        disposable = auth.signOut().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { result ->
                    hideProgress()

                    result?.let {
                        message("Keluar")
                        Preferences(this).storeToken("")
                        Preferences(this).storeName("")
                        isSignIn(true)
                    } ?: run {
                        message("Gagal logout")
                    }

                }, { error ->
                    hideProgress()
                    message(error.message.toString())
                }
            )
    }

    private fun isSignIn(state: Boolean) {
        if(state) {
            buttonSignIn.visibility = View.VISIBLE
            buttonSignUp.visibility = View.VISIBLE
            inputName.visibility = View.VISIBLE
            inputEmail.visibility = View.VISIBLE
            inputPassword.visibility = View.VISIBLE
            inputPasswordConfirm.visibility = View.VISIBLE
            buttonSignOut.visibility = View.GONE
        }else{
            labelMessage.text = "Hallo, ${Preferences(this).getName()}"
            
            buttonSignIn.visibility = View.GONE
            buttonSignUp.visibility = View.GONE
            inputName.visibility = View.GONE
            inputEmail.visibility = View.GONE
            inputPassword.visibility = View.GONE
            inputPasswordConfirm.visibility = View.GONE
            buttonSignOut.visibility = View.VISIBLE
        }
        
    }

    private fun showProgress() {
        progress?.show()
    }

    private fun message(text: String) {
        labelMessage.text = text

        Handler().postDelayed({
            labelMessage.text = ""
        }, 2500)
    }

    private fun hideProgress() {
        progress?.dismiss()
    }

    private fun validate(email: String, password: String, confirm: String = "", name: String = "", isSignUp: Boolean = false): Boolean {
        
        if(email.isEmpty() || password.isEmpty()) {
            message("Email dan password tidak boleh kosong!")
            return false
        }
        
        if(password.length < 8) {
            message("Password kurang dari 8 karakter!")
            return false
        }
        
        if(isSignUp) {
            if(password != confirm) {
                message("Password tidak sama!")
                return false
            }
            
            if(name.isEmpty()) {
                message("Nama tidak boleh kosong!")
                return false
            }
        }
        
        return true
    }
}
