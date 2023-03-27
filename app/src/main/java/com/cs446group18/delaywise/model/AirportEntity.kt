package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "airport")
data class AirportEntity(
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

@Dao
interface AirportDao : ClientCacheDao<AirportEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: AirportEntity)

    @Query("SELECT * from airport WHERE id = :id")
    override fun getItem(id: String): AirportEntity?
}
