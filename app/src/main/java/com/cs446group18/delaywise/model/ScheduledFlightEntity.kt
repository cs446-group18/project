package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "scheduled_flight")
data class ScheduledFlightEntity(
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

@Dao
interface ScheduledFlightDao : ClientCacheDao<ScheduledFlightEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: ScheduledFlightEntity)

    @Query("SELECT * from scheduled_flight WHERE id = :id")
    override fun getItem(id: String): ScheduledFlightEntity?
}
