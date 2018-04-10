package com.demo.developer.deraesw.demomoviewes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.ui.synchronize_data.SynchronizedDataActivity
import com.demo.developer.deraesw.demomoviewes.utils.Injection

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = Injection.provideMainActivityFactory(this)
        val viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel::class.java)

        viewModel.movieGenreList.observe(this, Observer {
            if(it != null){
                Toast.makeText(this, "MainAct =>  Size is "+it.size, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.accountData.observe(this, Observer {
            if(it != null){
                Toast.makeText(this, "MainAct Account sync date ${it.lastDateSync}", Toast.LENGTH_SHORT).show()
                if(it.lastDateSync == "" && it.syncStatus == AccountData.SyncStatus.NO_SYNC){
                    Log.d(TAG, "Call sync")
                    Toast.makeText(this, "MainAct : Start sync", Toast.LENGTH_SHORT).show()
                    it.syncStatus = AccountData.SyncStatus.SYNC_PROGRESS
                    viewModel.callUpdateAccountData(it)
                    viewModel.callFetchMovieGenreList()
                }
            }
        })
    }

}
