package me.cniekirk.trackbuddy.domain.repository

interface AnalyticsRepository {

    fun logEvent(eventName: String, vararg params: Pair<String, String>)

    fun logScreen(screenName: String, screenClass: String)

    fun setEnabled(enabled: Boolean)
}