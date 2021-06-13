package com.demo.developer.deraesw.demomoviewes.ui.synchronize_data

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentSynchronizedDataBinding
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.repository.MessageEvent
import com.demo.developer.deraesw.demomoviewes.repository.SynchronizationStatusEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class SynchronizedDataActivityFragment : Fragment() {

    private lateinit var binding: FragmentSynchronizedDataBinding
    private val viewModel: SynchronizedDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSynchronizedDataBinding.inflate(layoutInflater, container, false)

        var accountData: AccountData? = null

        viewModel.accountData.observe(viewLifecycleOwner, {
            if (it != null) {
                accountData = it
                //Meaning first time open application or clear data
                if (it.isNeverSync()) {
                    lifecycleScope.launch {
                        delay(1000)
                        viewModel.callFullSyncData(it)
                    }
                }
            }
        })

        //Method 1, using lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED), more suited for multi flow
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow.collect {
                    when (it) {
                        is MessageEvent -> binding.tvInformationMessage.text = it.message
                        is SynchronizationStatusEvent -> handleSyncStatusUpdate(it)
                    }
                }
            }
        }


        binding.btnRetry.setOnClickListener {
            accountData?.apply {
                viewModel.resetFailedStatus(this)
            }
            binding.btnRetry.visibility = View.INVISIBLE
        }

        return binding.root
    }

    private fun handleSyncStatusUpdate(synchronizationStatusEvent: SynchronizationStatusEvent) {
        when (synchronizationStatusEvent.status.status) {
            AccountData.SyncStatus.SYNC_INIT_DONE -> {
                debug("init done launch service")
                binding.tvInformationMessage.text = ""
                showSuccessIcon(true)
                showProgressBar(false)
                showFailedIcon(false)
                lifecycleScope.launch {
                    delay(2000)
                    this@SynchronizedDataActivityFragment.findNavController().popBackStack()
                }
            }
            AccountData.SyncStatus.SYNC_FAILED -> {
                binding.tvInformationMessage.text =
                    synchronizationStatusEvent.status.networkError?.statusMessage ?: "unknown error"
                showFailedIcon(true)
                showSuccessIcon(false)
                showProgressBar(false)
                binding.btnRetry.visibility = View.VISIBLE
                debug("Failed status receive")
            }
            AccountData.SyncStatus.SYNC_PROGRESS -> {
                showProgressBar(true)
                showFailedIcon(false)
                showSuccessIcon(false)
            }
        }
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.apply {
            if ((show && visibility != View.VISIBLE) || (!show && visibility == View.VISIBLE)) {
                animate().setDuration(200).alpha(
                    (if (show) 1 else 0).toFloat()
                ).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
            }
        }
    }

    private fun showSuccessIcon(show: Boolean) {
        binding.ivSyncSuccess.apply {
            if ((show && visibility != View.VISIBLE) || (!show && visibility == View.VISIBLE)) {
                animate().setDuration(200).alpha(
                    (if (show) 1 else 0).toFloat()
                ).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
            }
        }
    }

    private fun showFailedIcon(show: Boolean) {
        binding.ivSyncFailed.apply {
            if ((show && visibility != View.VISIBLE) || (!show && visibility == View.VISIBLE)) {
                animate().setDuration(200).alpha(
                    (if (show) 1 else 0).toFloat()
                ).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
            }
        }
    }
}