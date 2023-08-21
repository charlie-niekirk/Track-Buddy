package me.cniekirk.trackbuddy.ui.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackBuddyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        withContext(Dispatchers.IO) {

        }

        provideContent {

        }
    }
}