package com.demo.developer.deraesw.demomoviewes.ui.synchronize_data

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentSynchronizedDataBinding
import com.demo.developer.deraesw.demomoviewes.extension.debug
import dagger.hilt.android.AndroidEntryPoint

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
                if (it.lastDateSync == "" && it.syncStatus == AccountData.SyncStatus.NO_SYNC) {
                    Handler().postDelayed({
                        viewModel.callFullSyncData(it)
                    }, 1000)
                }
            }
        })

        viewModel.syncStatus.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    AccountData.SyncStatus.SYNC_INIT_DONE -> {
                        debug("init done launch service")
                        binding.tvInformationMessage.text = ""
                        showSuccessIcon(true)
                        showProgressBar(false)
                        showFailedIcon(false)
                        Handler().postDelayed({
                            this.findNavController().popBackStack()
                        }, 2000)

                    }
                    AccountData.SyncStatus.SYNC_FAILED -> {
                        binding.tvInformationMessage.text =
                            it.networkError?.statusMessage ?: "unknown error"
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
        })

        viewModel.syncInformationMessage.observe(this, Observer {
            if (it != null) {
                binding.tvInformationMessage.text = it
            }
        })

        binding.btnRetry.setOnClickListener {
            accountData?.apply {
                viewModel.resetFailedStatus(this)
            }
            binding.btnRetry.visibility = View.INVISIBLE
        }

        return binding.root
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