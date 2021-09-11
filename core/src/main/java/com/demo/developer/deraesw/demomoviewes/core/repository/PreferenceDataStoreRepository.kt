package com.demo.developer.deraesw.demomoviewes.core.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.demo.developer.deraesw.demomoviewes.core.data.model.AccountData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataStoreRepository
@Inject constructor(@ApplicationContext val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATA_STORE_NAME
    )

    private var _accountData = AccountData()
    val accountData = context.dataStore.data.map { preferences ->
        _accountData.apply {
            lastDateSync = preferences[KEY_LAST_DATE_SYNC] ?: ""
            syncStatus = preferences[KEY_SYNC_STATUS] ?: 0
        }
    }

    suspend fun updateAccountInformation(accountDate: AccountData) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LAST_DATE_SYNC] = accountDate.lastDateSync
            preferences[KEY_SYNC_STATUS] = accountDate.syncStatus
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "demo_movies_pref"

        private val KEY_LAST_DATE_SYNC = stringPreferencesKey("LAST_DATE_SYNC")
        private val KEY_SYNC_STATUS = intPreferencesKey("SYNC_STATUS")
    }
}