package me.cniekirk.trackbuddy.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.cniekirk.trackbuddy.data.local.datastore.PreferencesSerializer
import me.cniekirk.trackbuddy.datastore.Preferences
import javax.inject.Singleton

private const val DATASTORE_FILENAME = "prefs.proto"

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return DataStoreFactory.create(
            serializer = PreferencesSerializer,
            produceFile = { context.dataStoreFile(DATASTORE_FILENAME) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}