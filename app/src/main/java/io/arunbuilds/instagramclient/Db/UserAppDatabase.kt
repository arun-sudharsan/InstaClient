package io.arunbuilds.instagramclient.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.arunbuilds.instagramclient.Db.enitity.Converters
import io.arunbuilds.instagramclient.Db.enitity.UserData


@Database(entities = [UserData::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserAppDatabase : RoomDatabase() {
    abstract val userdataDAO: UserdataDAO


    companion object {
        private var userAppDatabase: UserAppDatabase? = null
        fun getInstance(context: Context): UserAppDatabase {
            if (null == userAppDatabase) {
                userAppDatabase = buildDatabaseInstance(context)
            }
            return userAppDatabase as UserAppDatabase
        }

        private fun buildDatabaseInstance(context: Context): UserAppDatabase {
            return Room.databaseBuilder(
                context,
                UserAppDatabase::class.java,
                "user.db"
            ).build()
        }

        fun cleanUp() {
            userAppDatabase = null
        }
    }

}