package me.cniekirk.trackbuddy.domain.repository

interface PreferencesRepository {

    suspend fun setIsNormalLaunch(isNormalLaunch: Boolean)

    suspend fun getIsNormalLaunch(): Boolean

    suspend fun setIsAnalyticsEnabled(isAnalyticsEnabled: Boolean)

    suspend fun getIsAnalyticsEnabled(): Boolean

    suspend fun setIsCrashlyticsEnabled(isCrashlyticsEnabled: Boolean)

    suspend fun getIsCrashlyticsEnabled(): Boolean
}