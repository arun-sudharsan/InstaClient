package io.arunbuilds.instagramclient.Db.enitity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "users")
data class UserData(
    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "user_name")
    val username: String,
    @ColumnInfo(name = "user_profile_link")
    val userprofilepic: String,
    @ColumnInfo(name = "user_bio")
    val userbio: String,
    val date: Date
)


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}