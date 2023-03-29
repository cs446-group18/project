package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "saved_flights")
data class SavedFlightEntity(
    @PrimaryKey
    val id: String,
    val json: String,
    var updatedAt: Long = System.currentTimeMillis()
)

@Dao
interface SavedFlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedFlightEntity)

    @Delete()
    suspend fun delete(entity: SavedFlightEntity)

    @Query("SELECT * from saved_flights WHERE id = :id")
    fun getItem(id: String): SavedFlightEntity?

    @Query("SELECT * from saved_flights")
    fun listFlights(): List<SavedFlightEntity>
}
