package com.demo.developer.deraesw.demomoviewes.ui.synchronize_data

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.developer.deraesw.demomoviewes.R

class SynchronizedDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synchronized_data)
        /*setSupportActionBar(toolbar)

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
        })*/
    }

}
