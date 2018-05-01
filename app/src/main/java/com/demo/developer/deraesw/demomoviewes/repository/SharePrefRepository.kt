package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SharePrefRepository
@Inject constructor(@Named("context_app") mContext : Context){
    private val TAG = SharePrefRepository::class.java.simpleName

    val account : MutableLiveData<AccountData> = MutableLiveData()
    internal val sharedPreferences : SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)

    init {
        fetchAccountInformation()
    }

    fun fetchAccountInformationDirectly() : AccountData{
        return getAccountData()
    }

    fun fetchAccountInformation(){
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
        accountData.lastDateSync = sharedPreferences.getString(Constant.ShareKey.LAST_DATE_SYNC, "")
        accountData.syncStatus   = sharedPreferences.getInt(Constant.ShareKey.SYNC_STATUS, AccountData.SyncStatus.NO_SYNC)

        return accountData
    }

    companion object {
        @Volatile private var sInstance :SharePrefRepository? = null

        fun getInstance(context: Context) : SharePrefRepository {
            sInstance ?: synchronized(this){
                sInstance = SharePrefRepository(context)
            }

            return sInstance!!
        }
    }
}