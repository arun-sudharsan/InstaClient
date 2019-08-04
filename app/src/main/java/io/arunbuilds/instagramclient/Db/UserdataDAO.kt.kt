package io.arunbuilds.instagramclient.Db

import androidx.room.*
import io.arunbuilds.instagramclient.Db.enitity.UserData
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface UserdataDAO {

    @Insert
    fun addUserData(userData: UserData): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUserData(userData: UserData)

    @Delete
    fun deleteUserData(userData: UserData)

    @Query("select * from users")
    fun getUserData(): Observable<List<UserData>>

    @Query("select * from users where user_id ==:id")
    fun getUserDatabyId(id:Long):Single<UserData>
}