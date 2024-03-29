package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "flight_info")
data class FlightInfoEntity(
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

@Dao
interface FlightInfoDao : ClientCacheDao<FlightInfoEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: FlightInfoEntity)

    @Query("SELECT * from flight_info WHERE id = :id")
    override fun getItem(id: String): FlightInfoEntity?
}