package com.demo.developer.deraesw.demomoviewes.ui.synchronize_data

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentSynchronizedDataBinding
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.MainActivityViewModel
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class SynchronizedDataActivityFragment : DaggerFragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var binding: FragmentSynchronizedDataBinding
    lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSynchronizedDataBinding.inflate(layoutInflater, container, false)

        viewModel = viewModelProvider(factory)

        viewModel.accountData.observe(this, Observer {
            if(it != null) {
                //Meaning first time open application or clear data
                if((it.lastDateSync == "" && it.syncStatus == AccountData.SyncStatus.NO_SYNC) || (it.lastDateSync != AppTools.getCurrentDate() && it.syncStatus == AccountData.SyncStatus.SYNC_DONE)){
                    Handler().postDelayed({
                        viewModel.callFullSyncData(it)
                    }, 1000)
                }
            }
        })

        viewModel.syncStatus.observe(this, Observer {
            if (it != null) {
                binding.progressBar.visibility = View.INVISIBLE
                when (it.status) {
                    AccountData.SyncStatus.SYNC_INIT_DONE -> {
                        debug("init done launch service")
                        binding.tvInformationMessage.text = ""
                        Handler().postDelayed({
                            this.findNavController().popBackStack()
                        }, 2000)

                    }
                    AccountData.SyncStatus.SYNC_FAILED -> {
                        binding.tvInformationMessage.text = it.networkError?.statusMessage ?: "unknown error"
                        debug("Failed status receive")
                        error("Error => ${it.networkError?.statusMessage}")
                        //todo display failed info
                    }
                    AccountData.SyncStatus.SYNC_PROGRESS -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.syncInformationMessage.observe(this, Observer {
            if(it != null) {
                binding.tvInformationMessage.text = it
            }
        })

        return binding.root
    }
}
