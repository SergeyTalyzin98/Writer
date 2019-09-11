package com.sergeytalyzin.writer.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.presenters.LoginPresenter
import com.sergeytalyzin.writer.views.LoginView
import com.vk.api.sdk.VK
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : MvpAppCompatActivity(), LoginView {

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_authorization_login.setOnClickListener {
            VK.login(this@LoginActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(loginPresenter.authorization(requestCode = requestCode, resultCode = resultCode, data = data))
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showStartLoading() {
        btn_authorization_login.visibility = View.GONE
        loading_login.visibility = View.VISIBLE
    }

    override fun showEndLoading() {
        btn_authorization_login.visibility = View.VISIBLE
        loading_login.visibility = View.GONE
    }

    override fun startHostActivity() {

        startActivity(Intent(this@LoginActivity, HostActivity::class.java))
        finish()
    }

    override fun showError(strId: Int, errorCode: Int) {
        val text =  "${resources.getText(strId)} $errorCode"
        error_login.text = text
    }

    override fun deleteError() {
        error_login.text = ""
        val pas = getSharedPreferences("pass", Context.MODE_PRIVATE)
        pas.edit().putInt("age", 8).apply()
    }
}
