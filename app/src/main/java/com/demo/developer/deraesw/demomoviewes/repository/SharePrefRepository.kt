package com.demo.developer.deraesw.demomoviewes.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharePrefRepository
@Inject constructor(@ApplicationContext mContext: Context) {

    val account: MutableLiveData<AccountData> = MutableLiveData()
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(mContext)

    init {
        fetchAccountInformation()
    }

    fun fetchAccountInformationDirectly(): AccountData {
        return getAccountData()
    }

    private fun fetchAccountInformation() {
        account.postValue(getAccountData())
    }

    fun updateAccountInformation(accountDate : AccountData){
        val editor = sharedPreferences.edit()
        editor.putString(Constant.ShareKey.LAST_DATE_SYNC, accountDate.lastDateSync)
        editor.putInt(Constant.ShareKey.SYNC_STATUS, accountDate.syncStatus)
        editor.apply()
        fetchAccountInformation()
    }

    private fun getAccountData() : AccountData{
        val accountData = AccountData()
        accountData.lastDateSync = sharedPreferences.getString(Constant.ShareKey.LAST_DATE_SYNC, "") ?: ""
        accountData.syncStatus   = sharedPreferences.getInt(Constant.ShareKey.SYNC_STATUS, AccountData.SyncStatus.NO_SYNC)

        return accountData
    }
}