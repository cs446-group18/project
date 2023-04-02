package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "airport_info")
data class AirportInfoEntity(
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

@Dao
interface AirportInfoDao : ClientCacheDao<AirportInfoEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: AirportInfoEntity)

    @Query("SELECT * from airport_info WHERE id = :id")
    override fun getItem(id: String): AirportInfoEntity?
}