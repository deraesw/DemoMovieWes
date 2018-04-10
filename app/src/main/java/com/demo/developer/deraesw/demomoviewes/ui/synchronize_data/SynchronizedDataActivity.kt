package com.demo.developer.deraesw.demomoviewes.ui.synchronize_data

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.demo.developer.deraesw.demomoviewes.MainActivityViewModel
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.utils.Injection

import kotlinx.android.synthetic.main.activity_synchronized_data.*

class SynchronizedDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synchronized_data)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val factory = Injection.provideMainActivityFactory(this)
        val viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel::class.java)

        viewModel.movieGenreList.observe(this, Observer {
            if(it != null){
                Toast.makeText(this, "SyncApp : Size is "+it.size, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.accountData.observe(this, Observer {
            if(it != null){
                Toast.makeText(this, "SyncApp :Account sync date ${it.lastDateSync}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
