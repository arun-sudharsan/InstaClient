package io.arunbuilds.instagramclient.Db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.arunbuilds.instagramclient.Db.enitity.Converters
import io.arunbuilds.instagramclient.Db.enitity.UserData

@Database(entities = [UserData::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserAppDatabase : RoomDatabase() {
    abstract val userdataDAO: UserdataDAO
}