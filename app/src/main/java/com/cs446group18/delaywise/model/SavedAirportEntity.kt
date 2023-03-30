package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "saved_airports")
data class SavedAirportEntity(
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

@Dao
interface SavedAirportDao : ClientCacheDao<SavedAirportEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: SavedAirportEntity)

    @Delete()
    suspend fun delete(entity: SavedAirportEntity)

    @Query("SELECT * from saved_airports WHERE id = :id")
    override fun getItem(id: String): SavedAirportEntity?

    @Query("SELECT * from saved_airports")
    fun listAirports(): List<SavedAirportEntity>
}
