package me.cniekirk.trackbuddy.data.repository

import androidx.datastore.core.DataStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.first
import me.cniekirk.trackbuddy.datastore.Preferences
import me.cniekirk.trackbuddy.domain.repository.AnalyticsRepository
import me.cniekirk.trackbuddy.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val datastore: DataStore<Preferences>,
    private val analyticsRepository: AnalyticsRepository,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : PreferencesRepository {

    override suspend fun setIsNormalLaunch(isNormalLaunch: Boolean) {
        datastore.updateData { preferences ->
            preferences.toBuilder().setNormalLaunch(isNormalLaunch).build()
        }
    }

    override suspend fun getIsNormalLaunch(): Boolean = datastore.data.first().normalLaunch

    override suspend fun setIsAnalyticsEnabled(isAnalyticsEnabled: Boolean) {
        analyticsRepository.setEnabled(isAnalyticsEnabled)
        datastore.updateData { preferences ->
            preferences.toBuilder().setAnalyticsEnabled(isAnalyticsEnabled).build()
        }
    }

    override suspend fun getIsAnalyticsEnabled(): Boolean = datastore.data.first().analyticsEnabled

    override suspend fun setIsCrashlyticsEnabled(isCrashlyticsEnabled: Boolean) {
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(isCrashlyticsEnabled)
        datastore.updateData { preferences ->
            preferences.toBuilder().setCrashlyticsEnabled(isCrashlyticsEnabled).build()
        }
    }

    override suspend fun getIsCrashlyticsEnabled(): Boolean = datastore.data.first().crashlyticsEnabled
}