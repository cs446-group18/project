package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "airport_delay")
data class AirportDelayEntity(
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

@Dao
interface AirportDelayDao : ClientCacheDao<AirportDelayEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: AirportDelayEntity)

    @Query("SELECT * from airport_delay WHERE id = :id")
    override fun getItem(id: String): AirportDelayEntity?
}