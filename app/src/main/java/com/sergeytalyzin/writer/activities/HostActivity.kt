package com.sergeytalyzin.writer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.fragments.NewsFragment
import com.sergeytalyzin.writer.fragments.ProfileFragment
import com.sergeytalyzin.writer.presenters.HostPresenter
import com.sergeytalyzin.writer.views.HostView
import kotlinx.android.synthetic.main.activity_host.*

class HostActivity : MvpAppCompatActivity(), HostView {

    @InjectPresenter
    lateinit var hostPresenter: HostPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        hostPresenter.getUser()

        if(supportFragmentManager.findFragmentById(R.id.host_host_activity) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.host_host_activity, NewsFragment())
                .commit()
        }

        news_btn_host_activity.setOnClickListener {
            changeFragment(NewsFragment())
        }

        profile_btn_host_activity.setOnClickListener {
            changeFragment(ProfileFragment())
        }
    }

    override fun showData() {
        wrapper_host.visibility = View.VISIBLE
        loading_host.visibility = View.GONE
    }

    override fun startLogin() {
        startActivity(Intent(this@HostActivity, LoginActivity::class.java))
        finish()
    }

    override fun showBottomPanel() {
        bottom_panel_host_activity.visibility = View.VISIBLE
    }

    override fun closeBottomPanel() {
        bottom_panel_host_activity.visibility = View.GONE
    }

    override fun changeFragment(fragment: Fragment, addToBackStack: Boolean) {

        supportFragmentManager.fragments.forEach {
            if(it !is ProfileFragment && it !is NewsFragment)
                supportFragmentManager.beginTransaction().remove(it).commit()
        }

        if(fragment.javaClass.simpleName == ProfileFragment().javaClass.simpleName
            || fragment.javaClass.simpleName == NewsFragment().javaClass.simpleName) {

            if (!supportFragmentManager.popBackStackImmediate(fragment.javaClass.simpleName, 0)) {

                val oldFragment = supportFragmentManager.findFragmentById(R.id.host_host_activity)

                if (fragment.javaClass.simpleName != oldFragment!!.javaClass.simpleName) {

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.host_host_activity, fragment)
                    if (addToBackStack) transaction.addToBackStack(fragment.javaClass.simpleName)
                    transaction.commit()
                }
            }
        }
        else {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.host_host_activity, fragment)
            if (addToBackStack) transaction.addToBackStack(fragment.javaClass.simpleName)
            transaction.commit()
            hostPresenter.closeBottomPanel()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hostPresenter.showBottomPanel()
    }
}
