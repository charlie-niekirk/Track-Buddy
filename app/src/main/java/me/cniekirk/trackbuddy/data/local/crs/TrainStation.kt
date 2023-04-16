package me.cniekirk.trackbuddy.data.local.crs

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class TrainStation(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "station_name") val name: String,
    @ColumnInfo(name = "station_code") val code: String
) : Parcelable
